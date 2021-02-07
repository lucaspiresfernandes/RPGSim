package com.rpgsim.common;

import com.rpgsim.common.game.NetworkGameObject;
import java.util.Collection;
import java.util.HashMap;

public class Scene
{
    private final HashMap<Integer, NetworkGameObject> gameObjects = new HashMap<>();
    
    public void addGameObject(NetworkGameObject go)
    {
        gameObjects.put(go.getID(), go);
    }

    public void updateGameObjects(float dt) 
    {
        for (NetworkGameObject go : gameObjects.values())
            go.update(dt);
    }
    
    public NetworkGameObject getGameObject(int id)
    {
        return gameObjects.get(id);
    }

    public Collection<NetworkGameObject> getGameObjects()
    {
        return gameObjects.values();
    }
    
}
