package com.rpgsim.client;

import com.rpgsim.client.util.ClientConfigurations;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.rpgsim.client.prefabs.DraggableObjectPrefab;
import com.rpgsim.client.prefabs.MousePrefab;
import com.rpgsim.common.Account;
import com.rpgsim.common.ClientActions;
import com.rpgsim.common.ConnectionType;
import com.rpgsim.common.PrefabID;
import com.rpgsim.common.Vector2;
import com.rpgsim.common.clientpackages.ClientPackage;
import com.rpgsim.common.game.NetworkGameObject;
import com.rpgsim.common.serverpackages.ConnectionRequest;
import com.rpgsim.common.serverpackages.NetworkGameObjectPositionUptadeRequest;
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
    
    public int getConnectionID()
    {
        return connection.getID();
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
        for (NetworkGameObject go : game.getScene().getGameObjects())
        {
            if (go.isDirty())
            {
                NetworkGameObjectPositionUptadeRequest r = new NetworkGameObjectPositionUptadeRequest
                (go.getObjectID(), go.transform().position(), go.transform().scale(), 
                go.transform().rotation(), go.transform().flipX(), go.transform().flipY());
                client.sendUDP(r);
                go.setDirty(false);
            }
        }
        
        ClientPackage p;
        while ((p = packages.poll()) != null)
        {
            p.executeClientAction(this);
        }
    }

    @Override
    public void onConnectionRequestResponse(boolean accepted, String message, ConnectionType type)
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
    public void onInstantiateNetworkGameObject(int id, int clientID, Vector2 position, PrefabID pID)
    {
        NetworkGameObject go;
        switch (pID)
        {
            case MOUSE:
                go = new MousePrefab(id, clientID, pID);
                break;
            case DRAGGABLE_OBJECT:
                go = new DraggableObjectPrefab(id, clientID, pID);
                break;
            default:
                go = new NetworkGameObject(id, clientID, pID);
                break;
        }
        game.getScene().addGameObject(go);
    }
    
    @Override
    public void updateNetworkGameObjectTransform(int id, Vector2 position, Vector2 scale, float rotation, boolean flipX, boolean flipY)
    {
        NetworkGameObject go = game.getScene().getGameObject(id);
        go.transform().position(position);
        go.transform().scale(scale);
        go.transform().rotation(rotation);
        go.transform().flipX(flipX);
        go.transform().flipY(flipY);
    }

    @Override
    public void onNetworkGameObjectDestroy(int id)
    {
        game.getScene().removeGameObject(id);
    }
    
}
