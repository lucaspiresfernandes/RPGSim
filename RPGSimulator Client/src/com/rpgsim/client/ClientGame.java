package com.rpgsim.client;

import com.rpgsim.common.Screen;
import com.rpgsim.common.CommonConfigurations;
import com.rpgsim.common.PrefabID;
import com.rpgsim.common.Scene;
import com.rpgsim.common.Vector2;
import com.rpgsim.common.game.Input;
import com.rpgsim.common.game.KeyCode;
import com.rpgsim.common.serverpackages.InstantiatePrefabRequest;
import com.rpgsim.common.sheets.PlayerSheet;
import com.rpgsim.common.sheets.SheetModel;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientGame extends Canvas implements Runnable
{
    private static final int UPS = 60;
    
    private boolean gameRunning;
    private boolean networkRunning;
    private final Thread gameThread = new Thread(this, "Client Game");
    
    private final ClientManager client;
    
    private static Screen screen;
    private Input input;
    
    private final Scene scene;
    
    private final ClientSheetFrame sheetFrame;
    private final ObjectFrame objectFrame;
    
    public ClientGame(ClientManager manager, int width, int height)
    {
        super.setBounds(0, 0, width, height);
        super.setBackground(Color.BLACK);
        
        this.client = manager;
        this.scene = new Scene();
        Physics.setObjects(this.scene.getGameObjects());
        
        this.sheetFrame = new ClientSheetFrame(client);
        this.objectFrame = new ObjectFrame(client);
        
        WindowAdapter l = new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                requestFocus();
            }
        };
        this.sheetFrame.addWindowListener(l);
        this.objectFrame.addWindowListener(l);
    }
    
    private void update(float dt)
    {
        if (Input.getKey(KeyCode.W) && screen.getTransform().position().y < 0)
            screen.getTransform().position(new Vector2(screen.getTransform().position().x, screen.getTransform().position().y + 10));
        else if (Input.getKey(KeyCode.S))
            screen.getTransform().position(new Vector2(screen.getTransform().position().x, screen.getTransform().position().y - 10));
        if (Input.getKey(KeyCode.A) && screen.getTransform().position().x < 0)
            screen.getTransform().position(new Vector2(screen.getTransform().position().x + 10, screen.getTransform().position().y));
        else if (Input.getKey(KeyCode.D))
            screen.getTransform().position(new Vector2(screen.getTransform().position().x - 10, screen.getTransform().position().y));
        
        double wheel = Input.mouseWheel() * 0.05f;
        Vector2 scale = screen.getTransform().scale();
        if (wheel != 0 && scale.x - wheel > 0)
            screen.getTransform().scale(new Vector2(scale.x - wheel, scale.y - wheel));
        
        if (Input.getKeyDown(KeyCode.NUM1))
            sheetFrame.setVisible(true);
        
        if (Input.getKeyDown(KeyCode.NUM2))
            objectFrame.open();
        
        scene.updateGameObjects(client.getAccount().getConnectionID(), dt);
    }
    
    private void render()
    {
        screen.begin();
        scene.renderBackground(screen);
        scene.renderGameObjects(client.getAccount().getConnectionID(), screen);
        screen.end();
    }
    
    public void start()
    {
        networkRunning = true;
        gameThread.start();
    }
    
    public void open(SheetModel model, PlayerSheet sheet)
    {
        createBufferStrategy(3);
        screen = new Screen(getBufferStrategy(), getWidth(), getHeight());
        
        input = new Input(screen.getTransform());
        addKeyListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);
        addMouseWheelListener(input);
        addFocusListener(input);
        
        this.sheetFrame.load(client.getAccount().getConnectionID(), model, sheet, true);
        
        client.sendPackage(new InstantiatePrefabRequest(Input.mousePosition(), PrefabID.MOUSE, client.getAccount().getConnectionID(), "data files\\assets\\cursor.png"));
        gameRunning = true;
        requestFocus();
    }

    public void stop()
    {
        sheetFrame.dispose();
        objectFrame.dispose();
        gameRunning = false;
        networkRunning = false;
    }

    @Override
    public void run()
    {
        long lastTime = System.currentTimeMillis();
        
        float gameUpdateThreshold = 1f / UPS;
        float networkUpdateThreshold = 1f / CommonConfigurations.networkUPS;
        
        float dt = 0f;
        float ndt = 0f;
        float timer = 0f;
        
        int frames = 0;
        
        while (networkRunning)
        {
            long now = System.currentTimeMillis();
            
            float timePassed = (now - lastTime) * 0.001f;
            
            ndt += timePassed;
            
            if (gameRunning)
            {
                dt += timePassed;
                timer += timePassed;
                
                boolean render = false;
                while (dt >= gameUpdateThreshold)
                {
                    render = true;
                    update(dt);
                    input.update();
                    dt -= gameUpdateThreshold;
                }

                if (render)
                {
                    render();
                    frames++;
                }
                else
                {
                    try
                    {
                        Thread.sleep(1);
                    }
                    catch (InterruptedException ex)
                    {
                        Logger.getLogger(ClientGame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                if (timer >= 1)
                {
                    System.out.println("FPS: " + frames);
                    timer = 0;
                    frames = 0;
                }
            }
            
            while (ndt >= networkUpdateThreshold)
            {
                client.update();
                ndt -= networkUpdateThreshold;
            }
            
            lastTime = now;
        }
    }

    public ClientSheetFrame getSheetFrame()
    {
        return sheetFrame;
    }

    public Scene getScene()
    {
        return scene;
    }

    public static Screen getScreen()
    {
        return screen;
    }

}
