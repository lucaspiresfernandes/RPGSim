package com.rpgsim.common.sheets.graphics;

import com.rpgsim.common.FileManager;
import com.rpgsim.common.serverpackages.UpdateType;
import com.rpgsim.common.sheets.PlayerSheet;
import com.rpgsim.common.sheets.SheetModel;
import com.rpgsim.common.sheets.UpdateField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public abstract class OLDSheetFrame extends javax.swing.JFrame
{
    private int connectionID;
    private SheetModel model;
    private PlayerSheet sheet;
    
    private final Font f = new Font("Comic Sans MS", Font.PLAIN, 16);
    private final Font f12 = f.deriveFont(12f);
    private final Font f24 = f.deriveFont(24f);
    
    private final SheetScrollPane scrMain;
    private final JPanel pnlMain;
    private SheetScrollPane scrSkills, scrEquipments, scrItems;
    private SheetPanel pnlInfo, pnlStats, pnlAbout, pnlExtras, pnlAttributes, pnlSkills, pnlEquipments, pnlItems;
    private JPanel pnlEquipmentFields, pnlItemFields;
    
    private JTextField[] txtInfo;
    
    private JLabel avatarImage;
    private boolean loadImage;
    private JProgressBar[] prgStats;
    private JButton[] btnStatsUp;
    private JButton[] btnStatsDown;
    
    private JTextArea txaAbout, txaExtras;
    
    private JTextField[] txtAttributes;
    private JCheckBox[] chkAttributeMarked;
    
    private JTextField[] txtSkills;
    private JCheckBox[] chkSkillMarked;
    
    private final ArrayList<JField> equipments = new ArrayList<>();
    private final JButton btnAddEquipment = new JButton("+");
    
    private final ArrayList<JField> items = new ArrayList<>();
    private final JButton btnAddItem = new JButton("+");
    
    private boolean localChange = false;
    
    private int[] equipmentPositions, itemPositions;
    
    //Rolling dices.
    private final Random rand = new Random();
    private int diceNumber, fieldNumber;
    private JLabel lblDiceAnimation;
    private JPanel pnlRoll;
    
    public OLDSheetFrame()
    {
        initComponents();
        
        pnlMain = new JPanel(null, false);
        scrMain = new SheetScrollPane(pnlMain);
        scrMain.setBounds(0, 0, getWidth(), getHeight());
        getContentPane().add(scrMain);
    }
    
    public void load(int connectionID, SheetModel model, PlayerSheet sheet, boolean loadImage)
    {
        this.model = model;
        this.sheet = sheet;
        this.connectionID = connectionID;
        this.loadImage = loadImage;
        
        txtInfo = new JTextField[sheet.getInfo().length];
        txtAttributes = new JTextField[sheet.getAttributes().length];
        chkAttributeMarked = new JCheckBox[sheet.getAttributeMarked().length];
        txtSkills = new JTextField[sheet.getSkills().length];
        chkSkillMarked = new JCheckBox[sheet.getSkillMarked().length];
        prgStats = new JProgressBar[sheet.getCurrentStats().length];
        btnStatsUp = new JButton[prgStats.length];
        btnStatsDown = new JButton[prgStats.length];
        
        initInfoPanel();
        initStatsPanel();
        initAboutPanel();
        initExtrasPanel();
        initAttributesPanel();
        initSkillsPanel();
        initEquipmentsPanel();
        initItemsPanel();
        initRollPanel();
        
        pnlMain.setPreferredSize(new Dimension(getWidth(), scrItems.getY() + scrItems.getHeight() + 50));
        pnlMain.setBackground(Color.BLACK);
        scrMain.getVerticalScrollBar().setUnitIncrement(40);
    }
    
    public void reload(int connectionID, PlayerSheet sheet)
    {
        this.sheet = sheet;
        this.connectionID = connectionID;
        
        for (int i = 0; i < txtInfo.length; i++)
            txtInfo[i].setText(sheet.getInfo()[i]);
        
        for (int i = 0; i < prgStats.length; i++)
        {
//            prgStats[i].setValue(Integer.parseInt(sheet.getCurrentStats()[i]));
//            prgStats[i].setMaximum(Integer.parseInt(sheet.getMaxStats()[i]));
            prgStats[i].setString(sheet.getCurrentStats()[i] + "/" + sheet.getMaxStats()[i]);
        }
        
        txaAbout.setText(sheet.getAbout());
        
        txaExtras.setText(sheet.getExtras());
        
//        for (int i = 0; i < txtAttributes.length; i++)
//            txtAttributes[i].setText(sheet.getAttributes()[i]);
//        for (int i = 0; i < chkAttributeMarked.length; i++)
//            chkAttributeMarked[i].setSelected(sheet.getAttributeMarked()[i]);
//        
//        for (int i = 0; i < txtSkills.length; i++)
//            txtSkills[i].setText(sheet.getSkills()[i]);
//        for (int i = 0; i < chkSkillMarked.length; i++)
//            chkSkillMarked[i].setSelected(sheet.getSkillMarked()[i]);
//        
//        for (int i = 0; i < equipments.size(); i++)
//            removeEquipmentField(i);
//        for (int i = 0; i < sheet.getEquipments().size(); i++)
//            createEquipmentField(sheet.getEquipments().get(i));
//        
//        for (int i = 0; i < items.size(); i++)
//            removeItem(i);
//        for (int i = 0; i < sheet.getItems().size(); i++)
//            createItem(sheet.getItems().get(i));
    }
    
    public void networkUpdate(UpdateField field, Object newValue, int propertyIndex)
    {
        localChange = false;
        switch (field)
        {
            case INFO:
                String aux = (String) newValue;
                sheet.getInfo()[propertyIndex] = aux;
                txtInfo[propertyIndex].setText(aux);
                break;
            case ABOUT:
                aux = (String) newValue;
                sheet.setAbout(aux);
                txaAbout.setText(aux);
                break;
            case EXTRAS:
                aux = (String) newValue;
                sheet.setExtras(aux);
                txaExtras.setText(aux);
                break;
            case CUR_STATS:
//                aux = (String) newValue;
//                int intAux = Integer.parseInt(aux);
//                sheet.getCurrentStats()[propertyIndex] = aux;
//                prgStats[propertyIndex].setValue(intAux);
//                prgStats[propertyIndex].setString(sheet.getCurrentStats()[propertyIndex] + "/" + sheet.getMaxStats()[propertyIndex]);
                break;
            case MAX_STATS:
//                aux = (String) newValue;
//                intAux = Integer.parseInt(aux);
//                
//                sheet.getMaxStats()[propertyIndex] = aux;
//                
//                if (intAux < prgStats[propertyIndex].getValue())
//                    sheet.getCurrentStats()[propertyIndex] = aux;
//                
//                prgStats[propertyIndex].setMaximum(intAux);
                
                prgStats[propertyIndex].setString(sheet.getCurrentStats()[propertyIndex] + "/" + sheet.getMaxStats()[propertyIndex]);
                break;
            case ATTRIBUTES:
//                aux = (String) newValue;
//                sheet.getAttributes()[propertyIndex] = aux;
//                txtAttributes[propertyIndex].setText(aux);
                break;
            case MARK_ATTRIBUTES:
                boolean bAux = (boolean) newValue;
                sheet.getAttributeMarked()[propertyIndex] = bAux;
                chkAttributeMarked[propertyIndex].setSelected(bAux);
                break;
            case SKILLS:
//                aux = (String) newValue;
//                sheet.getSkills()[propertyIndex] = aux;
//                txtSkills[propertyIndex].setText(aux);
                break;
            case MARK_SKILLS:
                bAux = (boolean) newValue;
                sheet.getSkillMarked()[propertyIndex] = bAux;
                chkSkillMarked[propertyIndex].setSelected(bAux);
                break;
            case EQUIPMENTS:
//                String[] str = (String[]) newValue;
//                sheet.getEquipments().set(propertyIndex, str);
//                equipments.get(propertyIndex).setFields(str);
//                repaint();
                break;
            case ITEMS:
//                str = (String[]) newValue;
//                sheet.getItems().set(propertyIndex, str);
//                items.get(propertyIndex).setFields(str);
                repaint();
                break;
        }
    }
    
    public void networkAdd(UpdateField field, Object newValue, int propertyIndex)
    {
        switch (field)
        {
            case EQUIPMENTS:
                String[] e = (String[]) newValue;
                sheet.getEquipments().add(e);
                createEquipmentField(e);
                break;
            case ITEMS:
                e = (String[]) newValue;
                sheet.getItems().add(e);
                createItem(e);
                break;
        }
    }
    
    public void networkRemove(UpdateField field, Object newValue, int propertyIndex)
    {
        switch (field)
        {
            case EQUIPMENTS:
                sheet.getEquipments().remove(propertyIndex);
                removeEquipmentField(propertyIndex);
                break;
            case ITEMS:
                sheet.getItems().remove(propertyIndex);
                removeItem(propertyIndex);
                break;
        }
    }

    public PlayerSheet getSheet()
    {
        return sheet;
    }

    public int getConnectionID()
    {
        return connectionID;
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
            final int ii = i;
            txtInfo[i].addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyPressed(KeyEvent e)
                {
                    localChange = true;
                }
            });
            txtInfo[i].getDocument().addDocumentListener(new SheetDocumentListener()
            {
                @Override
                public void update()
                {
                    if (!localChange)
                        return;
                    
                    sheet.getInfo()[ii] = txtInfo[ii].getText();
                    
                    sendSheetUpdate(UpdateField.INFO, txtInfo[ii].getText(), ii, UpdateType.UPDATE);
                }
            });
            
            pnlInfo.add(lblInfo[i]);
            pnlInfo.add(txtInfo[i]);
            
            y += lblInfo[i].getHeight() + gap;
        }
        pnlMain.add(pnlInfo);
    }
    
    private void initStatsPanel()
    {
//        int x = pnlInfo.getX() + pnlInfo.getWidth()+ 20;
//        int y = 10;
//        int w = getWidth() / 2 - x;
//        int h = pnlInfo.getHeight();
//        
//        pnlStats = new SheetPanel("Stats", f24, true);
//        pnlStats.setLayout(null);
//        pnlStats.setBounds(x, y, w, h);
//        pnlStats.setBackground(Color.BLACK);
//        pnlStats.setBounds(x, y, getWidth() / 2 - 100, h);
//        pnlMain.add(pnlStats);
//        
//        int iconW = 150;
//        int iconH = 150;
//        x = pnlStats.getWidth() / 2 - iconW / 2;
//        y = 50;
//        
//        if (loadImage)
//        {
//            ImageIcon icon = new ImageIcon(FileManager.app_dir + sheet.getAvatarRelativePath());
//            Image aux = icon.getImage().getScaledInstance(iconW, iconH, Image.SCALE_SMOOTH);
//            icon.setImage(aux);
//            avatarImage = new JLabel(icon);
//            avatarImage.setBounds(x, y, iconW, iconH);
//            pnlStats.add(avatarImage);
//        }
//        
//        x = 10;
//        y += iconH + 20;
//        w = pnlStats.getWidth() - x - 10;
//        h = 25;
//        
//        for (int i = 0; i < prgStats.length; i++)
//        {
//            JLabel desc = new JLabel(model.getStats()[i].getName());
//            desc.setFont(f12);
//            int lblH = desc.getFont().getSize();
//            desc.setForeground(Color.WHITE);
//            desc.setBounds(x, y - lblH - 5, getStringWidth(desc.getFont(), desc.getText()), lblH);
//            
//            prgStats[i] = new JProgressBar(0, Integer.parseInt(sheet.getMaxStats()[i]));
//            prgStats[i].setValue(Integer.parseInt(sheet.getCurrentStats()[i]));
//            prgStats[i].setBounds(x, y, w, h);
//            prgStats[i].setBackground(Color.BLACK);
//            prgStats[i].setForeground(new Color(model.getStats()[i].getColor()));
//            prgStats[i].setStringPainted(true);
//            prgStats[i].setString(sheet.getCurrentStats()[i] + "/" + sheet.getMaxStats()[i]);
//            
//            final int ii = i;
//            prgStats[i].addMouseListener(new MouseAdapter()
//            {
//                @Override
//                public void mouseClicked(MouseEvent e)
//                {
//                    if (e.getClickCount() >= 2)
//                    {
//                        String value = JOptionPane.showInputDialog("Type in the new maximum value to this field.", prgStats[ii].getMaximum());
//                        
//                        if (value == null || value.isEmpty())
//                            return;
//                        
//                        try
//                        {
//                            int maxVal = Integer.parseInt(value);
//                            
//                            if (maxVal <= 0)
//                                return;
//                            
//                            int curVal = prgStats[ii].getValue();
//                            prgStats[ii].setMaximum(maxVal);
//                            sheet.getMaxStats()[ii] = value;
//                            
//                            if (maxVal < curVal)
//                                sheet.getCurrentStats()[ii] = Integer.toString(maxVal);
//                            
//                            prgStats[ii].setString(sheet.getCurrentStats()[ii] + "/" + sheet.getMaxStats()[ii]);
//                            sendSheetUpdate(UpdateField.MAX_STATS, value, ii, UpdateType.UPDATE);
//                        }
//                        catch (NumberFormatException ex)
//                        {
//                            JOptionPane.showMessageDialog(null, "Only numbers are allowed!");
//                        }
//                        
//                    }
//                }
//            });
//            
//            btnStatsUp[i] = new JButton("+");
//            btnStatsUp[i].setBounds(x, y + h + 10, 100, 20);
//            btnStatsUp[i].setBackground(Color.BLACK);
//            btnStatsUp[i].setForeground(Color.WHITE);
//            btnStatsUp[i].addActionListener(l -> 
//            {
//                int val = prgStats[ii].getValue();
//                int maxVal = prgStats[ii].getMaximum();
//                
//                if (++val > maxVal)
//                    return;
//                
//                String strVal = Integer.toString(val);
//                
//                prgStats[ii].setValue(val);
//                sheet.getCurrentStats()[ii] = strVal;
//                prgStats[ii].setString(strVal + "/" + sheet.getMaxStats()[ii]);
//                sendSheetUpdate(UpdateField.CUR_STATS, strVal, ii, UpdateType.UPDATE);
//            });
//            
//            btnStatsDown[i] = new JButton("-");
//            btnStatsDown[i].setBackground(Color.BLACK);
//            btnStatsDown[i].setForeground(Color.WHITE);
//            btnStatsDown[i].setBounds(x + w - 100, y + h + 10, 100, 20);
//            btnStatsDown[i].addActionListener(l -> 
//            {
//                int val = prgStats[ii].getValue();
//                
//                if (--val < 0)
//                    return;
//                
//                String strVal = Integer.toString(val);
//                
//                prgStats[ii].setValue(val);
//                sheet.getCurrentStats()[ii] = strVal;
//                prgStats[ii].setString(strVal + "/" + sheet.getMaxStats()[ii]);
//                sendSheetUpdate(UpdateField.CUR_STATS, strVal, ii, UpdateType.UPDATE);
//            });
//            
//            y += h + 60;
//            
//            pnlStats.add(desc);
//            pnlStats.add(prgStats[i]);
//            pnlStats.add(btnStatsUp[i]);
//            pnlStats.add(btnStatsDown[i]);
//        }
//        
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
        
        txaAbout = new JTextArea(sheet.getAbout())
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
        
        txaAbout.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                localChange = true;
            }
        });
        txaAbout.getDocument().addDocumentListener(new SheetDocumentListener()
        {
            @Override
            public void update()
            {
                if (txaAbout.getText().length() <= 1)
                    txaAbout.repaint();
                
                if (!localChange)
                    return;
                
                sheet.setAbout(txaAbout.getText());
                
                sendSheetUpdate(UpdateField.ABOUT, txaAbout.getText(), 0, UpdateType.UPDATE);
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
        
        txaExtras = new JTextArea(sheet.getExtras())
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
        
        txaExtras.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                localChange = true;
            }
        });
        txaExtras.getDocument().addDocumentListener(new SheetDocumentListener()
        {
            @Override
            public void update()
            {
                if (txaExtras.getText().length() <= 1)
                    txaExtras.repaint();
                
                if (!localChange)
                    return;
                
                sheet.setExtras(txaExtras.getText());
                
                sendSheetUpdate(UpdateField.EXTRAS, txaExtras.getText(), 0, UpdateType.UPDATE);
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
        int y = pnlAbout.getY() + pnlAbout.getHeight() + 50;
        int w = pnlExtras.getX() + pnlExtras.getWidth() - x;
        
        int pnlW = 125;
        int pnlH = 75;
        int hGap = 25;
        int vGap = 50;
        int numRow = (int) Math.ceil(model.getAttributes().length / (w / (pnlW + hGap)));
        
        int h = numRow * pnlH + numRow * vGap + 20;

        
        pnlAttributes = new SheetPanel("Attributes", f24, false);
        pnlAttributes.setLayout(new FlowLayout(FlowLayout.CENTER, hGap, vGap));
        pnlAttributes.setBounds(x, y, w, h);
        pnlAttributes.setBackground(Color.BLACK);
        pnlMain.add(pnlAttributes);
        
        JLabel[] lblAttributes = new JLabel[model.getAttributes().length];
        for (int i = 0; i < lblAttributes.length; i++)
        {
            lblAttributes[i] = new JLabel(model.getAttributes()[i].getName());
            txtAttributes[i] = new JTextField(sheet.getAttributes()[i] + "");
            
            JPanel miniAttr = new JPanel(null, false);
            miniAttr.setLayout(new BoxLayout(miniAttr, BoxLayout.PAGE_AXIS));
            miniAttr.setPreferredSize(new Dimension(pnlW, pnlH));
            miniAttr.setBackground(Color.BLACK);
            
            chkAttributeMarked[i] = new JCheckBox("", sheet.getAttributeMarked()[i]);
            chkAttributeMarked[i].setAlignmentX(CENTER_ALIGNMENT);
            chkAttributeMarked[i].setBackground(Color.BLACK);
            final int ii = i;
            chkAttributeMarked[i].addActionListener(l ->
            {
                boolean val = chkAttributeMarked[ii].isSelected();
                sheet.getAttributeMarked()[ii] = val;
                sendSheetUpdate(UpdateField.MARK_ATTRIBUTES, val, ii, UpdateType.UPDATE);
            });
            
            lblAttributes[i].setFont(f12);
            lblAttributes[i].setBounds(0, 0, getStringWidth(lblAttributes[i].getFont(), lblAttributes[i].getText()), lblAttributes[i].getFont().getSize());
            lblAttributes[i].setForeground(Color.WHITE);
            lblAttributes[i].setAlignmentX(CENTER_ALIGNMENT);
            lblAttributes[i].addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    try
                    {
                        int val = Integer.parseInt(txtAttributes[ii].getText());
                        showRollPopup(rand.nextInt(model.getAttributes()[ii].getDiceNum() + 1), val);
                    }
                    catch (NumberFormatException ex)
                    {
                        JOptionPane.showMessageDialog(null, "To roll a dice, please type in a number in the selected attribute.");
                    }
                }
            });
            
            txtAttributes[i].setBackground(Color.BLACK);
            txtAttributes[i].setForeground(Color.WHITE);
            txtAttributes[i].setBorder(new MatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
            txtAttributes[i].setAlignmentX(CENTER_ALIGNMENT);
            txtAttributes[i].setHorizontalAlignment(SwingConstants.CENTER);
            txtAttributes[i].setFont(f);
            txtAttributes[i].addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyPressed(KeyEvent e)
                {
                    localChange = true;
                }
            });
            txtAttributes[i].getDocument().addDocumentListener(new SheetDocumentListener()
            {
                @Override
                public void update()
                {
                    if (!localChange)
                        return;
                    
//                    sheet.getAttributes()[ii] = txtAttributes[ii].getText();
                    
                    sendSheetUpdate(UpdateField.ATTRIBUTES, txtAttributes[ii].getText(), ii, UpdateType.UPDATE);
                }
            });
            
            miniAttr.add(chkAttributeMarked[i]);
            miniAttr.add(lblAttributes[i]);
            miniAttr.add(txtAttributes[i]);
            
            pnlAttributes.add(miniAttr);
        }
    }
    
    private void initSkillsPanel()
    {
        int x = pnlAttributes.getX();
        int y = pnlAttributes.getY() + pnlAttributes.getHeight() + 50;
        int w = pnlAttributes.getWidth();
        
        int sklW = 125;
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
            lblSkills[i] = new JLabel(model.getSkills()[i].getName());
            txtSkills[i] = new JTextField(sheet.getSkills()[i] + "");
            
            JPanel miniSkl = new JPanel(null, false);
            miniSkl.setLayout(new BoxLayout(miniSkl, BoxLayout.PAGE_AXIS));
            miniSkl.setPreferredSize(new Dimension(sklW, sklH));
            miniSkl.setBackground(Color.BLACK);
            
            chkSkillMarked[i] = new JCheckBox("", sheet.getSkillMarked()[i]);
            chkSkillMarked[i].setAlignmentX(CENTER_ALIGNMENT);
            chkSkillMarked[i].setBackground(Color.BLACK);
            final int ii = i;
            chkSkillMarked[i].addActionListener(l ->
            {
                boolean val = chkSkillMarked[ii].isSelected();
                sheet.getSkillMarked()[ii] = val;
                sendSheetUpdate(UpdateField.MARK_SKILLS, val, ii, UpdateType.UPDATE);
            });
            
            lblSkills[i].setFont(f12);
            lblSkills[i].setBounds(0, 0, getStringWidth(lblSkills[i].getFont(), lblSkills[i].getText()), lblSkills[i].getFont().getSize());
            lblSkills[i].setForeground(Color.WHITE);
            lblSkills[i].setAlignmentX(CENTER_ALIGNMENT);
            lblSkills[i].addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    try
                    {
                        int val = Integer.parseInt(txtSkills[ii].getText());
                        showRollPopup(rand.nextInt(model.getSkills()[ii].getDiceNum() + 1), val);
                    }
                    catch (NumberFormatException ex)
                    {
                        JOptionPane.showMessageDialog(null, "To roll a dice, please type in a number in the selected attribute.");
                    }
                }
            });
            
            txtSkills[i].setBackground(Color.BLACK);
            txtSkills[i].setForeground(Color.WHITE);
            txtSkills[i].setBorder(new MatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
            txtSkills[i].setAlignmentX(CENTER_ALIGNMENT);
            txtSkills[i].setHorizontalAlignment(SwingConstants.CENTER);
            txtSkills[i].setFont(f);
            txtSkills[i].addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyPressed(KeyEvent e)
                {
                    localChange = true;
                }
            });
            txtSkills[i].getDocument().addDocumentListener(new SheetDocumentListener()
            {
                @Override
                public void update()
                {
                    if (!localChange)
                        return;
                    
//                    sheet.getSkills()[ii] = txtSkills[ii].getText();
                    
                    sendSheetUpdate(UpdateField.SKILLS, txtSkills[ii].getText(),ii, UpdateType.UPDATE);
                }
            });
            
            miniSkl.add(chkSkillMarked[i]);
            miniSkl.add(lblSkills[i]);
            miniSkl.add(txtSkills[i]);
            
            pnlSkills.add(miniSkl);
        }
    }
    
    private void initEquipmentsPanel()
    {
        int x = scrSkills.getX();
        int y = scrSkills.getY() + scrSkills.getHeight() + 50;
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
        
        JLabel[] desc = new JLabel[model.getEquipmentDescriptions().length];
        equipmentPositions = new int[desc.length];
        x = 60;
        for (int i = 0; i < desc.length; i++)
        {
            desc[i] = new JLabel(model.getEquipmentDescriptions()[i]);
            desc[i].setForeground(Color.WHITE);
            desc[i].setFont(f);
            desc[i].setBounds(x, 0, getStringWidth(desc[i].getFont(), desc[i].getText()), desc[i].getFont().getSize());
            equipmentPositions[i] = x;
            pnlDescriptions.add(desc[i]);
            x += desc[i].getWidth() + 50;
        }
        
        btnAddEquipment.setBackground(Color.BLACK);
        btnAddEquipment.setForeground(Color.WHITE);
        btnAddEquipment.setBounds(1020, 10, 50, 25);
        btnAddEquipment.addActionListener(l ->
        {
            String[] obj = new String[model.getEquipmentDescriptions().length];
            for (int i = 0; i < obj.length; i++)
                obj[i] = "none";
            obj[0] = "Equipment " + (equipments.size() + 1);
            JField e = createEquipmentField(obj);
            sheet.getEquipments().add(obj);
            sendSheetUpdate(UpdateField.EQUIPMENTS, obj, equipments.indexOf(e), UpdateType.ADD);
        });
        pnlEquipments.add(btnAddEquipment);
        
        for (int i = 0; i < sheet.getEquipments().size(); i++)
            createEquipmentField(sheet.getEquipments().get(i));
    }
    
    private void initItemsPanel()
    {
        int x = scrEquipments.getX();
        int y = scrEquipments.getY() + scrEquipments.getHeight() + 50;
        int w = scrEquipments.getWidth();
        int h = 200;
        
        pnlItems = new SheetPanel("Items", f24, false);
        pnlItems.setBackground(Color.BLACK);
        pnlItems.setLayout(null);
        
        scrItems = new SheetScrollPane(pnlItems, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrItems.getVerticalScrollBar().setUnitIncrement(40);
        scrItems.setBounds(x, y, w, h);
        pnlMain.add(scrItems);
        
        JPanel pnlDescriptions = new JPanel(null, false);
        pnlDescriptions.setBounds(0, 50, scrItems.getWidth(), f.getSize());
        pnlDescriptions.setBackground(Color.BLACK);
        pnlItems.add(pnlDescriptions);
        
        x = pnlDescriptions.getX();
        y = pnlDescriptions.getY() + pnlDescriptions.getHeight() + 10;
        w = pnlDescriptions.getWidth();
        h = f12.getSize() * sheet.getItems().size() + 10;
        
        pnlItemFields = new JPanel(null, false);
        pnlItemFields.setLayout(new BoxLayout(pnlItemFields, BoxLayout.PAGE_AXIS));
        pnlItemFields.setBounds(x, y, w, h);
        pnlItemFields.setBackground(Color.BLACK);
        
        pnlItems.add(pnlItemFields);
        pnlItems.setPreferredSize(new Dimension(scrEquipments.getWidth(), pnlItemFields.getY() + pnlItemFields.getHeight() + 20));
        
        pnlItemFields.setPreferredSize(new Dimension(scrItems.getWidth(), pnlItemFields.getY() + pnlItemFields.getHeight() + 20));
        
        JLabel[] desc = new JLabel[model.getItemDescriptions().length];
        itemPositions = new int[desc.length];
        x = 60;
        for (int i = 0; i < desc.length; i++)
        {
            desc[i] = new JLabel(model.getItemDescriptions()[i]);
            desc[i].setForeground(Color.WHITE);
            desc[i].setFont(f);
            desc[i].setBounds(x, 0, getStringWidth(desc[i].getFont(), desc[i].getText()), desc[i].getFont().getSize());
            itemPositions[i] = x;
            pnlDescriptions.add(desc[i]);
            x += desc[i].getWidth() + 50;
        }
        
        btnAddItem.setBackground(Color.BLACK);
        btnAddItem.setForeground(Color.WHITE);
        btnAddItem.setBounds(1020, 10, 50, 25);
        btnAddItem.addActionListener(l ->
        {
            String[] obj = new String[model.getItemDescriptions().length];
            for (int i = 0; i < obj.length; i++)
                obj[i] = "none";
            obj[0] = "Item " + (items.size() + 1);
            createItem(obj);
            sheet.getItems().add(obj);
            sendSheetUpdate(UpdateField.ITEMS, obj, 0, UpdateType.ADD);
        });
        pnlItems.add(btnAddItem);
        
        for (int i = 0; i < sheet.getItems().size(); i++)
            createItem(sheet.getItems().get(i));
    }
    
    private JField createEquipmentField(String[] equip)
    {
        
        
        
        
////        JField e = new JField(equip);
////        e.setPositions(equipmentPositions);
////        e.setFont(f12);
//        e.setForeground(Color.WHITE);
//        e.addMouseListener(new MouseAdapter()
//        {
//            @Override
//            public void mousePressed(MouseEvent ev)
//            {
//                if (ev.getClickCount() < 2)
//                    return;
//                int selected = -1;
//                for (int i = 0; i < equipmentPositions.length; i++)
//                {
//                    int x = ev.getX();
//                    if (i < equipmentPositions.length - 1)
//                    {
//                        if (x > equipmentPositions[i] && x < equipmentPositions[i + 1])
//                        {
//                            selected = i;
//                        }
//                    }
//                    else if (x > equipmentPositions[i])
//                    {
//                        selected = i;
//                    }
//                }
//                
//                if (selected < 0)
//                    return;
//                
//                String a = JOptionPane.showInputDialog("Please type in new value.", e.getProperties()[selected]);
//                
//                if (a == null)
//                    return;
//                
//                e.getProperties()[selected] = a;
//                
//                int index = equipments.indexOf(e);
//                
//                sheet.getEquipments().set(index, e.getProperties());
//                
//                sendSheetUpdate(UpdateField.EQUIPMENTS, e.getProperties(), index, UpdateType.UPDATE);
//                
//                repaint();
//            }
//        });
//        
//        equipments.add(e);
//        pnlEquipmentFields.add(e);
//        int h = (f12.getSize() + 10) * equipments.size();
//        e.getRemoveButton().addActionListener(l ->
//        {
//            sendSheetUpdate(UpdateField.EQUIPMENTS, null, equipments.indexOf(e), UpdateType.REMOVE);
//            sheet.getEquipments().remove(e.getProperties());
//            removeEquipmentField(equipments.indexOf(e));
//        });
//        
//        pnlEquipmentFields.setBounds(pnlEquipmentFields.getX(), 
//                pnlEquipmentFields.getY(), 
//                pnlEquipmentFields.getWidth(), h);
//        pnlEquipments.setPreferredSize(new Dimension(pnlEquipments.getPreferredSize().width, pnlEquipmentFields.getY() + pnlEquipmentFields.getHeight() + 20));
//        scrEquipments.revalidate();
//        pnlEquipments.revalidate();
        return null;
    }
    
    private void removeEquipmentField(int index)
    {
//        equipments.remove(index);
//        pnlEquipmentFields.remove(index);
//        
//        int hh = (f12.getSize() + 10) * equipments.size();
//        pnlEquipmentFields.setBounds(pnlEquipmentFields.getX(), 
//            pnlEquipmentFields.getY(), 
//            pnlEquipmentFields.getWidth(), hh);
//
//        pnlEquipments.setPreferredSize(new Dimension(pnlEquipments.getPreferredSize().width, pnlEquipmentFields.getY() + pnlEquipmentFields.getHeight() + 20));
//
//        scrEquipments.revalidate();
//        pnlEquipments.revalidate();
    }
    
    private JField createItem(String[] item)
    {
//        JField it = new JField(item);
//        it.setPositions(itemPositions);
//        it.setFont(f12);
//        it.setForeground(Color.WHITE);
//        
//        it.addMouseListener(new MouseAdapter()
//        {
//            @Override
//            public void mousePressed(MouseEvent ev)
//            {
//                if (ev.getClickCount() < 2)
//                    return;
//                int selected = -1;
//                for (int i = 0; i < itemPositions.length; i++)
//                {
//                    int x = ev.getX();
//                    if (i < itemPositions.length - 1)
//                    {
//                        if (x > itemPositions[i] && x < itemPositions[i + 1])
//                        {
//                            selected = i;
//                        }
//                    }
//                    else if (x > itemPositions[i])
//                    {
//                        selected = i;
//                    }
//                }
//                
//                if (selected < 0)
//                    return;
//                
//                String a = JOptionPane.showInputDialog("Please type in new value.", it.getProperties()[selected]);
//                
//                if (a == null)
//                    return;
//                
//                it.getProperties()[selected] = a;
//                
//                int index = items.indexOf(it);
//                
//                sheet.getItems().set(index, it.getProperties());
//                
//                sendSheetUpdate(UpdateField.ITEMS, it.getProperties(), index, UpdateType.UPDATE);
//                
//                repaint();
//            }
//        });
//        
//        items.add(it);
//        pnlItemFields.add(it);
//        int h = (f12.getSize() + 10) * items.size();
//        it.getRemoveButton().addActionListener(l ->
//        {
//            int index = items.indexOf(it);
//            sendSheetUpdate(UpdateField.ITEMS, null, index, UpdateType.REMOVE);
//            sheet.getItems().remove(index);
//            removeItem(index);
//        });
//        
//        pnlItemFields.setBounds(pnlItemFields.getX(), 
//                pnlItemFields.getY(), 
//                pnlItemFields.getWidth(), h);
//        pnlItems.setPreferredSize(new Dimension(pnlItems.getPreferredSize().width, pnlItemFields.getY() + pnlItemFields.getHeight() + 20));
//        scrItems.revalidate();
//        pnlItems.revalidate();
//        return it;
        return null;
    }
    
    private void removeItem(int index)
    {
        items.remove(index);
        pnlItemFields.remove(index);
        
        int hh = (f12.getSize() + 10) * items.size();
        pnlItemFields.setBounds(pnlItemFields.getX(), 
            pnlItemFields.getY(), 
            pnlItemFields.getWidth(), hh);

        pnlItems.setPreferredSize(new Dimension(pnlItems.getPreferredSize().width, pnlItemFields.getY() + pnlItemFields.getHeight() + 20));

        scrItems.revalidate();
        pnlItems.revalidate();
    }
    
    private void initRollPanel()
    {
        lblDiceAnimation = new JLabel(new ImageIcon(FileManager.app_dir + "data files\\objects\\dice_roll.gif"));
        lblDiceAnimation.setAlignmentX(CENTER_ALIGNMENT);
        lblDiceAnimation.setAlignmentY(CENTER_ALIGNMENT);
        pnlRoll = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                String desc;
                
                if (diceNumber <= fieldNumber * 0.2f)
                {
                    desc = "Extreme";
                    g.setColor(new Color(0xFFD700).darker());
                }
                else if (diceNumber <= fieldNumber * 0.5f)
                {
                    desc = "Good";
                    g.setColor(Color.GREEN.darker());
                }
                else if (diceNumber <= fieldNumber)
                {
                    desc = "Success";
                    g.setColor(Color.BLACK);
                }
                else
                {
                    desc = "Fail";
                    g.setColor(Color.RED.darker());
                }
                
                g.setFont(f);
                g.drawString(Integer.toString(diceNumber) + " - " + desc, 
                        40, 
                        20);
            }
        };
        pnlRoll.add(lblDiceAnimation);
    }
    
    private int getStringWidth(Font font, String text)
    {
        return getFontMetrics(font).stringWidth(text);
    }
    
    private abstract class SheetDocumentListener implements DocumentListener
    {
        @Override
        public void insertUpdate(DocumentEvent e)
        {
            update();
        }

        @Override
        public void removeUpdate(DocumentEvent e)
        {
            update();
        }

        @Override
        public void changedUpdate(DocumentEvent e)
        {
            update();
        }
        
        public abstract void update();
    }
    
    public abstract void sendSheetUpdate(UpdateField field, Object newValue, int propertyIndex, UpdateType type);
    
    public void showRollPopup(int diceNumber, int fieldNumber)
    {
        this.diceNumber = diceNumber;
        this.fieldNumber = fieldNumber;
        JOptionPane.showMessageDialog(this, pnlRoll, "Dice Result", JOptionPane.PLAIN_MESSAGE, new ImageIcon(FileManager.app_dir + "data files\\objects\\diceicon.png"));
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
