package com.rpgsim.common;

import com.rpgsim.common.game.NetworkGameObject;
import java.awt.Image;
import java.util.Collection;
import java.util.HashMap;

public class Scene
{
    private Image background;
    private final HashMap<Integer, NetworkGameObject> gameObjects = new HashMap<>();
    
    public void addGameObject(NetworkGameObject go)
    {
        gameObjects.put(go.getObjectID(), go);
    }

    public void updateGameObjects(int clientID, float dt)
    {
        for (NetworkGameObject go : gameObjects.values())
            go.update(clientID, dt);
    }
    
    public void renderGameObjects(int clientID, Screen screen)
    {
        for (NetworkGameObject go : gameObjects.values())
            go.renderer().render(clientID, screen);
    }
    
    public void renderBackground(Screen screen)
    {
        if (background == null)
            return;
        screen.drawImage(background, 0, 0);
    }
    
    public NetworkGameObject getGameObject(int id)
    {
        return gameObjects.get(id);
    }
    
    public Collection<NetworkGameObject> getGameObjects()
    {
        return gameObjects.values();
    }

    public HashMap<Integer, NetworkGameObject> getGameObjectsHash()
    {
        return gameObjects;
    }

    public void removeGameObject(int id)
    {
        gameObjects.remove(id);
    }

    public Image getBackground()
    {
        return background;
    }

    public void setBackground(Image background)
    {
        this.background = background;
    }
    
}
