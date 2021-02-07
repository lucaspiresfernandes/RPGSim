package com.rpgsim.client;

import com.rpgsim.common.CommonConfigurations;
import com.rpgsim.common.Scene;
import com.rpgsim.common.game.Input;
import com.rpgsim.common.Screen;
import java.awt.Canvas;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientGame extends Canvas implements Runnable
{
    private static final int UPS = 60;
    
    private boolean gameRunning;
    private boolean networkRunning;
    private final Thread gameThread = new Thread(this, "Client Game");
    
    private final ClientManager client;
    
    private Screen screen;
    private Input input;
    
    private final Scene scene;
    
    public ClientGame(ClientManager manager, int width, int height)
    {
        super.setBounds(0, 0, width, height);
        super.setBackground(Color.BLACK);
        this.client = manager;
        this.scene = new Scene();
    }
    
    private void update(float dt)
    {
        scene.updateGameObjects(dt);
    }
    
    private void render()
    {
        screen.begin();
        
        screen.end();
    }
    
    public void start()
    {
        networkRunning = true;
        gameThread.start();
    }
    
    public void open()
    {
        input = new Input();
        this.addKeyListener(input);
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        createBufferStrategy(3);
        screen = new Screen(getBufferStrategy(), getWidth(), getHeight());
        gameRunning = true;
    }

    public void stop()
    {
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
        
        while (networkRunning)
        {
            long now = System.currentTimeMillis();
            
            float timePassed = (now - lastTime) * 0.001f;
            
            ndt += timePassed;
            
            while (ndt >= networkUpdateThreshold)
            {
                client.update();
                ndt -= networkUpdateThreshold;
            }
            
            if (gameRunning)
            {
                dt += timePassed;
                
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
            }
            
            lastTime = now;
        }
    }

    public Scene getScene()
    {
        return scene;
    }

}
