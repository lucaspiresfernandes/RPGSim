package com.rpgsim.common.sheets.graphics;

import com.rpgsim.common.serverpackages.UpdateType;
import com.rpgsim.common.sheets.PlayerSheet;
import com.rpgsim.common.sheets.SheetModel;
import com.rpgsim.common.sheets.UpdateField;
import java.awt.Color;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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

public abstract class SheetFrame extends javax.swing.JFrame
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
    private JPanel pnlEquipmentFields;
    
    private JTextField[] txtInfo;
    
    private JProgressBar[] prgStats;
    private JButton[] btnStatsUp;
    private JButton[] btnStatsDown;
    
    private JTextArea txaAbout, txaExtras;
    
    private JTextField[] txtAttributes;
    
    private JTextField[] txtSkills;
    
    private final ArrayList<JEquipmentField> equipments = new ArrayList<>();
    private final JButton btnAddEquipment = new JButton("+");
    
    private final ArrayList<JLabel> items = new ArrayList<>();
    private final JButton btnAddItem = new JButton("+");
    
    private boolean loaded = false;
    private boolean localChange = false;
    
    public SheetFrame()
    {
        initComponents();
        
        pnlMain = new JPanel(null, false);
        scrMain = new SheetScrollPane(pnlMain);
        scrMain.setBounds(0, 0, getWidth(), getHeight());
        getContentPane().add(scrMain);
    }
    
    public void load(int connectionID, SheetModel model, PlayerSheet sheet)
    {
        this.model = model;
        this.sheet = sheet;
        this.connectionID = connectionID;
        
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
    
    public void reload(int connectionID, PlayerSheet sheet)
    {
        this.sheet = sheet;
        this.connectionID = connectionID;
        
        for (int i = 0; i < txtInfo.length; i++)
            txtInfo[i].setText(sheet.getInfo()[i]);
        
        txaAbout.setText(sheet.getAbout());
        
        txaExtras.setText(sheet.getExtras());
        
        for (int i = 0; i < txtAttributes.length; i++)
            txtAttributes[i].setText(sheet.getAttributes()[i]);
        
        for (int i = 0; i < txtSkills.length; i++)
            txtSkills[i].setText(sheet.getSkills()[i]);
        
        for (int i = 0; i < equipments.size(); i++)
            removeEquipmentField(equipments.get(i));
        for (int i = 0; i < sheet.getEquipments().size(); i++)
            createEquipmentField(sheet.getEquipments().get(i));
        
        for (int i = 0; i < items.size(); i++)
            removeItem(i);
        for (int i = 0; i < sheet.getItems().size(); i++)
            createItem(sheet.getItems().get(i));
    }
    
    public void networkUpdate(UpdateField field, Object newValue, int propertyIndex)
    {
        localChange = false;
        switch (field)
        {
            case INFO:
                String info = (String) newValue;
                sheet.getInfo()[propertyIndex] = info;
                txtInfo[propertyIndex].setText(info);
                break;
            case ABOUT:
                sheet.setAbout((String) newValue);
                System.out.println(sheet.getAbout());
                txaAbout.setText((String) newValue);
                break;
            case EXTRAS:
                sheet.setExtras((String) newValue);
                txaExtras.setText((String) newValue);
                break;
            case CUR_STATS:
//                tring stats = updatedSheet.getCurrentStats()[propertyIndex];
//                sheet.getCurrentStats()[propertyIndex] = stats;
                break;
            case MAX_STATS:
    //            String stats = updatedSheet.getMaxStats()[propertyIndex];
    //            sheet.getMaxStats()[propertyIndex] = stats;
                break;
            case ATTRIBUTES:
                String attribute = (String) newValue;
                sheet.getAttributes()[propertyIndex] = attribute;
                txtAttributes[propertyIndex].setText(attribute);
                break;
            case SKILLS:
                String skill = (String) newValue;
                sheet.getSkills()[propertyIndex] = skill;
                txtSkills[propertyIndex].setText(skill);
                break;
            case EQUIPMENTS:
                String[] equip = (String[]) newValue;
                sheet.getEquipments().set(propertyIndex, equip);
                equipments.get(propertyIndex).setEquipment(equip);
                repaint();
                break;
            case ITEMS:
                String item = (String) newValue;
                items.get(propertyIndex).setText(item);
                sheet.getItems().set(propertyIndex, item);
                break;
            default:
                throw new AssertionError();
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
                String i = (String) newValue;
                sheet.getItems().add(i);
                createItem(i);
                break;
        }
    }
    
    public void networkRemove(UpdateField field, Object newValue, int propertyIndex)
    {
        switch (field)
        {
            case EQUIPMENTS:
                sheet.getEquipments().remove(propertyIndex);
                removeEquipmentField(equipments.get(propertyIndex));
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
            final int ii = i;
            txtInfo[i].addKeyListener(new KeyAdapter()
            {
                @Override
                public void keyPressed(KeyEvent e)
                {
                    localChange = true;
                }
            });
            txtInfo[i].getDocument().addDocumentListener(new SheetDocumentListener() {
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
            final int ii = i;
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
                    
                    sheet.getAttributes()[ii] = txtAttributes[ii].getText();
                    
                    sendSheetUpdate(UpdateField.ATTRIBUTES, txtAttributes[ii].getText(), ii, UpdateType.UPDATE);
                }
            });
            
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
            final int ii = i;
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
                    
                    sheet.getSkills()[ii] = txtSkills[ii].getText();
                    
                    sendSheetUpdate(UpdateField.SKILLS, txtSkills[ii].getText(),ii, UpdateType.UPDATE);
                }
            });
            
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
            String[] obj = new String[model.getEquipments().length];
            for (int i = 0; i < obj.length; i++)
                obj[i] = "none";
            obj[0] = "Equipment " + (equipments.size() + 1);
            JEquipmentField e = createEquipmentField(obj);
            sheet.getEquipments().add(obj);
            sendSheetUpdate(UpdateField.EQUIPMENTS, obj, equipments.indexOf(e), UpdateType.ADD);
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
        
        
        pnlItems = new SheetPanel("Items", f24, false);
        pnlItems.setPreferredSize(new Dimension(scrSkills.getWidth(), 2000));
        pnlItems.setBackground(Color.BLACK);
        pnlItems.setLayout(new FlowLayout(FlowLayout.LEADING, 50, 50));
        
        scrItems = new SheetScrollPane(pnlItems, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrItems.getVerticalScrollBar().setUnitIncrement(40);
        scrItems.setBounds(x, y, w, h);
        pnlMain.add(scrItems);
        
        btnAddItem.setBackground(Color.BLACK);
        btnAddItem.setForeground(Color.WHITE);
        btnAddItem.setPreferredSize(new Dimension(75, 25));
        btnAddItem.addActionListener(l ->
        {
            String i = "New Item " + (sheet.getItems().size() + 1);
            createItem(i);
            sheet.getItems().add(i);
            sendSheetUpdate(UpdateField.ITEMS, i, 0, UpdateType.ADD);
        });
        pnlItems.add(btnAddItem);
        
        for (int i = 0; i < sheet.getItems().size(); i++)
            createItem(sheet.getItems().get(i));
    }
    
    private JEquipmentField createEquipmentField(String[] equip)
    {
        JEquipmentField e = new JEquipmentField(equip);
        e.setFont(f12);
        e.setForeground(Color.WHITE);
        e.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent ev)
            {
                if (ev.getClickCount() < 2)
                    return;
                int selected = -1;
                for (int i = 0; i < JEquipmentField.getPositions().length; i++)
                {
                    int x = ev.getX();
                    if (i < JEquipmentField.getPositions().length - 1)
                    {
                        if (x > JEquipmentField.getPositions()[i] && x < JEquipmentField.getPositions()[i + 1])
                        {
                            selected = i;
                        }
                    }
                    else if (x > JEquipmentField.getPositions()[i])
                    {
                        selected = i;
                    }
                }
                
                if (selected < 0)
                    return;
                
                String a = JOptionPane.showInputDialog("Please type in new value.", e.getEquipment()[selected]);
                
                if (a == null)
                    return;
                
                e.getEquipment()[selected] = a;
                
                int index = equipments.indexOf(e);
                
                sheet.getEquipments().set(index, e.getEquipment());
                
                sendSheetUpdate(UpdateField.EQUIPMENTS, e.getEquipment(), index, UpdateType.UPDATE);
                
                repaint();
            }
        });
        
        equipments.add(e);
        pnlEquipmentFields.add(e);
        int h = (f12.getSize() + 10) * equipments.size();
        e.getRemoveButton().addActionListener(l ->
        {
            sendSheetUpdate(UpdateField.EQUIPMENTS, null, equipments.indexOf(e), UpdateType.REMOVE);
            sheet.getEquipments().remove(e.getEquipment());
            removeEquipmentField(e);
        });
        
        pnlEquipmentFields.setBounds(pnlEquipmentFields.getX(), 
                pnlEquipmentFields.getY(), 
                pnlEquipmentFields.getWidth(), h);
        pnlEquipments.setPreferredSize(new Dimension(getPreferredSize().width, pnlEquipmentFields.getY() + pnlEquipmentFields.getHeight() + 20));
        scrEquipments.revalidate();
        pnlEquipments.revalidate();
        return e;
    }
    
    private void removeEquipmentField(JEquipmentField equip)
    {
        equipments.remove(equip);
        pnlEquipmentFields.remove(equip);
        
        int hh = (f12.getSize() + 10) * equipments.size();
        pnlEquipmentFields.setBounds(pnlEquipmentFields.getX(), 
            pnlEquipmentFields.getY(), 
            pnlEquipmentFields.getWidth(), hh);

        pnlEquipments.setPreferredSize(new Dimension(pnlEquipments.getPreferredSize().width, pnlEquipmentFields.getY() + pnlEquipmentFields.getHeight() + 20));

        scrEquipments.revalidate();
        pnlEquipments.revalidate();
    }
    
    private JLabel createItem(String name)
    {
        FlowLayout l = (FlowLayout) pnlItems.getLayout();
        int hGap = l.getHgap();
        int vGap = l.getVgap();
        
        JLabel item = new JLabel(name);
        item.setFont(f);
        item.setForeground(Color.WHITE);
        item.setPreferredSize(new Dimension(getStringWidth(item.getFont(), name), item.getFont().getSize()));
        
        items.add(item);
        pnlItems.add(item);
        int numRow = (int) Math.ceil(items.size() / (scrItems.getWidth() / (item.getPreferredSize().width + hGap)));
        int h = numRow * item.getPreferredSize().height + numRow * vGap + 20;
        pnlItems.setPreferredSize(new Dimension(pnlItems.getPreferredSize().width, h));
        item.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON3)
                {
                    sheet.getItems().remove(name);
                    sendSheetUpdate(UpdateField.ITEMS, null, items.indexOf(item), UpdateType.REMOVE);
                    removeItem(items.indexOf(item));
                }
                else
                {
                    if (e.getClickCount() == 2)
                    {
                        String newVal = JOptionPane.showInputDialog("Please type in new value.", item.getText());
                        
                        if (newVal == null)
                            return;
                        
                        sheet.getItems().set(sheet.getItems().indexOf(item.getText()), newVal);
                        
                        item.setText(newVal);
                        item.setPreferredSize(new Dimension(getStringWidth(item.getFont(), item.getText()), item.getPreferredSize().height));
                        sendSheetUpdate(UpdateField.ITEMS, newVal, items.indexOf(item), UpdateType.UPDATE);
                    }
                }
            }
        });
        
        pnlItems.revalidate();
        pnlItems.repaint();
        scrItems.revalidate();
        return item;
    }
    
    private void removeItem(int index)
    {
        FlowLayout l = (FlowLayout) pnlItems.getLayout();
        int hGap = l.getHgap();
        int vGap = l.getVgap();
        JLabel item = items.get(index);
        
        items.remove(item);
        pnlItems.remove(item);
        int numRow = (int) Math.ceil(items.size() / (scrItems.getWidth() / (item.getPreferredSize().width + hGap)));
        int h = numRow * item.getPreferredSize().height + numRow * vGap + 20;
        pnlItems.setPreferredSize(new Dimension(pnlItems.getPreferredSize().width, h));
        pnlItems.revalidate();
        pnlItems.repaint();
        scrItems.revalidate();
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
