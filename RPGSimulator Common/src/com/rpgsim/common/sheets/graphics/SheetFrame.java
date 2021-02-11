package com.rpgsim.common.sheets.graphics;

import com.rpgsim.common.serverpackages.UpdateType;
import com.rpgsim.common.sheets.Equipment;
import com.rpgsim.common.sheets.PlayerSheet;
import com.rpgsim.common.sheets.SheetModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

public abstract class SheetFrame extends javax.swing.JFrame
{
    protected static final int CHARACTER_INFO_ID = 0;
    protected static final int ABOUT_ID = 1;
    protected static final int EXTRAS_ID = 2;
    protected static final int ATTRIBUTES_ID = 3;
    protected static final int SKILLS_ID = 4;
    protected static final int EQUIPMENT_ID = 5;
    protected static final int ITEM_ID = 6;
    protected static final int FIELDS_LENGTH = 7;
    
    private SheetModel model;
    private PlayerSheet playerSheet;
    
    private final Font f = new Font("Comic Sans MS", Font.PLAIN, 16);
    private final Font f12 = f.deriveFont(12f);
    private final Font f24 = f.deriveFont(24f);
    
    private SheetPanel pnlInfo, pnlAbout, pnlExtras, pnlStats, 
            pnlAttributes, pnlSkills, pnlItems;
    private EquipmentPanel pnlEquipments;
    private JScrollPane scrSkills, scrEquipments, scrItems;
    
    private JTextField[] txtInfo;
    private JTextArea txaAbout, txaExtras;
    private JTextField[] txtAttributes;
    private JTextField[] txtSkills;
    
    private final SheetPanel[] fields = new SheetPanel[FIELDS_LENGTH];
    
    public SheetFrame(SheetModel model)
    {
        this.model = model;
        this.playerSheet = new PlayerSheet(model);
        
        initComponents();
        internalLoad();
        
        fields[CHARACTER_INFO_ID] = pnlInfo;
        fields[ABOUT_ID] = pnlAbout;
        fields[EXTRAS_ID] = pnlExtras;
        fields[ATTRIBUTES_ID] = pnlAttributes;
        fields[SKILLS_ID] = pnlSkills;
        fields[EQUIPMENT_ID] = pnlEquipments;
        fields[ITEM_ID] = pnlItems;
        
        pnlMain.setPreferredSize(new Dimension(pnlMain.getPreferredSize().width, scrEquipments.getY() + scrEquipments.getHeight() + 20));
        pnlMain.setBackground(Color.BLACK);
        scrMain.getVerticalScrollBar().setUnitIncrement(40);
    }
    
    public SheetFrame(SheetModel model, PlayerSheet sheet)
    {
        this.model = model;
        this.playerSheet = sheet;
        
        initComponents();
        internalLoad();
        
        fields[CHARACTER_INFO_ID] = pnlInfo;
        fields[ABOUT_ID] = pnlAbout;
        fields[EXTRAS_ID] = pnlExtras;
        fields[ATTRIBUTES_ID] = pnlAttributes;
        fields[SKILLS_ID] = pnlSkills;
        fields[EQUIPMENT_ID] = pnlEquipments;
        fields[ITEM_ID] = pnlItems;
        
        pnlMain.setPreferredSize(new Dimension(pnlMain.getPreferredSize().width, scrEquipments.getY() + scrEquipments.getHeight() + 20));
        pnlMain.setBackground(Color.BLACK);
        scrMain.getVerticalScrollBar().setUnitIncrement(40);
    }
    
    private void internalLoad()
    {
        loadBasicInfoPanel();
        loadBasicStatsPanel();
        loadAboutPanel();
        loadAttributesPanel();
        loadSkillsPanel();
        loadEquipmentsPanel();
        loadItemsPanel();
    }

    public void setPlayerSheet(PlayerSheet playerSheet)
    {
        if (this.playerSheet.equals(playerSheet))
            return;
        this.playerSheet = playerSheet;
        reloadSheets();
    }
    
