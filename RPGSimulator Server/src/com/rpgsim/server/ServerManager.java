package com.rpgsim.server;

import com.rpgsim.server.util.AccountManager;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.rpgsim.common.ApplicationConfigurations;
import com.rpgsim.common.sheets.Account;
import com.rpgsim.common.ConnectionType;
import com.rpgsim.common.FileManager;
import com.rpgsim.common.PrefabID;
import com.rpgsim.common.ServerActions;
import com.rpgsim.common.Vector2;
import com.rpgsim.common.clientpackages.BackgroundUpdateResponse;
import com.rpgsim.common.clientpackages.ClientPackage;
import com.rpgsim.common.clientpackages.ConnectionRequestResponse;
import com.rpgsim.common.clientpackages.NetworkGameObjectDestroyResponse;
import com.rpgsim.common.clientpackages.InstantiateNetworkGameObjectResponse;
import com.rpgsim.common.clientpackages.NetworkGameObjectImageUpdateResponse;
import com.rpgsim.common.clientpackages.NetworkGameObjectTransformUpdate;
import com.rpgsim.common.game.NetworkGameObject;
import com.rpgsim.common.serverpackages.ServerPackage;
import com.rpgsim.common.serverpackages.UpdateType;
import com.rpgsim.common.sheets.UpdateField;
import com.rpgsim.server.util.SheetManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ServerManager extends Listener implements ServerActions
{
    //The server receives all client requests. Other classes are just utilities for the server.
    private final Server server;
    private final ApplicationConfigurations serverConfigurations;
    private final AccountManager accountManager;
    private final ServerGame serverGame;
    private final ServerSheetFrame sheetFrame;
    
    //The window in which the server supervises client/user actions.
    private final ServerFrame serverFrame;
    
    public ServerManager() throws IOException
    {
        WindowAdapter l = new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent we)
            {
                int opt = JOptionPane.showConfirmDialog(null, "You really want to close the server?", "Close Server", JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION)
                {
                    stop();
                }
            }
        };
        this.sheetFrame = new ServerSheetFrame(this);
        this.sheetFrame.setTitle(this.sheetFrame.getTitle() + " - Server");
        this.serverFrame = new ServerFrame(sheetFrame);
        this.serverFrame.addWindowListener(l);
        this.serverFrame.setVisible(true);
        
        
        this.serverConfigurations = new ApplicationConfigurations(FileManager.app_dir + "data\\config.ini");
        
        this.accountManager = new AccountManager();
        try
        {
            this.accountManager.checkAccounts();
        }
        catch (IOException ex)
        {
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Could not open and check all accounts "
                    + "(They are either corrupted or not accessible).", "WARNING", JOptionPane.WARNING_MESSAGE);
        }
        
        this.serverGame = new ServerGame();
        
        server = new Server(16384, 16384);
        server.getKryo().setRegistrationRequired(false);
    }
    
    @Override
    public void received(Connection connection, Object object)
    {
        if (object instanceof ServerPackage)
        {
            ((ServerPackage)object).executeServerAction(this);
        }
    }
    
    @Override
    public void disconnected(Connection connection)
    {
        Account acc = accountManager.getActiveAccount(connection.getID());
        
        if (acc == null)
        {
            //The client wasn't able to pass login and disconnected.
            return;
        }
        
        accountManager.setAccountActive(connection.getID(), accountManager.getActiveAccount(connection.getID()), false);
        Iterator<NetworkGameObject> aux = new ArrayList<>(serverGame.getScene().getGameObjects()).iterator();
        while (aux.hasNext())
        {
            NetworkGameObject go = aux.next();
            if (go.getClientID() == connection.getID())
            {
                onNetworkGameObjectDestroy(go.getObjectID());
            }
        }
        
        try
        {
            accountManager.updateAccountSheet(acc);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        serverFrame.removePlayer(acc);
    }
    
    public void start()
    {
        server.addListener(this);
        server.start();
        int tcp = this.serverConfigurations.getIntegerProperty("TCPPort");
        int udp = this.serverConfigurations.getIntegerProperty("UDPPort");
        try
        {
            server.bind(tcp, udp);
            serverFrame.setTitle(serverFrame.getTitle() + " - TCP Port: " + tcp + " | UDP Port: " + udp);
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null, "Could not bind to ports TCP: " + tcp + " and UDP: " + udp);
            Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }
    
    /**
     * Stops the server application and everything bound to it, including ServerFrame.
     */
    public void stop()
    {
        serverFrame.dispose();
        sheetFrame.dispose();
        server.stop();
        server.close();
    }
    
    public void sendPackage(int connectionID, ClientPackage p)
    {
        server.sendToTCP(connectionID, p);
    }

    @Override
    public void onClientConnection(int connectionID, String username, String password, ConnectionType type)
    {
        switch (type)
        {
            case LOGIN:
                if (accountManager.isAccountRegistered(username, password))
                {
                    if (accountManager.isAccountActive(username, password))
                    {
                        server.sendToTCP(connectionID, 
                            new ConnectionRequestResponse(false, 
                                    "Account is already logged in.", null, null, 
                                    type));
                    }
                    else
                    {
                        try
                        {
                            Account acc = accountManager.getRegisteredAccount(username, password);
                            acc.setConnectionID(connectionID);
                            accountManager.setAccountActive(connectionID, acc, true);
                            server.sendToTCP(connectionID,
                                    new ConnectionRequestResponse(true, "", acc, SheetManager.getDefaultSheetModel(), 
                                            type));
                            for (NetworkGameObject go : serverGame.getScene().getGameObjects())
                            {
                                server.sendToTCP(connectionID,
                                        new InstantiateNetworkGameObjectResponse(go.getObjectID(),
                                                go.getClientID(),
                                                go.transform().position(),
                                                go.getPrefabID(), go.renderer().getImagePath()));
                            }
                            if (serverGame.getBackgroundPath() != null)
                                server.sendToTCP(connectionID, new BackgroundUpdateResponse(serverGame.getBackgroundPath()));
                            serverFrame.addPlayer(acc);
                        }
                        catch (IOException ex)
                        {
                            Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                else
                {
                    server.sendToTCP(connectionID, 
                            new ConnectionRequestResponse(false, 
                                    "Username or password is incorrect.", null,  null, 
                                    type));
                }
                break;
            case REGISTER:
                if (accountManager.isAccountRegistered(username, password))
                {
                    server.sendToTCP(connectionID, 
                            new ConnectionRequestResponse(false, 
                                    "Username already exists.", null,  null, 
                                    type));
                }
                else
                {
                    try
                    {
                        Account acc = accountManager.registerNewAccount(connectionID, username, password, SheetManager.getDefaultSheetModel());
                        acc.setConnectionID(connectionID);
                        accountManager.setAccountActive(connectionID, acc, true);
                        server.sendToTCP(connectionID,
                                new ConnectionRequestResponse(true, "", acc, SheetManager.getDefaultSheetModel(), 
                                        type));
                        for (NetworkGameObject go : serverGame.getScene().getGameObjects())
                            server.sendToTCP(connectionID,
                                    new InstantiateNetworkGameObjectResponse(go.getObjectID(),
                                            go.getClientID(),
                                            go.transform().position(),
                                            go.getPrefabID(), go.renderer().getImagePath()));
                        if (serverGame.getBackgroundPath() != null)
                            server.sendToTCP(connectionID, new BackgroundUpdateResponse(serverGame.getBackgroundPath()));
                        serverFrame.addPlayer(acc);
                    }
                    catch (IOException ex)
                    {
                        Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
        }
    }

    @Override
    public void onNetworkGameObjectTransformUpdate(int id, Vector2 position, Vector2 scale, float rotation, boolean flipX, boolean flipY)
    {
        NetworkGameObject go = serverGame.getScene().getGameObject(id);
        go.transform().position(position);
        go.transform().scale(scale);
        go.transform().rotation(rotation);
        go.transform().flipX(flipX);
        go.transform().flipY(flipY);
        
        sendAll(new NetworkGameObjectTransformUpdate(id, position, scale, rotation, flipX, flipY), false);
    }
    
    @Override
    public void onNetworkGameObjectRequest(Vector2 position, PrefabID pID, int clientID, String imageRelativePath)
    {
        NetworkGameObject go;
        go = new NetworkGameObject(ServerGame.getGameObjectID(), clientID, pID);
        go.renderer().setImagePath(imageRelativePath);
        serverGame.getScene().addGameObject(go);
        sendAll(new InstantiateNetworkGameObjectResponse(go.getObjectID(), go.getClientID(), position, pID, imageRelativePath), true);
    }

    @Override
    public void onNetworkGameObjectImageUpdate(int id, String relativePath)
    {
        serverGame.getScene().getGameObject(id).renderer().setImagePath(relativePath);
        sendAll(new NetworkGameObjectImageUpdateResponse(id, relativePath), true);
    }

    @Override
    public void onNetworkGameObjectDestroy(int id)
    {
        serverGame.getScene().removeGameObject(id);
        sendAll(new NetworkGameObjectDestroyResponse(id), true);
    }

    @Override
    public void onCharacterSheetFieldUpdate(int connectionID, UpdateField field, Object newValue, int propertyIndex, UpdateType type)
    {
        switch (type)
        {
            case ADD:
                sheetFrame.onSheetFieldAddReceived(connectionID, field, newValue, propertyIndex);
                break;
            case REMOVE:
                sheetFrame.onSheetFieldRemoveReceived(connectionID, field, newValue, propertyIndex);
                break;
            case UPDATE:
                sheetFrame.onSheetFieldUpdateReceived(connectionID, field, newValue, propertyIndex);
                break;
            default:
                throw new AssertionError();
        }
    }

    @Override
    public void onBackgroundUpdate(String relativePath)
    {
        serverGame.setBackgroundPath(relativePath);
        sendAll(new BackgroundUpdateResponse(relativePath), true);
    }
    
    private void sendAll(ClientPackage p, boolean tcp)
    {
        if (tcp)
            for (Account acc : accountManager.getActiveAccounts().values())
                server.sendToTCP(acc.getConnectionID(), p);
        else
            for (Account acc : accountManager.getActiveAccounts().values())
                server.sendToUDP(acc.getConnectionID(), p);
    }
    
}
