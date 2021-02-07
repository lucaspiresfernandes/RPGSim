package com.rpgsim.common.game;

import com.rpgsim.common.Vector2;
import java.awt.Image;
import java.awt.geom.AffineTransform;

public class Transform
{
    private final NetworkGameObject gameObject;
    private AffineTransform transform;
    private float rotation = 0f;
    private boolean flipX = false, flipY = false;

    public Transform(NetworkGameObject gameObject)
    {
        this.gameObject = gameObject;
    }
    
    public Vector2 position()
    {
        return new Vector2(transform.getTranslateX(), transform.getTranslateY());
    }
    
    public void position(Vector2 newPosition)
    {
        transform.setToTranslation(newPosition.x, newPosition.y);
    }
    
    public Vector2 scale()
    {
        return new Vector2(transform.getScaleX(), transform.getScaleY());
    }
    
    public void scale(Vector2 newScale)
    {
        transform.setToScale(newScale.x, newScale.y);
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
    
    public AffineTransform getProcessedInstance()
    {
        AffineTransform t = new AffineTransform(transform);
        Image img = gameObject.renderer().getImage();
        t.rotate(Math.toRadians(rotation), img.getWidth(null) * 0.5f, img.getHeight(null) * 0.5f);
        return t;
    }
    
}