    private void reloadSheets()
    {
        for (int i = 0; i < txtInfo.length; i++)
            txtInfo[i].setText(playerSheet.getBasicInfo()[i]);
        for (int i = 0; i < txtAttributes.length; i++)
            txtAttributes[i].setText(playerSheet.getAttributes()[i] + "");
        for (int i = 0; i < txtSkills.length; i++)
            txtSkills[i].setText(playerSheet.getSkills()[i] + "");
        pnlEquipments.setEquipments(playerSheet.getEquipments());
    }
    
    private void loadBasicInfoPanel()
    {
        pnlInfo = new SheetPanel("Character Information", f24, null, true)
        {
            @Override
            public void updateSheet(int propertyID, Object newValue, UpdateType type)
            {
                String s = (String) newValue;
                playerSheet.getBasicInfo()[propertyID] = s;
                txtInfo[propertyID].setText(s);
            }
        };
        pnlMain.add(pnlInfo);
        JLabel[] lblBasicInfo = new JLabel[model.getBasicInfo().length];
        txtInfo = new JTextField[model.getBasicInfo().length];
        
        int lines = lblBasicInfo.length;
        int gap = 40;
        
        int txtGap = 10;
        
        int x = 100;
        int y = 10;
        
        pnlInfo.setBounds(x, y, getWidth() / 2 - 100, lines * f.getSize() + lines * txtGap + lines * gap);
        
        x = 10;
        y = 50;
        
        for (int i = 0; i < lblBasicInfo.length; i++)
        {
            lblBasicInfo[i] = new JLabel(model.getBasicInfo()[i]);
            
            txtInfo[i] = new JTextField(playerSheet.getBasicInfo()[i]);
            final int ii = i;
            txtInfo[i].addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyTyped(KeyEvent e)
                {
                    String txt;
                    if (Character.isLetterOrDigit(e.getKeyChar()))
                        txt = txtInfo[ii].getText() + e.getKeyChar();
                    else
                        txt = txtInfo[ii].getText();
                    sendCharacterSheetUpdate(CHARACTER_INFO_ID, ii, txt, UpdateType.UPDATE);
                }
            });

            
            pnlInfo.add(txtInfo[i]);
            pnlInfo.add(lblBasicInfo[i]);
            
            JLabel lbl = lblBasicInfo[i];
            JTextField txt = txtInfo[i];
            
            lbl.setFont(f);
            lbl.setBounds(x, y, getStringWidth(lbl.getFont(), lbl.getText()), lbl.getFont().getSize());
            lbl.setForeground(Color.WHITE);
            
