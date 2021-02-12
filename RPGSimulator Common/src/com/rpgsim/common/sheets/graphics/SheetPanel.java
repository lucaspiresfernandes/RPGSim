package com.rpgsim.common.sheets.graphics;



import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class SheetPanel extends JPanel
{
    private final String title;
    private final Font titleFont;
    private final boolean border;
    
    private Color borderColor;
    private Color titleColor;
    
    public SheetPanel(String title, Font titleFont, boolean border)
    {
        this.titleFont = titleFont;
        this.title = title;
        this.border = border;
        this.titleColor = Color.WHITE;
        this.borderColor = Color.DARK_GRAY;
    }

    public Color getBorderColor()
    {
        return borderColor;
    }

    public void setBorderColor(Color borderColor)
    {
        this.borderColor = borderColor;
    }

    public Color getTitleColor()
    {
        return titleColor;
    }

    public void setTitleColor(Color titleColor)
    {
        this.titleColor = titleColor;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        int width = getWidth();
        int height = getHeight();
        Graphics2D g2d = (Graphics2D) g;
        
        Dimension arc = new Dimension(10, 10);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0,  0, width - 1, height - 1, arc.width, arc.height);
        
        g.setColor(titleColor);
        g.setFont(titleFont);
        
        g.drawString(title, getWidth() / 2 - g.getFontMetrics(titleFont).stringWidth(title) / 2, 30);
        
        g.setColor(borderColor);
        g.drawLine(10, titleFont.getSize() + 15, getWidth() - 10, titleFont.getSize() + 15);
        
        if (border)
        {
            g2d.setColor(borderColor);
            g2d.setStroke(new BasicStroke(1));
            g2d.setColor(getForeground());
            g2d.drawRoundRect(0,  0, width - 1, height - 1, arc.width, arc.height);
            g2d.setStroke(new BasicStroke());
        }
    }
    
}
