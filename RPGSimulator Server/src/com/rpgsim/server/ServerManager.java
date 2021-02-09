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
import com.rpgsim.common.clientpackages.ConnectionRequestResponse;
import com.rpgsim.common.clientpackages.DestroyNetworkGameObject;
import com.rpgsim.common.clientpackages.InstantiateNetworkGameObjectResponse;
import com.rpgsim.common.clientpackages.NetworkGameObjectTransformUpdate;
import com.rpgsim.common.game.NetworkGameObject;
import com.rpgsim.common.serverpackages.ServerPackage;
import com.rpgsim.common.sheets.SheetManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
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
    private final ServerGame game;
    private Connection currentConnection;
    
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
        this.serverFrame = new ServerFrame(l);
        this.serverFrame.setVisible(true);
        
        this.serverConfigurations = new ApplicationConfigurations(FileManager.app_dir + "data\\config.ini");
        this.accountManager = new AccountManager();
        this.accountManager.checkAccounts();
        
        this.game = new ServerGame();
        
        server = new Server();
        server.getKryo().setRegistrationRequired(false);
    }
    
    @Override
    public void received(Connection connection, Object object)
    {
        if (object instanceof ServerPackage)
        {
            currentConnection = connection;
            ((ServerPackage)object).executeServerAction(this);
        }
        else
            System.out.println("The data received is not a ServerPackage. Object: " + object);
    }
    
    @Override
    public void disconnected(Connection connection)
    {
        accountManager.setAccountActive(connection.getID(), accountManager.getActiveAccount(connection.getID()), false);
        Iterator<NetworkGameObject> aux = game.getScene().getGameObjects().iterator();
        while (aux.hasNext())
        {
            NetworkGameObject go = aux.next();
            if (go.getClientID() == connection.getID())
            {
                game.getScene().removeGameObject(go.getObjectID());
                server.sendToAllTCP(new DestroyNetworkGameObject(go.getObjectID()));
            }
        }
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
        server.stop();
        server.close();
    }

    @Override
    public void onClientConnection(String username, String password, ConnectionType type)
    {
        switch (type)
        {
            case LOGIN:
                if (accountManager.isAccountRegistered(username, password))
                {
                    if (accountManager.isAccountActive(username, password))
                    {
                        server.sendToTCP(currentConnection.getID(), 
                            new ConnectionRequestResponse(false, 
                                    "Account is already logged in.", null, null, 
                                    type));
                    }
                    else
                    {
                        try
                        {
                            Account acc = accountManager.getRegisteredAccount(username, password);
                            accountManager.setAccountActive(currentConnection.getID(), acc, true);
                            server.sendToTCP(currentConnection.getID(),
                                    new ConnectionRequestResponse(true, "", acc, SheetManager.defaultSheetModel, 
                                            type));
                            for (NetworkGameObject go : game.getScene().getGameObjects())
                                server.sendToTCP(currentConnection.getID(),
                                        new InstantiateNetworkGameObjectResponse(go.getObjectID(),
                                                go.getClientID(),
                                                go.transform().position(),
                                                go.getPrefabID()));
                        }
                        catch (IOException ex)
                        {
                            Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                else
                {
                    server.sendToTCP(currentConnection.getID(), 
                            new ConnectionRequestResponse(false, 
                                    "Username or password is incorrect.", null,  null, 
                                    type));
                }
                break;
            case REGISTER:
                if (accountManager.isAccountRegistered(username, password))
                {
                    server.sendToTCP(currentConnection.getID(), 
                            new ConnectionRequestResponse(false, 
                                    "Username already exists.", null,  null, 
                                    type));
                }
                else
                {
                    try
                    {
                        Account acc = accountManager.registerNewAccount(username, password, SheetManager.defaultSheetModel);
                        accountManager.setAccountActive(currentConnection.getID(), acc, true);
                        server.sendToTCP(currentConnection.getID(),
                                new ConnectionRequestResponse(true, "", acc, SheetManager.defaultSheetModel, 
                                        type));
                        for (NetworkGameObject go : game.getScene().getGameObjects())
                            server.sendToTCP(currentConnection.getID(),
                                    new InstantiateNetworkGameObjectResponse(go.getObjectID(),
                                            go.getClientID(),
                                            go.transform().position(),
                                            go.getPrefabID()));
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
        NetworkGameObject go = game.getScene().getGameObject(id);
        go.transform().position(position);
        go.transform().scale(scale);
        go.transform().rotation(rotation);
        go.transform().flipX(flipX);
        go.transform().flipY(flipY);
        
        NetworkGameObjectTransformUpdate p = new NetworkGameObjectTransformUpdate(id, position, scale, rotation, flipX, flipY);
        
        server.sendToAllUDP(p);
    }
    
    @Override
    public void onNetworkGameObjectRequest(Vector2 position, PrefabID pID)
    {
        NetworkGameObject go;
        go = new NetworkGameObject(ServerGame.getGameObjectID(), currentConnection.getID(), pID);
        game.getScene().addGameObject(go);
        server.sendToAllTCP(new InstantiateNetworkGameObjectResponse(go.getObjectID(), go.getClientID(), position, pID));
    }
    
}