            txt.setBackground(Color.BLACK);
            txt.setForeground(Color.WHITE);
            txt.setBorder(new MatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
            txt.setBounds(x, y + lbl.getHeight() + txtGap, pnlInfo.getWidth() - x - 30, 20);
            
            y += lbl.getHeight() + gap;
        }
    }
    
    private void loadBasicStatsPanel()
    {
        pnlStats = new SheetPanel("Stats", f24, null, true)
        {
            @Override
            public void updateSheet(int propertyID, Object newValue, UpdateType type)
            {
                System.out.println("stats updateSheet called.");
            }
        };
        pnlMain.add(pnlStats);
        
        pnlStats.setBounds(pnlInfo.getX() + pnlInfo.getWidth() + 20, 10, getWidth() / 2 - 100, pnlInfo.getHeight());
        
        JLabel[] lblBasicStats = new JLabel[model.getBasicStats().length];
        for (int i = 0; i < lblBasicStats.length; i++)
        {
            lblBasicStats[i] = new JLabel(model.getBasicStats()[i]);
            pnlStats.add(lblBasicStats[i]);
        }
    }
    
    private void loadAboutPanel()
    {
        int txtH = 250;
        
        pnlAbout = new SheetPanel("About", f24, null, true)
        {
            @Override
            public void updateSheet(int propertyID, Object newValue, UpdateType type)
            {
                String s = (String) newValue;
                playerSheet.setAbout(s);
                txaAbout.setText(s);
            }
        };
        pnlAbout.setBounds(pnlStats.getX(), pnlStats.getY() + pnlStats.getHeight() + 20, pnlStats.getWidth(), txtH + 60);
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
                    g.drawString("Write about your character's likes, dislikes, ", 0, f.getSize());
                    g.drawString("dreams, etc.", 0, f.getSize() * 2);
                }
            }
        };
        txaAbout.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                if (txaAbout.getText().length() < 1)
                {
                    txaAbout.repaint();
                }
                String txt;
                if (Character.isLetterOrDigit(e.getKeyChar()))
                    txt = txaAbout.getText() + e.getKeyChar();
                else
                    txt = txaAbout.getText();
                sendCharacterSheetUpdate(ABOUT_ID, 0, txt, UpdateType.UPDATE);
            }
        });
        txaAbout.setBackground(Color.DARK_GRAY);
        txaAbout.setForeground(Color.WHITE);
        txaAbout.setBorder(new MatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
        txaAbout.setFont(f);
        txaAbout.setBounds(10, 50, pnlAbout.getWidth() - 10 - 20, txtH);
        txaAbout.setWrapStyleWord(true);
        txaAbout.setLineWrap(true);
        pnlAbout.add(txaAbout);
        
        pnlExtras = new SheetPanel("Extras", f24, null, true)
        {
            @Override
            public void updateSheet(int propertyID, Object newValue, UpdateType type)
            {
                String s = (String) newValue;
                playerSheet.setExtras(s);
                txaExtras.setText(s);
            }
        };
        pnlExtras.setBounds(pnlAbout.getX(), pnlAbout.getY() + pnlAbout.getHeight() + 20, pnlAbout.getWidth(), txtH + 60);
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
                    g.drawString("You can write anything you want here. E.g:", 0, f.getSize());
                    g.drawString("Notes, things you want to do, etc.", 0, f.getSize() * 2);
                }
            }
        };
        txaExtras.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                if (txaExtras.getText().length() < 1)
                {
                    txaExtras.repaint();
                }
                String txt;
                if (Character.isLetterOrDigit(e.getKeyChar()))
                    txt = txaExtras.getText() + e.getKeyChar();
                else
                    txt = txaExtras.getText();
                sendCharacterSheetUpdate(EXTRAS_ID, 0, txt, UpdateType.UPDATE);
            }
        });
        txaExtras.setBackground(Color.DARK_GRAY);
        txaExtras.setForeground(Color.WHITE);
        txaExtras.setBorder(new MatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
        txaExtras.setFont(f);
        txaExtras.setBounds(10, 50, pnlAbout.getWidth() - 10 - 20, txtH);
        txaExtras.setWrapStyleWord(true);
        txaExtras.setLineWrap(true);
        
        pnlExtras.add(txaExtras);
    }
    
    private void loadAttributesPanel()
    {
        int w = pnlInfo.getWidth();
        
        FlowLayout l = new FlowLayout(FlowLayout.CENTER, 25, 50);
        pnlAttributes = new SheetPanel("Attributes", f24, l, true)
        {
            @Override
            public void updateSheet(int propertyID, Object newValue, UpdateType type)
            {
                String s = (String) newValue;
                try
                {
                    playerSheet.getAttributes()[propertyID] = Integer.parseInt(s);
                }
                catch(NumberFormatException ex)
                {
                    System.out.println("could not parse " + s);
                }
                txtAttributes[propertyID].setText(s);
            }
        };
        pnlMain.add(pnlAttributes);
        
        JLabel[] lblAttributes = new JLabel[model.getAttributes().length];
        txtAttributes = new JTextField[model.getAttributes().length];
        
        int attrW = 100;
        int attrH = 75;
        
        int numRow = (int) Math.ceil(lblAttributes.length / (w / (attrW + l.getHgap())));
        
        pnlAttributes.setBounds(pnlInfo.getX(), pnlInfo.getY() + pnlInfo.getHeight() + 20, w, numRow * attrH + numRow * l.getVgap() + 20);
        
        for (int i = 0; i < lblAttributes.length; i++)
        {
            lblAttributes[i] = new JLabel(model.getAttributes()[i]);
            txtAttributes[i] = new JTextField(playerSheet.getAttributes()[i] + "");
            final int ii = i;
            txtAttributes[i].addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyTyped(KeyEvent e)
                {
                    String txt;
                    if (Character.isLetterOrDigit(e.getKeyChar()))
                        txt = txtAttributes[ii].getText() + e.getKeyChar();
                    else
                        txt = txtAttributes[ii].getText();
                    sendCharacterSheetUpdate(ATTRIBUTES_ID, ii, txt, UpdateType.UPDATE);
                }
            });
            
            JLabel lbl = lblAttributes[i];
            JTextField txt = txtAttributes[i];
            
            JPanel miniAttr = new JPanel(null, false);
            miniAttr.setLayout(new BoxLayout(miniAttr, BoxLayout.PAGE_AXIS));
            miniAttr.setPreferredSize(new Dimension(attrW, attrH));
            miniAttr.setBackground(Color.BLACK);
            
            lbl.setFont(f12);
            lbl.setBounds(0, 0, getStringWidth(lbl.getFont(), lbl.getText()), lbl.getFont().getSize());
            lbl.setForeground(Color.WHITE);
            lbl.setAlignmentX(CENTER_ALIGNMENT);
            
            txt.setBackground(Color.BLACK);
            txt.setForeground(Color.WHITE);
            txt.setBorder(new MatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
            txt.setAlignmentX(CENTER_ALIGNMENT);
            txt.setHorizontalAlignment(SwingConstants.CENTER);
            txt.setFont(f);
            
            miniAttr.add(lbl);
            miniAttr.add(txt);
            
            pnlAttributes.add(miniAttr);
        }
    }
    
    private void loadSkillsPanel()
    {
        int x = pnlInfo.getX();
        int y = Math.max(pnlAttributes.getY() + pnlAttributes.getHeight(), pnlExtras.getY() + pnlExtras.getHeight()) + 20;
        int w = getWidth() - x - 125;
        
        int sklW = 100;
        int sklH = 75;
        
        JLabel[] lblSkills = new JLabel[model.getSkills().length];
        txtSkills = new JTextField[model.getSkills().length];
        
        FlowLayout l = new FlowLayout(FlowLayout.CENTER, 50, 75);
        pnlSkills = new SheetPanel("Skills", f24, l, false)
        {
            @Override
            public void updateSheet(int propertyID, Object newValue, UpdateType type)
            {
                String s = (String) newValue;
                try
                {
                    playerSheet.getSkills()[propertyID] = Integer.parseInt(s);
                }
                catch(NumberFormatException ex)
                {
                    System.out.println("could not parse " + s);
                }
                txtSkills[propertyID].setText(s);
            }
        };
        
        int numRow = (int) Math.ceil(lblSkills.length / (w / (sklW + l.getHgap())));
        
        int h = numRow * sklH + numRow * l.getVgap() + 20;
        
        pnlSkills.setBounds(x, y, w, h);
        pnlSkills.setPreferredSize(new Dimension(w, h));
        
        scrSkills = new SheetScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrSkills.getVerticalScrollBar().setBackground(Color.BLACK);
        scrSkills.setBounds(x, y, w, 500);
        scrSkills.setPreferredSize(new Dimension(w, 500));
        scrSkills.setViewportView(pnlSkills);
        scrSkills.getVerticalScrollBar().setUnitIncrement(40);
        
        pnlMain.add(scrSkills);
        
        for (int i = 0; i < lblSkills.length; i++)
        {
            lblSkills[i] = new JLabel(model.getSkills()[i]);
            txtSkills[i] = new JTextField(playerSheet.getSkills()[i] + "");
            final int ii = i;
            txtSkills[i].addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyTyped(KeyEvent e)
                {
                    String txt;
                    if (Character.isLetterOrDigit(e.getKeyChar()))
                        txt = txtSkills[ii].getText() + e.getKeyChar();
                    else
                        txt = txtSkills[ii].getText();
                    sendCharacterSheetUpdate(SKILLS_ID, ii, txt, UpdateType.UPDATE);
                }
            });
            
            JLabel lbl = lblSkills[i];
            JTextField txt = txtSkills[i];
            
            JPanel miniSkl = new JPanel(null, false);
            miniSkl.setLayout(new BoxLayout(miniSkl, BoxLayout.PAGE_AXIS));
            miniSkl.setPreferredSize(new Dimension(sklW, sklH));
            miniSkl.setBackground(Color.BLACK);
            
            lbl.setFont(f12);
            lbl.setBounds(0, 0, getStringWidth(lbl.getFont(), lbl.getText()), lbl.getFont().getSize());
            lbl.setForeground(Color.WHITE);
            lbl.setAlignmentX(CENTER_ALIGNMENT);
            
            txt.setBackground(Color.BLACK);
            txt.setForeground(Color.WHITE);
            txt.setBorder(new MatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
            txt.setAlignmentX(CENTER_ALIGNMENT);
            txt.setHorizontalAlignment(SwingConstants.CENTER);
            txt.setFont(f);
            
            miniSkl.add(lbl);
            miniSkl.add(txt);
            
            pnlSkills.add(miniSkl);
        }
    }
    
    private void loadEquipmentsPanel()
    {
        int x = scrSkills.getX();
        int y = scrSkills.getY() + scrSkills.getHeight() + 20;
        int w = scrSkills.getWidth();
        
        ArrayList<Equipment> equipments = playerSheet.getEquipments();
        pnlEquipments = new EquipmentPanel("Equipments", f24, null, false, equipments)
        {
            @Override
            public void buttonRemoveEquipment(int propertyID)
            {
                pnlEquipments.removeEquipment(propertyID);
                sendCharacterSheetUpdate(EQUIPMENT_ID, propertyID, null, UpdateType.REMOVE);
            }
        };
        pnlEquipments.setBounds(10, 60, pnlEquipments.getWidth() - 20, pnlEquipments.getHeight() - 80);
        pnlEquipments.setPreferredSize(new Dimension(w, 2000));
        
        scrEquipments = new SheetScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrEquipments.setViewportView(pnlEquipments);
        scrEquipments.getVerticalScrollBar().setUnitIncrement(40);
        scrEquipments.setBounds(x, y, w, 200);
        pnlMain.add(scrEquipments);
        
    }
    
    private void loadItemsPanel()
    {
        int x = scrEquipments.getX();
        int y = scrEquipments.getY() + scrEquipments.getHeight() + 20;
        int w = scrEquipments.getWidth();
        
        FlowLayout l = new FlowLayout(FlowLayout.CENTER, 10, 45);
        pnlItems = new SheetPanel("Equipments", f24, null, false)
        {
            @Override
            public void updateSheet(int propertyID, Object newValue, UpdateType type)
            {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        pnlItems.setBackground(Color.red);
        this.pnlItems.setLayout(l);
    }

    public PlayerSheet getPlayerSheet()
    {
        return playerSheet;
    }
    
    private int getStringWidth(Font font, String text)
    {
        return getFontMetrics(font).stringWidth(text);
    }
    
    public void onReceiveCharacterSheetUpdate(int fieldID, int propertyID, Object newValue, UpdateType type)
    {
        fields[fieldID].updateSheet(propertyID, newValue, type);
    }
    
    protected abstract void sendCharacterSheetUpdate(int fieldID, int propertyID, Object newValue, UpdateType type);
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrMain = scrMain = new SheetScrollPane(null)
        ;
        pnlMain = new javax.swing.JPanel();

        setTitle("RPG Simulator Sheet");
        setResizable(false);

        scrMain.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        pnlMain.setPreferredSize(new java.awt.Dimension(1280, 720));

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1280, Short.MAX_VALUE)
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 720, Short.MAX_VALUE)
        );

        scrMain.setViewportView(pnlMain);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrMain, javax.swing.GroupLayout.DEFAULT_SIZE, 1280, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrMain, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlMain;
    private javax.swing.JScrollPane scrMain;
    // End of variables declaration//GEN-END:variables

    
}
