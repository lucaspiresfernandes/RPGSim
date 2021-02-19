package com.rpgsim.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;

public class Screen
{
    private final BufferStrategy bs;
    private final int screenWidth, screenHeight;
    private Graphics2D g;
    private AffineTransform transform = new AffineTransform();
    
    
    public Screen(BufferStrategy bs, int screenWidth, int screenHeight)
    {
        this.bs = bs;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }
    
    public void begin()
    {
        g = (Graphics2D) bs.getDrawGraphics();
        g.setTransform(transform);
        g.setColor(Color.BLACK);
        int x = (int) transform.getTranslateX();
        int y = (int) transform.getTranslateY();
        g.fillRect(-x, -y, screenWidth - x, screenHeight - y);
    }
    
    public void end()
    {
        g.dispose();
        bs.show();
    }

    public boolean drawImage(Image img, int x, int y)
    {
        return g.drawImage(img, x, y, null);
    }
    
    public boolean drawImage(Image img, AffineTransform xform)
    {
        return g.drawImage(img, xform, null);
    }

    public void drawString(String str, Vector2 position)
    {
        g.drawString(str, position.x, position.y);
    }

    public Color getColor()
    {
        return g.getColor();
    }

    public void setColor(Color c) 
    {
        g.setColor(c);
    }

    public Font getFont()
    {
        return g.getFont();
    }

    public void setFont(Font font)
    {
        g.setFont(font);
    }
    
    public void drawLine(Vector2 pos1, Vector2 pos2)
    {
        g.drawLine((int) pos1.x, (int) pos1.y, (int) pos2.x, (int) pos2.y);
    }

    public void fillRect(int x, int y, int width, int height)
    {
        g.fillRect(x, y, width, height);
    }

    public void drawRect(int x, int y, int width, int height)
    {
        g.drawRect(x, y, width, height);
    }

    public AffineTransform getTransform()
    {
        return transform;
    }

    public void setTransform(AffineTransform transform)
    {
        this.transform = transform;
    }
    
}
