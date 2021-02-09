package com.rpgsim.client.sheet;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class SheetPanel extends JPanel
{
    private final String title;
    private final Font font;
    private final boolean border;

    public SheetPanel(String title, Font font, LayoutManager layout, boolean border)
    {
        super(layout, false);
        this.font = font;
        this.title = title;
        this.border = border;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        
        Dimension arc = new Dimension(10, 10);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(getBackground());
        graphics.fillRoundRect(0,  0, width - 1, height - 1, arc.width, arc.height);
        
        g.setColor(Color.WHITE);
        g.setFont(font);
        
        
        g.drawString(title, getWidth() / 2 - g.getFontMetrics(font).stringWidth(title) / 2, 30);
        
        g.setColor(Color.DARK_GRAY);
        g.drawLine(10, font.getSize() + 15, getWidth() - 10, font.getSize() + 15);
        
        if (border)
        {
            graphics.setStroke(new BasicStroke(1));
            graphics.setColor(getForeground());
            graphics.drawRoundRect(0,  0, width - 1, height - 1, arc.width, arc.height);
            graphics.setStroke(new BasicStroke());
        }
        
    }
}
