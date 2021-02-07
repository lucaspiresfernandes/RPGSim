package com.rpgsim.common.game;

import com.rpgsim.common.Vector2;

public class Transform
{
    private Vector2 position;
    private Vector2 scale;
    private float rotation = 0f;
    private boolean flipX = false, flipY = false;

    public Transform(NetworkGameObject gameObject)
    {
        this.position = new Vector2();
        this.scale = new Vector2(1f, 1f);
    }
    
    public Vector2 position()
    {
        return new Vector2(position);
    }
    
    public void position(Vector2 newPosition)
    {
        position = new Vector2(newPosition);
    }
    
    public Vector2 scale()
    {
        return new Vector2(scale);
    }
    
    public void scale(Vector2 newScale)
    {
        this.scale = new Vector2(newScale);
    }
    
    public float rotation()
    {
        return rotation;
    }
    
    public void rotation(float newRotation)
    {
        this.rotation = newRotation;
    }
    
    public boolean flipX()
    {
        return flipX;
    }
    
    public void flipX(boolean flipX)
    {
        this.flipX = flipX;
    }
    
    public void flipY(boolean flipY)
    {
        this.flipY = flipY;
    }

    public boolean flipY()
    {
        return flipY;
    }
    
}
