package com.rpgsim.common.sheets.graphics;


import java.awt.LayoutManager;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JField extends JPanel
{
    private JLabel[] properties;

    public JField(LayoutManager layout, boolean isDoubleBuffered)
    {
        super(layout, isDoubleBuffered);
    }

    public JField(LayoutManager layout)
    {
        super(layout);
    }

    public JField(boolean isDoubleBuffered)
    {
        super(isDoubleBuffered);
    }

    public JLabel[] getProperties()
    {
        return properties;
    }

    public void setProperties(JLabel[] properties)
    {
        this.properties = properties;
    }
    
}
