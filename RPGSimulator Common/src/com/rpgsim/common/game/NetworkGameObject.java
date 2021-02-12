package com.rpgsim.common.game;

import com.rpgsim.common.PrefabID;
import com.rpgsim.common.Vector2;
import java.awt.Rectangle;

public class NetworkGameObject
{
    private final int objectID;
    private final int clientID;
    private final PrefabID prefabID;
    private final Transform transform;
    private final Renderer renderer;
    
    private boolean dirty = false;
    private boolean destroyed = false;
    
    private Vector2 smoothPosition;

    public NetworkGameObject(int id, int clientID, PrefabID prefabID)
    {
        this.objectID = id;
        this.clientID = clientID;
        this.prefabID = prefabID;
        this.smoothPosition = new Vector2();
        transform = new Transform();
        renderer = new Renderer(this);
    }

    public int getObjectID()
    {
        return objectID;
    }

    public int getClientID()
    {
        return clientID;
    }
    
    public boolean hasClientID()
    {
        return clientID > 0;
    }

    public PrefabID getPrefabID()
    {
        return prefabID;
    }

    public boolean isDirty()
    {
        return dirty;
    }

    public void setDirty(boolean dirty)
    {
        this.dirty = dirty;
    }

    public boolean isDestroyed()
    {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed)
    {
        this.destroyed = destroyed;
    }
    
    public Transform transform()
    {
        return transform;
    }

    public Renderer renderer()
    {
        return renderer;
    }

    public Vector2 getSmoothPosition()
    {
        return smoothPosition;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 73 * hash + this.objectID;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final NetworkGameObject other = (NetworkGameObject) obj;
        if (this.objectID != other.objectID)
        {
            return false;
        }
        return true;
    }
    
    public void update(int clientID, float dt)
    {
        smoothPosition = Vector2.lerp(smoothPosition, transform.position(), 10 * dt);
    }
    
    public Rectangle getBounds()
    {
        return new Rectangle((int)transform.position().x, (int)transform.position().y, 
                (int) (renderer.getImage().getWidth(null) * transform.scale().x), 
                (int) (renderer.getImage().getHeight(null) * transform.scale().y));
    }
    
}
