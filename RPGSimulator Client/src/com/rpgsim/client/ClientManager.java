package com.rpgsim.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.rpgsim.client.prefabs.DraggableObjectPrefab;
import com.rpgsim.client.prefabs.MousePrefab;
import com.rpgsim.common.ApplicationConfigurations;
import com.rpgsim.common.sheets.Account;
import com.rpgsim.common.ClientActions;
import com.rpgsim.common.ConnectionType;
import com.rpgsim.common.FileManager;
import com.rpgsim.common.PrefabID;
import com.rpgsim.common.Vector2;
import com.rpgsim.common.clientpackages.ClientPackage;
import com.rpgsim.common.game.NetworkGameObject;
import com.rpgsim.common.serverpackages.ConnectionRequest;
import com.rpgsim.common.serverpackages.NetworkGameObjectStateUptadeRequest;
import com.rpgsim.common.serverpackages.ServerPackage;
import com.rpgsim.common.serverpackages.UpdateType;
import com.rpgsim.common.sheets.SheetModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class ClientManager extends Listener implements ClientActions
{
    private Account account;
    
    private final Client client;
    private final ApplicationConfigurations clientConfig;
    
    private final MainFrame mainFrame;
    private final GameFrame gameFrame;
    private final ClientGame game;
    
    private final ConcurrentLinkedQueue<ClientPackage> receivedPackages = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<ServerPackage> packagesToSend = new ConcurrentLinkedQueue<>();

    public ClientManager(MainFrame mainFrame, ApplicationConfigurations clientConfig)
    {
        this.mainFrame = mainFrame;
        
        WindowAdapter l = new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent we)
            {
                int opt = JOptionPane.showConfirmDialog(null, "You really want to exit the simulator?", "Exit Game", JOptionPane.YES_NO_OPTION);
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
        return client.getID();
    }

    public Account getAccount()
    {
        return account;
    }
    
    public void sendPackage(ServerPackage _package)
    {
        packagesToSend.add(_package);
    }

    @Override
    public void received(Connection connection, Object object)
    {
        if (object instanceof ClientPackage)
            receivedPackages.add((ClientPackage) object);
        else
            System.out.println("The data received is not a ClientPackage. Object: " + object);
    }

    @Override
    public void disconnected(Connection connection)
    {
        game.stop();
        gameFrame.dispose();
    }
    
    public void start(String username, String password, ConnectionType type) throws IOException
    {
        client.addListener(this);
        client.start();
        client.connect(5000, clientConfig.getProperty("IP"), 
                clientConfig.getIntegerProperty("TCPPort"), 
                clientConfig.getIntegerProperty("UDPPort"));
        game.start();
        
        client.sendTCP(new ConnectionRequest(client.getID(), username, password, type));
    }
    
    public void stop()
    {
        if (client.isConnected())
        {
            client.stop();
            client.close();
        }
        else
        {
            disconnected(null);
        }
    }
    
    public void update()
    {
        ServerPackage sp;
        while ((sp = packagesToSend.poll()) != null)
            client.sendTCP(sp);
        
        for (NetworkGameObject go : game.getScene().getGameObjects())
        {
            if (go.isDirty())
            {
                NetworkGameObjectStateUptadeRequest r = new NetworkGameObjectStateUptadeRequest
                (go.getObjectID(), go.transform().position(), go.transform().scale(), 
                go.transform().rotation(), go.transform().flipX(), go.transform().flipY(), go.isDestroyed());
                client.sendUDP(r);
            }
        }
        
        ClientPackage cp;
        while ((cp = receivedPackages.poll()) != null)
            cp.executeClientAction(this);
    }

    @Override
    public void onConnectionRequestResponse(boolean accepted, String message, Account account, SheetModel model, ConnectionType type)
    {
        if (accepted)
        {
            this.account = account;
            mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
            gameFrame.setVisible(true);
            
            game.open(model, account.getPlayerSheet());
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
    public void onInstantiateNetworkGameObject(int id, int clientID, Vector2 position, PrefabID pID, String imageRelativePath)
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
        asyncLoadImage(go, imageRelativePath);
        game.getScene().addGameObject(go);
    }
    
    @Override
    public void updateNetworkGameObjectTransform(int id, Vector2 position, Vector2 scale, float rotation, boolean flipX, boolean flipY)
    {
        NetworkGameObject go = game.getScene().getGameObject(id);
        
        if (go.isDirty())
        {
            go.setDirty(false);
        }
        else
        {
            go.transform().position(position);
            go.transform().scale(scale);
            go.transform().rotation(rotation);
            go.transform().flipX(flipX);
            go.transform().flipY(flipY);
        }
    }

    @Override
    public void onNetworkGameObjectDestroy(int id)
    {
        game.getScene().removeGameObject(id);
    }

    @Override
    public void onNetworkGameObjectImageChange(int id, String relativePath)
    {
        asyncLoadImage(game.getScene().getGameObject(id), relativePath);
    }
    
    public static void asyncLoadImage(NetworkGameObject obj, String relativePath)
    {
        obj.renderer().setImage(new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB));
        AsynTask.executeAsyncTask("Async Image Load", () ->
        {
            try
            {
                obj.renderer().setImage(ImageIO.read(new File(FileManager.app_dir + relativePath)));
                obj.renderer().setImagePath(relativePath);
            }
            catch (IOException ex)
            {
                System.out.println("could not load " + FileManager.app_dir + relativePath);
                Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
    }

    @Override
    public void onCharacterSheetUpdate(int fieldID, int propertyID, Object value, UpdateType type)
    {
        //game.getSheetFrame().onReceiveCharacterSheetUpdate(fieldID, propertyID, value, type);
    }
    
}
