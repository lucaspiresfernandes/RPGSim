package com.rpgsim.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.rpgsim.common.Account;
import com.rpgsim.common.CommonConfigurations;
import com.rpgsim.common.ConnectionType;
import com.rpgsim.common.ServerActions;
import com.rpgsim.common.clientpackages.ConnectionRequestResponsePackage;
import com.rpgsim.common.serverpackages.ServerPackage;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ServerManager extends Listener implements Runnable, ServerActions
{
    //The server receives all client requests. Other classes are just utilities for the server.
    private final Server server;
    private final ServerConfigurations serverConfigurations;
    private final AccountManager accountManager;
    
    //The window in which the server supervises client/user actions.
    private final ServerFrame serverFrame;
    
    //Server Manager is responsible for processing the requests received by the server.
    private boolean running = false;
    private final Thread serverManagerThread = new Thread(this, "Server Manager");
    
    //Thread-safe system to link Server and ServerManager system.
    //All requests are held by the queue until ServerManager process them.
    private final ConcurrentLinkedQueue<ClientRequest> clientRequests = new ConcurrentLinkedQueue<>();
    private ClientRequest currentRequest;
    
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
        
        server = new Server();
        server.getKryo().setRegistrationRequired(false);
    }
    
    @Override
    public void received(Connection connection, Object object)
    {
        if (object instanceof ServerPackage)
            clientRequests.add(new ClientRequest(connection.getID(), (ServerPackage) object));
        else
            System.out.println("The data received is not a ServerPackage. Object: " + object);
    }

    @Override
    public void connected(Connection connection)
    {
        
    }
    
    @Override
    public void disconnected(Connection connection)
    {
        
    }
    
    private void update()
    {
        while ((currentRequest = clientRequests.poll()) != null)
        {
            currentRequest.getRequest().executeServerAction(this);
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
        running = true;
        serverManagerThread.start();
    }
    
    /**
     * Stops the server application and everything bound to it, including ServerFrame.
     */
    public void stop()
    {
        serverFrame.dispose();
        running = false;
        server.stop();
        server.close();
        accountManager.saveAccounts();
    }
    
    @Override
    public void run()
    {
        long lastTime = System.currentTimeMillis();
        
        float updateThreshold = 1f / CommonConfigurations.networkUPS;
        
        float dt = 0f;
        
        while (running)
        {
            long now = System.currentTimeMillis();
            
            dt += (now - lastTime) * 0.001f;
            
            while (dt >= updateThreshold)
            {
                update();
                dt -= updateThreshold;
            }
            
            lastTime = now;
        }
    }

    @Override
    public void clientLogin(String username, String password)
    {
        Account acc = new Account(username, password);
        if (accountManager.isAccountRegistered(acc))
        {
            if (accountManager.isAccountActive(acc))
            {
                server.sendToTCP(currentRequest.getConnectionID(), 
                    new ConnectionRequestResponsePackage(false, 
                            "Account is already logged in.", 
                            -1, ConnectionType.LOGIN));
            }
            else
            {
                int sessionID = AccountManager.nextSessionID();
                accountManager.setAccountActive(sessionID, acc, true);
                server.sendToTCP(currentRequest.getConnectionID(), 
                        new ConnectionRequestResponsePackage(true, "", 
                                sessionID, ConnectionType.LOGIN));
            }
        }
        else
        {
            server.sendToTCP(currentRequest.getConnectionID(), 
                    new ConnectionRequestResponsePackage(false, 
                            "Username or password is incorrect.", 
                            -1, ConnectionType.LOGIN));
        }
    }

    @Override
    public void clientRegister(String username, String password)
    {
        Account acc = new Account(username, password);
        if (accountManager.isAccountRegistered(acc))
        {
            server.sendToTCP(currentRequest.getConnectionID(), 
                    new ConnectionRequestResponsePackage(false, 
                            "Username already exists.", 
                            -1, ConnectionType.REGISTER));
        }
        else
        {
            accountManager.registerNewAccount(acc);
            int sessionID = AccountManager.nextSessionID();
            accountManager.setAccountActive(sessionID, acc, true);
            server.sendToTCP(currentRequest.getConnectionID(), 
                    new ConnectionRequestResponsePackage(true, "", 
                            sessionID, ConnectionType.REGISTER));
        }
    }

    @Override
    public void clientLogoff(int sessionID)
    {
        accountManager.setAccountActive(sessionID, accountManager.getActiveAccount(sessionID), false);
        server.sendToTCP(currentRequest.getConnectionID(), 
                new ConnectionRequestResponsePackage(true, "", sessionID, ConnectionType.LOGOFF));
    }
    
}
