package com.rpgsim.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.rpgsim.common.Account;
import com.rpgsim.common.ClientActions;
import com.rpgsim.common.CommonConfigurations;
import com.rpgsim.common.ConnectionType;
import com.rpgsim.common.clientpackages.ClientPackage;
import com.rpgsim.common.serverpackages.ConnectionRequestPackage;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ClientManager extends Listener implements Runnable, ClientActions
{
    private Connection connection;
    
    private final Thread clientManagerThread = new Thread(this, "Client Manager");
    private boolean running;
    
    private final Account account;
    
    private final Client client;
    private final ClientConfigurations clientConfig;
    
    private final MainFrame mainFrame;
    private final GameFrame gameFrame;
    private final Game game;
    
    private final ConcurrentLinkedQueue<ClientPackage> packages = new ConcurrentLinkedQueue<>();

    public ClientManager(MainFrame mainFrame, Account account) throws IOException
    {
        this.mainFrame = mainFrame;
        
        this.account = account;
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
        gameFrame = new GameFrame(l);
        game = gameFrame.getGame();
        
        clientConfig = new ClientConfigurations();
        
        client = new Client();
        client.getKryo().setRegistrationRequired(false);
    }

    @Override
    public void received(Connection connection, Object object)
    {
        if (object instanceof ClientPackage)
            packages.add((ClientPackage) object);
        else
            System.out.println("The data received is not a ClientPackage. Object: " + object);
    }

    @Override
    public void connected(Connection connection)
    {
        this.connection = connection;
    }

    @Override
    public void disconnected(Connection connection)
    {
        System.out.println("disconnected");
        client.stop();
        client.close();
        running = false;
        game.stop();
        gameFrame.dispose();
    }
    
    public void start(ConnectionType type) throws IOException
    {
        running = true;
        clientManagerThread.start();
        
        client.addListener(this);
        client.start();
        client.connect(5000, clientConfig.getProperty("IP"), 
                clientConfig.getIntegerProperty("TCPPort"), 
                clientConfig.getIntegerProperty("UDPPort"));
        
        client.sendTCP(new ConnectionRequestPackage(account.getUsername(), account.getPassword(), type));
    }
    
    public void stop()
    {
        connection.close();
    }
    
    public void update()
    {
        ClientPackage p;
        while ((p = packages.poll()) != null)
        {
            p.executeClientAction(this);
        }
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
            
            boolean updated = false;
            while (dt >= updateThreshold)
            {
                updated = true;
                update();
                dt -= updateThreshold;
            }
            
            if (updated)
            {
                try
                {
                    Thread.sleep(1);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            lastTime = now;
        }
    }

    @Override
    public void onLoginRequestResponse(boolean accepted, String message)
    {
        if (accepted)
        {
            mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
            gameFrame.setVisible(true);
            game.start();
        }
        else
        {
            JOptionPane.showMessageDialog(null, message);
            stop();
        }
    }

    @Override
    public void onRegisterRequestResponse(boolean accepted, String message)
    {
        if (accepted)
        {
            mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
            gameFrame.setVisible(true);
            game.start();
        }
        else
        {
            JOptionPane.showMessageDialog(null, message);
            stop();
        }
    }
    
}
