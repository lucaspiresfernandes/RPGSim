package com.rpgsim.server;

import com.rpgsim.common.Scene;

public class ServerGame
{
    private static int validGameObjectID = 0;
    
    private final Scene scene;

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
    
}
