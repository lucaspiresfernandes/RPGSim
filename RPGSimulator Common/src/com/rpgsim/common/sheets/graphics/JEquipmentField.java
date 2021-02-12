package com.rpgsim.common.sheets.graphics;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class JEquipmentField extends JComponent
{
    private static int[] positions;
    private final Object[] equipment;
    private final JButton btnRemove;

    public JEquipmentField(Object[] equipment)
    {
        this.equipment = equipment;
        this.btnRemove = new JButton("-");
        this.btnRemove.setBackground(Color.BLACK);
        this.btnRemove.setForeground(Color.WHITE);
        this.btnRemove.setBounds(5, 1, 50, 15);
        this.btnRemove.setVerticalAlignment(JButton.CENTER);
        
        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                if (e.getClickCount() < 2)
                    return;
                int selected = -1;
                for (int i = 0; i < positions.length; i++)
                {
                    int x = e.getX();
                    if (i < positions.length - 1)
                    {
                        if (x > positions[i] && x < positions[i + 1])
                        {
                            selected = i;
                        }
                    }
                    else if (x > positions[i])
                    {
                        selected = i;
                    }
                }
                
                if (selected < 0)
                    return;
                
                String a = JOptionPane.showInputDialog("Please type in new value.", equipment[selected]);
                
                if (a == null)
                    return;
                
                equipment[selected] = a;
                repaint();
            }
        });
        
        this.add(btnRemove);
    }

    public static void setPositions(int[] positions)
    {
        JEquipmentField.positions = positions;
    }

    public static int[] getPositions()
    {
        return positions;
    }

    public JButton getRemoveButton()
    {
        return btnRemove;
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        RenderingHints h = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        ((Graphics2D)g).setRenderingHints(h);
        
        g.setFont(getFont());
        g.setColor(getForeground());
        
        for (int i = 0; i < equipment.length; i++)
        {
            g.drawString(equipment[i].toString(), positions[i], g.getFont().getSize());
        }
    }
    
}
