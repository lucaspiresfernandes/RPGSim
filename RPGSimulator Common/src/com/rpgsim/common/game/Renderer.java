package com.rpgsim.common.game;

import com.rpgsim.common.Screen;
import java.awt.Image;
import java.awt.geom.AffineTransform;

public class Renderer
{
    private final NetworkGameObject gameObject;
    private Image image;

    public Renderer(NetworkGameObject gameObject)
    {
        this.gameObject = gameObject;
        image = Screen.NULL_IMAGE;
    }

    public Image getImage()
    {
        return image;
    }

    public void setImage(Image image)
    {
        this.image = image;
    }
    
    public void render(Screen screen)
    {
        AffineTransform transform = gameObject.transform().getProcessedInstance();
        if (gameObject.transform().flipX())
        {
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-image.getWidth(null), 0);
            transform.concatenate(tx);
        }
        if (gameObject.transform().flipY())
        {
            AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
            tx.translate(0, -image.getHeight(null));
            transform.concatenate(tx);
        }
        screen.drawImage(image, transform);
    }
    
}
