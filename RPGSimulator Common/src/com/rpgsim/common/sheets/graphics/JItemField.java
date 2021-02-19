package com.rpgsim.common.sheets.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JComponent;

public class JItemField extends JComponent
{
    private static int[] positions;
    private String[] item;
    private final JButton btnRemove;
    
    public JItemField(String[] item)
    {
        this.item = item;
        this.btnRemove = new JButton("-");
        this.btnRemove.setBackground(Color.BLACK);
        this.btnRemove.setForeground(Color.WHITE);
        this.btnRemove.setBounds(5, 1, 50, 15);
        this.btnRemove.setVerticalAlignment(JButton.CENTER);
        this.add(btnRemove);
        
    }

    public static void setPositions(int[] positions)
    {
        JItemField.positions = positions;
    }

    public static int[] getPositions()
    {
        return positions;
    }

    public JButton getRemoveButton()
    {
        return btnRemove;
    }

    public String[] getItem()
    {
        return item;
    }

    public void setItem(String[] equipment)
    {
        this.item = equipment;
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        RenderingHints h = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        ((Graphics2D)g).setRenderingHints(h);
        
        g.setFont(getFont());
        g.setColor(getForeground());
        
        for (int i = 0; i < item.length; i++)
            g.drawString(item[i], positions[i], g.getFont().getSize());
    }

}
