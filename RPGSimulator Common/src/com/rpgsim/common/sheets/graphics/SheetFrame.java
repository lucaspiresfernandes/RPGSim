package com.rpgsim.common.sheets.graphics;

import com.rpgsim.common.sheets.PlayerSheet;
import com.rpgsim.common.sheets.SheetModel;
import java.awt.Color;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SheetFrame extends javax.swing.JFrame
{
    private final int connectionID;
    private SheetModel model;
    private PlayerSheet sheet;
    
    private final Font f = new Font("Comic Sans MS", Font.PLAIN, 16);
    private final Font f12 = f.deriveFont(12f);
    private final Font f24 = f.deriveFont(24f);
    
    private final SheetScrollPane scrMain;
    private final JPanel pnlMain;
    private SheetScrollPane scrSkills, scrEquipments, scrItems;
    private SheetPanel pnlInfo, pnlStats, pnlAbout, pnlExtras, pnlAttributes, pnlSkills, pnlEquipments, pnlItems;
    private JPanel pnlEquipmentFields;
    
    private JTextField[] txtInfo;
    private JTextArea txaAbout, txaExtras;
    private JTextField[] txtAttributes;
    private JTextField[] txtSkills;
    private final ArrayList<JEquipmentField> equipments = new ArrayList<>();
    private final JButton btnAddEquipment = new JButton("+");
    private final ArrayList<JLabel> items = new ArrayList<>();
    private final JButton btnAddItem = new JButton("+");
    
    private boolean loaded = false;
    
    public SheetFrame(int connectionID)
    {
        initComponents();
        this.connectionID = connectionID;
        
        pnlMain = new JPanel(null, false);
        scrMain = new SheetScrollPane(pnlMain);
        scrMain.setBounds(0, 0, getWidth(), getHeight());
        getContentPane().add(scrMain);
    }
    
    public void load(SheetModel model, PlayerSheet sheet)
    {
        this.model = model;
        this.sheet = sheet;
        
        txtInfo = new JTextField[sheet.getInfo().length];
        txtAttributes = new JTextField[sheet.getAttributes().length];
        txtSkills = new JTextField[sheet.getSkills().length];
        
        initInfoPanel();
        initStatsPanel();
        initAboutPanel();
        initExtrasPanel();
        initAttributesPanel();
        initSkillsPanel();
        initEquipmentsPanel();
        initItemsPanel();
        
        pnlMain.setPreferredSize(new Dimension(getWidth(), scrItems.getY() + scrItems.getHeight() + 50));
        pnlMain.setBackground(Color.BLACK);
        scrMain.getVerticalScrollBar().setUnitIncrement(40);
        
        loaded = true;
    }
    
    public void reload(PlayerSheet sheet)
    {
        this.sheet = sheet;
        System.out.println("reloading...");
    }

    public boolean isLoaded()
    {
        return loaded;
    }
    
    private void initInfoPanel()
    {
        int x = 100;
        int y = 10;
        int w = getWidth() / 2 - x;
        
        int lines = sheet.getInfo().length;
        int gap = 40;
        
        int txtGap = 10;
        
        int h = lines * f.getSize() + lines * txtGap + lines * gap;
        
        pnlInfo = new SheetPanel("Character Information", f24, true);
        pnlInfo.setLayout(null);
        pnlInfo.setBounds(x, y, w, h);
        pnlInfo.setBackground(Color.BLACK);
        
        x = 15;
        y = 55;
        w = pnlInfo.getWidth() - x -  20;
        
        JLabel[] lblInfo = new JLabel[model.getInfo().length];
        
        for (int i = 0; i < txtInfo.length; i++)
        {
            lblInfo[i] = new JLabel(model.getInfo()[i]);
            lblInfo[i].setFont(f);
            lblInfo[i].setBounds(x, y, getStringWidth(lblInfo[i].getFont(), lblInfo[i].getText()), lblInfo[i].getFont().getSize());
            lblInfo[i].setForeground(Color.WHITE);
            
            txtInfo[i] = new JTextField(sheet.getInfo()[i]);
            txtInfo[i].setBounds(x, y, w, h);
            txtInfo[i].setBackground(Color.BLACK);
            txtInfo[i].setForeground(Color.WHITE);
            txtInfo[i].setBorder(new MatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
            txtInfo[i].setBounds(x, y + lblInfo[i].getHeight() + txtGap, pnlInfo.getWidth() - x - 30, 20);
            
            pnlInfo.add(lblInfo[i]);
            pnlInfo.add(txtInfo[i]);
            
            y += lblInfo[i].getHeight() + gap;
        }
        pnlMain.add(pnlInfo);
    }
    
    private void initStatsPanel()
    {
        int x = pnlInfo.getX() + pnlInfo.getWidth()+ 20;
        int y = 10;
        int w = getWidth() / 2 - x;
        int h = pnlInfo.getHeight();
        
        pnlStats = new SheetPanel("Stats", f24, true);
        pnlStats.setLayout(null);
        pnlStats.setBounds(x, y, w, h);
        pnlStats.setBackground(Color.BLACK);
        pnlMain.add(pnlStats);
        
        pnlStats.setBounds(x, y, getWidth() / 2 - 100, h);
    }
    
    private void initAboutPanel()
    {
        int x = pnlInfo.getX();
        int y = pnlInfo.getY() + pnlInfo.getHeight() + 20;
        int w = pnlInfo.getWidth();
        int txtH = 250;
        int h = txtH + 60;
        
        pnlAbout = new SheetPanel("About", f24, true);
        pnlAbout.setLayout(null);
        pnlAbout.setBounds(x, y, w, h);
        pnlAbout.setBackground(Color.BLACK);
        pnlMain.add(pnlAbout);
        
        txaAbout = new JTextArea()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                if (this.getText().isEmpty())
                {
                    g.setColor(Color.GRAY);
                    g.setFont(f);
                    g.drawString("Write about your character's likes, dislikes, dreams, etc.", 
                        getInsets().left, g.getFontMetrics().getMaxAscent() + getInsets().top);
                }
            }
        };
        
        txaAbout.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                if (txaAbout.getText().length() <= 1)
                {
                    txaAbout.repaint();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                if (txaAbout.getText().length() <= 1)
                {
                    txaAbout.repaint();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                if (txaAbout.getText().length() <= 1)
                {
                    txaAbout.repaint();
                }
            }
        });
        
        txaAbout.setBackground(Color.DARK_GRAY);
        txaAbout.setForeground(Color.WHITE);
        txaAbout.setFont(f);
        txaAbout.setBounds(10, 50, pnlAbout.getWidth() - 20, txtH);
        txaAbout.setLineWrap(true);
        pnlAbout.add(txaAbout);
    }
    
    private void initExtrasPanel()
    {
        int x = pnlStats.getX();
        int y = pnlStats.getY() + pnlStats.getHeight() + 20;
        int w = pnlStats.getWidth();
        int txtH = 250;
        int h = txtH + 60;
        
        pnlExtras = new SheetPanel("Extras", f24, true);
        pnlExtras.setLayout(null);
        pnlExtras.setBounds(x, y, w, h);
        pnlExtras.setBackground(Color.BLACK);
        pnlMain.add(pnlExtras);
        
        txaExtras = new JTextArea()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                if (this.getText().isEmpty())
                {
                    g.setColor(Color.GRAY);
                    g.setFont(f);
                    g.drawString("You can write anything you want here. (Notes, extra informations...)", 
                        getInsets().left, g.getFontMetrics().getMaxAscent() + getInsets().top);
                }
            }
        };
        
        txaExtras.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                if (txaExtras.getText().length() <= 1)
                {
                    txaExtras.repaint();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                if (txaExtras.getText().length() <= 1)
                {
                    txaExtras.repaint();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                if (txaExtras.getText().length() <= 1)
                {
                    txaExtras.repaint();
                }
            }
        });
        
        txaExtras.setBackground(Color.DARK_GRAY);
        txaExtras.setForeground(Color.WHITE);
        txaExtras.setFont(f);
        txaExtras.setBounds(10, 50, pnlExtras.getWidth() - 20, txtH);
        txaExtras.setLineWrap(true);
        pnlExtras.add(txaExtras);
    }
    
    private void initAttributesPanel()
    {
        int x = pnlAbout.getX();
        int y = pnlAbout.getY() + pnlAbout.getHeight() + 20;
        int w = pnlExtras.getX() + pnlExtras.getWidth() - x;
        
        int pnlW = 100;
        int pnlH = 75;
        int hGap = 25;
        int vGap = 50;
        int numRow = (int) Math.ceil(model.getAttributes().length / (w / (pnlW + hGap)));
        
        int h = numRow * pnlH + numRow * vGap + 20;
        
        pnlAttributes = new SheetPanel("Attributes", f24, true);
        pnlAttributes.setLayout(new FlowLayout(FlowLayout.CENTER, hGap, vGap));
        pnlAttributes.setBounds(x, y, w, h);
        pnlAttributes.setBackground(Color.BLACK);
        pnlMain.add(pnlAttributes);
        
        JLabel[] lblAttributes = new JLabel[model.getAttributes().length];
        for (int i = 0; i < lblAttributes.length; i++)
        {
            lblAttributes[i] = new JLabel(model.getAttributes()[i]);
            txtAttributes[i] = new JTextField(sheet.getAttributes()[i] + "");
            
            JPanel miniAttr = new JPanel(null, false);
            miniAttr.setLayout(new BoxLayout(miniAttr, BoxLayout.PAGE_AXIS));
            miniAttr.setPreferredSize(new Dimension(pnlW, pnlH));
            miniAttr.setBackground(Color.BLACK);
            
            lblAttributes[i].setFont(f12);
            lblAttributes[i].setBounds(0, 0, getStringWidth(lblAttributes[i].getFont(), lblAttributes[i].getText()), lblAttributes[i].getFont().getSize());
            lblAttributes[i].setForeground(Color.WHITE);
            lblAttributes[i].setAlignmentX(CENTER_ALIGNMENT);
            
            txtAttributes[i].setBackground(Color.BLACK);
            txtAttributes[i].setForeground(Color.WHITE);
            txtAttributes[i].setBorder(new MatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
            txtAttributes[i].setAlignmentX(CENTER_ALIGNMENT);
            txtAttributes[i].setHorizontalAlignment(SwingConstants.CENTER);
            txtAttributes[i].setFont(f);
            
            miniAttr.add(lblAttributes[i]);
            miniAttr.add(txtAttributes[i]);
            
            pnlAttributes.add(miniAttr);
        }
    }
    
    private void initSkillsPanel()
    {
        int x = pnlAttributes.getX();
        int y = pnlAttributes.getY() + pnlAttributes.getHeight() + 30;
        int w = pnlAttributes.getWidth();
        
        int sklW = 100;
        int sklH = 75;
        int hGap = 50;
        int vGap = 75;
        int numRow = (int) Math.ceil(model.getSkills().length / (w / (sklW + hGap)));
        
        int h = numRow * sklH + numRow * vGap + 20;
        
        pnlSkills = new SheetPanel("Skills", f24, false);
        pnlSkills.setBackground(Color.BLACK);
        pnlSkills.setLayout(new FlowLayout(FlowLayout.CENTER, hGap, vGap));
        pnlSkills.setPreferredSize(new Dimension(w, h));
        
        scrSkills = new SheetScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrSkills.getVerticalScrollBar().setBackground(Color.BLACK);
        scrSkills.setBounds(x, y, w, 450);
        scrSkills.setViewportView(pnlSkills);
        scrSkills.getVerticalScrollBar().setUnitIncrement(40);
        
        pnlMain.add(scrSkills);
        
        JLabel[] lblSkills = new JLabel[model.getSkills().length];
        for (int i = 0; i < lblSkills.length; i++)
        {
            lblSkills[i] = new JLabel(model.getSkills()[i]);
            txtSkills[i] = new JTextField(sheet.getSkills()[i] + "");
            
            JPanel miniSkl = new JPanel(null, false);
            miniSkl.setLayout(new BoxLayout(miniSkl, BoxLayout.PAGE_AXIS));
            miniSkl.setPreferredSize(new Dimension(sklW, sklH));
            miniSkl.setBackground(Color.BLACK);
            
            lblSkills[i].setFont(f12);
            lblSkills[i].setBounds(0, 0, getStringWidth(lblSkills[i].getFont(), lblSkills[i].getText()), lblSkills[i].getFont().getSize());
            lblSkills[i].setForeground(Color.WHITE);
            lblSkills[i].setAlignmentX(CENTER_ALIGNMENT);
            
            txtSkills[i].setBackground(Color.BLACK);
            txtSkills[i].setForeground(Color.WHITE);
            txtSkills[i].setBorder(new MatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
            txtSkills[i].setAlignmentX(CENTER_ALIGNMENT);
            txtSkills[i].setHorizontalAlignment(SwingConstants.CENTER);
            txtSkills[i].setFont(f);
            
            miniSkl.add(lblSkills[i]);
            miniSkl.add(txtSkills[i]);
            
            pnlSkills.add(miniSkl);
        }
    }
    
    private void initEquipmentsPanel()
    {
        int x = scrSkills.getX();
        int y = scrSkills.getY() + scrSkills.getHeight() + 30;
        int w = scrSkills.getWidth();
        int h = 200;
        
        pnlEquipments = new SheetPanel("Equipments", f24, false);
        pnlEquipments.setBackground(Color.BLACK);
        pnlEquipments.setLayout(null);
        
        scrEquipments = new SheetScrollPane(pnlEquipments, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrEquipments.getVerticalScrollBar().setUnitIncrement(40);
        scrEquipments.getHorizontalScrollBar().setUnitIncrement(40);
        scrEquipments.setBounds(x, y, w, h);
        pnlMain.add(scrEquipments);
        
        JPanel pnlDescriptions = new JPanel(null, false);
        pnlDescriptions.setBounds(0, 50, scrEquipments.getWidth(), f.getSize());
        pnlDescriptions.setBackground(Color.BLACK);
        pnlEquipments.add(pnlDescriptions);
        
        x = pnlDescriptions.getX();
        y = pnlDescriptions.getY() + pnlDescriptions.getHeight() + 10;
        w = pnlDescriptions.getWidth();
        h = f12.getSize() * sheet.getEquipments().size() + 10;
        
        pnlEquipmentFields = new JPanel(null, false);
        pnlEquipmentFields.setLayout(new BoxLayout(pnlEquipmentFields, BoxLayout.PAGE_AXIS));
        pnlEquipmentFields.setBounds(x, y, w, h);
        pnlEquipmentFields.setBackground(Color.BLACK);
        pnlEquipments.add(pnlEquipmentFields);
        
        pnlEquipments.setPreferredSize(new Dimension(scrSkills.getWidth(), pnlEquipmentFields.getY() + pnlEquipmentFields.getHeight() + 20));
        
        JLabel[] desc = new JLabel[model.getEquipments().length];
        int[] positions = new int[desc.length];
        x = 60;
        for (int i = 0; i < desc.length; i++)
        {
            desc[i] = new JLabel(model.getEquipments()[i]);
            desc[i].setForeground(Color.WHITE);
            desc[i].setFont(f);
            desc[i].setBounds(x, 0, getStringWidth(desc[i].getFont(), desc[i].getText()), desc[i].getFont().getSize());
            positions[i] = x;
            pnlDescriptions.add(desc[i]);
            x += desc[i].getWidth() + 60;
        }
        
        btnAddEquipment.setBackground(Color.BLACK);
        btnAddEquipment.setForeground(Color.WHITE);
        btnAddEquipment.setBounds(positions[positions.length - 1], 10, 50, 25);
        btnAddEquipment.addActionListener(l ->
        {
            Object[] obj = new Object[model.getEquipments().length];
            for (int i = 0; i < obj.length; i++)
                obj[i] = "none";
            JEquipmentField e = createEquipmentField(obj);
        });
        pnlEquipments.add(btnAddEquipment);
        
        JEquipmentField.setPositions(positions);
        for (int i = 0; i < sheet.getEquipments().size(); i++)
            createEquipmentField(sheet.getEquipments().get(i));
    }
    
    private void initItemsPanel()
    {
        int x = scrEquipments.getX();
        int y = scrEquipments.getY() + scrEquipments.getHeight();
        int w = scrEquipments.getWidth();
        int h = 200;
        int hGap = 50;
        int vGap = 50;
        
        pnlItems = new SheetPanel("Items", f24, false);
        pnlItems.setPreferredSize(new Dimension(scrSkills.getWidth(), 2000));
        pnlItems.setBackground(Color.BLACK);
        pnlItems.setLayout(new FlowLayout(FlowLayout.LEADING, hGap, vGap));
        
        scrItems = new SheetScrollPane(pnlItems, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrItems.getVerticalScrollBar().setUnitIncrement(40);
        scrItems.setBounds(x, y, w, h);
        pnlMain.add(scrItems);
        
        btnAddItem.setBackground(Color.BLACK);
        btnAddItem.setForeground(Color.WHITE);
        btnAddItem.setPreferredSize(new Dimension(75, 25));
        btnAddItem.addActionListener(l ->
        {
            String i = "New Item";
            createItem(i, hGap, vGap);
        });
        pnlItems.add(btnAddItem);
        
        for (int i = 0; i < sheet.getItems().size(); i++)
            createItem(sheet.getItems().get(i), hGap, vGap);
    }
    
    private JEquipmentField createEquipmentField(Object[] equip)
    {
        JEquipmentField e = new JEquipmentField(equip);
        e.setFont(f12);
        e.setForeground(Color.WHITE);
        
        equipments.add(e);
        pnlEquipmentFields.add(e);
        int h = (f12.getSize() + 10) * equipments.size();
        e.getRemoveButton().addActionListener(l ->
        {
            equipments.remove(e);
            pnlEquipmentFields.remove(e);
            int hh = (f12.getSize() + 10) * equipments.size();
            pnlEquipmentFields.setBounds(pnlEquipmentFields.getX(), 
                pnlEquipmentFields.getY(), 
                pnlEquipmentFields.getWidth(), hh);
            
            pnlEquipments.setPreferredSize(new Dimension(pnlEquipments.getPreferredSize().width, pnlEquipmentFields.getY() + pnlEquipmentFields.getHeight() + 20));
            
            scrEquipments.revalidate();
            pnlEquipments.revalidate();
        });
        
        pnlEquipmentFields.setBounds(pnlEquipmentFields.getX(), 
                pnlEquipmentFields.getY(), 
                pnlEquipmentFields.getWidth(), h);
        pnlEquipments.setPreferredSize(new Dimension(getPreferredSize().width, pnlEquipmentFields.getY() + pnlEquipmentFields.getHeight() + 20));
        scrEquipments.revalidate();
        pnlEquipments.revalidate();
        return e;
    }
    
    private JLabel createItem(String name, int hGap, int vGap)
    {
        JLabel i = new JLabel(name);
        i.setFont(f);
        i.setForeground(Color.WHITE);
        i.setPreferredSize(new Dimension(getStringWidth(i.getFont(), name), i.getFont().getSize()));
        
        items.add(i);
        pnlItems.add(i);
        int numRow = (int) Math.ceil(items.size() / (scrItems.getWidth() / (i.getPreferredSize().width + hGap)));
        int h = numRow * i.getPreferredSize().height + numRow * vGap + 20;
        pnlItems.setPreferredSize(new Dimension(pnlItems.getPreferredSize().width, h));
        i.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON3)
                {
                    items.remove(i);
                    pnlItems.remove(i);
                    int numRow = (int) Math.ceil(items.size() / (scrItems.getWidth() / (i.getPreferredSize().width + hGap)));
                    int h = numRow * i.getPreferredSize().height + numRow * vGap + 20;
                    pnlItems.setPreferredSize(new Dimension(pnlItems.getPreferredSize().width, h));
                    pnlItems.revalidate();
                    pnlItems.repaint();
                    scrItems.revalidate();
                }
                else
                {
                    if (e.getClickCount() == 2)
                    {
                        String newVal = JOptionPane.showInputDialog("Please type in new value.", i.getText());
                        
                        if (newVal == null)
                            return;
                        
                        i.setText(newVal);
                        i.setPreferredSize(new Dimension(getStringWidth(i.getFont(), i.getText()), i.getPreferredSize().height));
                    }
                }
            }
        });
        
        pnlItems.revalidate();
        pnlItems.repaint();
        scrItems.revalidate();
        return i;
    }
    
    private int getStringWidth(Font font, String text)
    {
        return getFontMetrics(font).stringWidth(text);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setTitle("RPG Simulator Sheet");
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1280, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 720, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    
}
