package com.rpgsim.client;

import java.awt.Canvas;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends Canvas implements Runnable
{
    private static final int UPS = 60;
    
    private boolean running;
    private final Thread gameThread = new Thread(this, "Game");
    
    
    public Game(int width, int height)
    {
        super.setBounds(0, 0, width, height);
        super.setBackground(Color.BLACK);
    }
    
    private void update(float dt)
    {
        
    }
    
    private void render()
    {
        
    }
    
    public void start()
    {
        running = true;
        gameThread.start();
    }

    public void stop()
    {
        running = false;
    }

    @Override
    public void run()
    {
        long lastTime = System.currentTimeMillis();
        
        float updateThreshold = 1f / UPS;
        
        float dt = 0f;
        
        while (running)
        {
            long now = System.currentTimeMillis();
            
            dt += (now - lastTime) * 0.001f;
            
            boolean render = false;
            while (dt >= updateThreshold)
            {
                render = true;
                update(dt);
                dt -= updateThreshold;
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
                    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            lastTime = now;
        }
    }

}
