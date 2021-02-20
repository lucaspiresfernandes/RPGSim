package com.rpgsim.server;

import com.rpgsim.common.Scene;

public class ServerGame
{
    private static int validGameObjectID = 0;
    
    private final Scene scene;
    private String backgroundPath;

    public ServerGame()
    {
        scene = new Scene();
    }

    public Scene getScene()
    {
        return scene;
    }
    
    public static int getGameObjectID()
    {
        return validGameObjectID++;
    }

    public String getBackgroundPath()
    {
        return backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath)
    {
        this.backgroundPath = backgroundPath;
    }
    
}
