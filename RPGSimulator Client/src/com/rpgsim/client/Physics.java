package com.rpgsim.client;

import com.rpgsim.common.game.NetworkGameObject;
import java.awt.Rectangle;
import java.util.Collection;

public class Physics
{
    private static Collection<NetworkGameObject> objects;

    public Physics(Collection<NetworkGameObject> objects)
    {
        Physics.objects = objects;
    }
    
    public static NetworkGameObject getCollision(NetworkGameObject orig)
    {
        NetworkGameObject obj = null;
        Rectangle origBounds = orig.getBounds();
        for (NetworkGameObject go : objects)
        {
            if (go.equals(orig))
                continue;
            
            if (go.getBounds().intersects(origBounds))
            {
                obj = go;
                break;
            }
        }
        return obj;
    }
    
    
}
