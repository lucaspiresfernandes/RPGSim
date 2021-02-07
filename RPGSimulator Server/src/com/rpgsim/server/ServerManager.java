package com.rpgsim.server;

import com.rpgsim.server.util.ClientRequest;
import com.rpgsim.server.util.ServerConfigurations;
import com.rpgsim.server.util.AccountManager;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.rpgsim.common.Account;
import com.rpgsim.common.ConnectionType;
import com.rpgsim.common.ServerActions;
import com.rpgsim.common.Vector2;
import com.rpgsim.common.clientpackages.ConnectionRequestResponse;
import com.rpgsim.common.clientpackages.InstantiateNetworkGameObjectResponse;
import com.rpgsim.common.clientpackages.NetworkGameObjectPositionUpdate;
import com.rpgsim.common.game.NetworkGameObject;
import com.rpgsim.common.serverpackages.ServerPackage;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ServerManager extends Listener implements ServerActions
{
    //The server receives all client requests. Other classes are just utilities for the server.
    private final Server server;
    private final ServerConfigurations serverConfigurations;
    private final AccountManager accountManager;
    
    private final ServerGame game;
    
    private Connection currentConnection;
    //The window in which the server supervises client/user actions.
    private final ServerFrame serverFrame;
    
    //Thread-safe system to link Server and ServerManager system.
    //All requests are held by the queue until ServerManager process them.
    private final ConcurrentLinkedQueue<ClientRequest> clientRequests = new ConcurrentLinkedQueue<>();
    
    public ServerManager()
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
        
        this.serverConfigurations = new ServerConfigurations();
        this.accountManager = new AccountManager();
        
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
        accountManager.saveAccounts();
    }

    @Override
    public void onClientConnection(String username, String password, ConnectionType type)
    {
        Account acc = new Account(username, password);
        switch (type)
        {
            case LOGIN:
                if (accountManager.isAccountRegistered(acc))
                {
                    if (accountManager.isAccountActive(acc))
                    {
                        server.sendToTCP(currentConnection.getID(), 
                            new ConnectionRequestResponse(false, 
                                    "Account is already logged in.", 
                                    type));
                    }
                    else
                    {
                        accountManager.setAccountActive(currentConnection.getID(), acc, true);
                        server.sendToTCP(currentConnection.getID(), 
                                new ConnectionRequestResponse(true, "", 
                                        type));
                    }
                }
                else
                {
                    server.sendToTCP(currentConnection.getID(), 
                            new ConnectionRequestResponse(false, 
                                    "Username or password is incorrect.", 
                                    type));
                }
                break;
            case REGISTER:
                if (accountManager.isAccountRegistered(acc))
                {
                    server.sendToTCP(currentConnection.getID(), 
                            new ConnectionRequestResponse(false, 
                                    "Username already exists.", 
                                    type));
                }
                else
                {
                    accountManager.registerNewAccount(acc);
                    accountManager.setAccountActive(currentConnection.getID(), acc, true);
                    server.sendToTCP(currentConnection.getID(), 
                            new ConnectionRequestResponse(true, "", 
                                    type));
                }
                break;
        }
    }
    
    @Override
    public void onNetworkGameObjectPositionChanged(int id, Vector2 newPosition)
    {
        game.getScene().getGameObject(id).transform().position(newPosition);
        server.sendToAllUDP(new NetworkGameObjectPositionUpdate(id, newPosition));
    }

    @Override
    public void onNetworkGameObjectRequest(boolean clientAuthority)
    {
        NetworkGameObject go;
        if (clientAuthority)
            go = new NetworkGameObject(ServerGame.getGameObjectID());
        else
            go = new NetworkGameObject(ServerGame.getGameObjectID());
        game.getScene().addGameObject(go);
        
        server.sendToAllTCP(new InstantiateNetworkGameObjectResponse(go.getID(), go.transform().position()));
    }
    
    
}
