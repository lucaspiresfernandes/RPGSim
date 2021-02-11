package com.rpgsim.common.sheets.graphics;

import com.rpgsim.common.serverpackages.UpdateType;
import com.rpgsim.common.sheets.Equipment;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JButton;

public abstract class EquipmentPanel extends SheetPanel
{
    private final Font f16;
    private final Font f12;
    private ArrayList<Equipment> equipments;
    
    private final ArrayList<JButton> btnRemoveEquipment;
    private final JButton addEquipment;
    
    public EquipmentPanel(String title, Font font, LayoutManager layout, boolean border, ArrayList<Equipment> equipments)
    {
        super(title, font, layout, border);
        
        this.f16 = font.deriveFont(16f);
        this.f12 = this.f16.deriveFont(12f);
        this.equipments = equipments;
        this.btnRemoveEquipment = new ArrayList<>(this.equipments.size());
        
        int y = f16.getSize() + 75;
        for (int i = 0; i < this.equipments.size(); i++)
        {
            this.btnRemoveEquipment.add(new JButton("-"));
            JButton btn = this.btnRemoveEquipment.get(i);
            int ii = i;
            btn.addActionListener(l -> 
            {
                buttonRemoveEquipment(ii);
            });
            btn.setBackground(Color.BLACK);
            btn.setForeground(Color.WHITE);
            btn.setBounds(0, y - 10, 40, 15);
            y += 10;
            super.add(btn);
        }
        
        this.addEquipment = new JButton("+");
        this.addEquipment.setBackground(Color.BLACK);
        this.addEquipment.setForeground(Color.WHITE);
        this.addEquipment.setBounds(10, 15, 50, 15);
        super.add(this.addEquipment);
    }

    @Override
    public void updateSheet(int propertyID, Object newValue, UpdateType type)
    {
        Equipment eq = (Equipment) newValue;
        switch (type)
        {
            case ADD:
                addNewEquipment(eq);
                break;
            case REMOVE:
                removeEquipment(propertyID);
                break;
            case UPDATE:
                Equipment oldEq = equipments.get(propertyID);
                oldEq.set(eq);
                break;
            default:
                throw new AssertionError();
        }
    }

    public ArrayList<Equipment> getEquipments()
    {
        return equipments;
    }

    public void setEquipments(ArrayList<Equipment> equipments)
    {
        this.equipments = equipments;
    }

    public ArrayList<JButton> getBtnRemoveEquipment()
    {
        return btnRemoveEquipment;
    }

    public JButton getAddEquipmentButton()
    {
        return addEquipment;
    }
    
    public void addNewEquipment(Equipment e)
    {
        equipments.add(e);
        JButton btnRemove = new JButton("-");
        btnRemove.addActionListener(l ->
        {
            buttonRemoveEquipment(equipments.indexOf(e));
        });
        JButton last = btnRemoveEquipment.get(btnRemoveEquipment.size() - 1);
        btnRemove.setBounds(0, last.getY() + last.getHeight() + 10, last.getWidth(), last.getHeight());
        btnRemoveEquipment.add(btnRemove);
        this.add(btnRemove);
    }
    
    public void removeEquipment(int index)
    {
        equipments.remove(index);
        JButton btn = btnRemoveEquipment.remove(index);
        this.remove(btn);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        ((Graphics2D)g).setRenderingHints(rh);
        g.setColor(Color.WHITE);
        g.setFont(f16);
        
        int x = 50;
        int y = 50;
        String[] desc = Equipment.getPropertiesDescriptions();
        
        int[] pos = new int[desc.length];
        
        for (int i = 0; i < desc.length; i++)
        {
            String s = desc[i];
            
            g.drawString(s, x, y + f16.getSize());
            
            pos[i] = x;
            
            x += g.getFontMetrics(f16).stringWidth(s) + 60;
        }
        
        y += f16.getSize() + 25;
        
        g.setFont(f12);
        
        for (int i = 0; i < equipments.size(); i++)
        {
            Equipment e = equipments.get(i);
            
            String[] prop = Equipment.getProperties(e);
            
            for (int j = 0; j < prop.length; j++)
            {
                x = pos[j];
                String s = prop[j];
                
                g.drawString(s, x, y);
            }
            
            y += f12.getSize() + 10;
        }
    }
    
    public abstract void buttonRemoveEquipment(int propertyID);
}
