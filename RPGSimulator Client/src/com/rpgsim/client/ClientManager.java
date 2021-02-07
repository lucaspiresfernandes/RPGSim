package com.rpgsim.client;

import com.rpgsim.client.util.ClientConfigurations;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.rpgsim.common.Account;
import com.rpgsim.common.ClientActions;
import com.rpgsim.common.CommonConfigurations;
import com.rpgsim.common.ConnectionType;
import com.rpgsim.common.Vector2;
import com.rpgsim.common.clientpackages.ClientPackage;
import com.rpgsim.common.game.NetworkGameObject;
import com.rpgsim.common.serverpackages.ConnectionRequest;
import com.rpgsim.common.serverpackages.ServerPackage;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ClientManager extends Listener implements ClientActions
{
    private Connection connection;
    
    private final Account account;
    
    private final Client client;
    private final ClientConfigurations clientConfig;
    
    private final MainFrame mainFrame;
    private final GameFrame gameFrame;
    private final ClientGame game;
    
    private final ConcurrentLinkedQueue<ClientPackage> packages = new ConcurrentLinkedQueue<>();

    public ClientManager(MainFrame mainFrame, ClientConfigurations clientConfig, Account account)
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
        gameFrame = new GameFrame(this, l);
        game = gameFrame.getGame();
        
        this.clientConfig = clientConfig;
        
        client = new Client();
        client.getKryo().setRegistrationRequired(false);
    }
    
    public void sendPackage(ServerPackage _package)
    {
        client.sendTCP(_package);
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
        client.stop();
        client.close();
        game.stop();
        gameFrame.dispose();
    }
    
    public void start(ConnectionType type) throws IOException
    {
        client.addListener(this);
        client.start();
        client.connect(5000, clientConfig.getProperty("IP"), 
                clientConfig.getIntegerProperty("TCPPort"), 
                clientConfig.getIntegerProperty("UDPPort"));
        game.start();
        
        client.sendTCP(new ConnectionRequest(account.getUsername(), account.getPassword(), type));
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
    public void onLoginRequestResponse(boolean accepted, String message)
    {
        if (accepted)
        {
            mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
            gameFrame.setVisible(true);
            game.open();
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
            game.open();
            try
            {
                clientConfig.saveConfigurations();
            }
            catch (IOException ex)
            {
                Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, message);
            stop();
        }
    }
    
    @Override
    public void onInstantiateNetworkGameObject(int id, Vector2 position)
    {
        NetworkGameObject go = new NetworkGameObject(id);
        go.transform().position(position);
        game.getScene().addGameObject(go);
    }
    
}
