package com.rpgsim.common.game;

public class NetworkGameObject
{
    private final int id;
    private final Transform transform;
    private final Renderer renderer;

    public NetworkGameObject(int id)
    {
        this.id = id;
        transform = new Transform(this);
        renderer = new Renderer(this);
    }

    public int getID()
    {
        return id;
    }
    
    public Transform transform()
    {
        return transform;
    }

    public Renderer renderer()
    {
        return renderer;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 73 * hash + this.id;
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
        if (this.id != other.id)
        {
            return false;
        }
        return true;
    }
    
    public void update(float deltaTime) {}
}
