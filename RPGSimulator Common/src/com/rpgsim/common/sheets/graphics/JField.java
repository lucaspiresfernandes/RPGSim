package com.rpgsim.common.sheets.graphics;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JComponent;

public class JField extends JComponent
{
    private int[] positions;
    private String[] field;
    private final JButton btnRemove;

    public JField(String[] equipment)
    {
        this.field = equipment;
        this.btnRemove = new JButton("-");
        this.btnRemove.setBackground(Color.BLACK);
        this.btnRemove.setForeground(Color.WHITE);
        this.btnRemove.setBounds(5, 1, 50, 15);
        this.btnRemove.setVerticalAlignment(JButton.CENTER);
        this.add(btnRemove);
        
    }

    public int[] getPositions()
    {
        return positions;
    }

    public void setPositions(int[] positions)
    {
        this.positions = positions;
    }
    
    public JButton getRemoveButton()
    {
        return btnRemove;
    }

    public String[] getField()
    {
        return field;
    }

    public void setField(String[] field)
    {
        this.field = field;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        RenderingHints h = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        ((Graphics2D)g).setRenderingHints(h);
        
        g.setFont(getFont());
        g.setColor(getForeground());
        
        for (int i = 0; i < field.length; i++)
        {
            g.drawString(field[i], positions[i], g.getFont().getSize());
        }
    }
    
}
