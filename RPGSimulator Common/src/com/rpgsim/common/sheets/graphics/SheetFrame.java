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
import java.security.SecureRandom;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
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
    
    private final Font f = new Font("Times New Roman", Font.PLAIN, 16);
    private final Font f14 = f.deriveFont(14f);
    
    private JTextField[] txtInfo;
    
    private JLabel avatarImage;
    private boolean loadImage;
    private JProgressBar[] prgStats;
    private JButton[] btnStatsUp;
    private JButton[] btnStatsDown;
    
    private JTextField[] txtAttributes;
    private JCheckBox[] chkAttributeMarked;
    
    private JTextField[] txtSkills;
    private JCheckBox[] chkSkillMarked;
    
    private final ArrayList<JField> equipments = new ArrayList<>();
    private final JButton btnAddEquipment = new JButton("+");
    
    private final ArrayList<JField> items = new ArrayList<>();
    private final JButton btnAddItem = new JButton("+");
    
    private boolean localChange = false;
    
    private final SecureRandom rand = new SecureRandom();
    
    public SheetFrame()
    {
        initComponents();
        scrMain.getVerticalScrollBar().setUnitIncrement(30);
    }
    
    public void reload(int connectionID, PlayerSheet sheet)
    {
        this.sheet = sheet;
        this.connectionID = connectionID;
        
        for (int i = 0; i < txtInfo.length; i++)
            txtInfo[i].setText(sheet.getInfo()[i]);
        
        for (int i = 0; i < prgStats.length; i++)
        {
            prgStats[i].setValue(sheet.getCurrentStats()[i]);
            prgStats[i].setMaximum(sheet.getMaxStats()[i]);
            prgStats[i].setString(sheet.getCurrentStats()[i] + "/" + sheet.getMaxStats()[i]);
        }
        
        txaAbout.setText(sheet.getAbout());
        
        txaExtras.setText(sheet.getExtras());
        
        for (int i = 0; i < txtAttributes.length; i++)
            txtAttributes[i].setText(Integer.toString(sheet.getAttributes()[i]));
        for (int i = 0; i < chkAttributeMarked.length; i++)
            chkAttributeMarked[i].setSelected(sheet.getAttributeMarked()[i]);
        
        for (int i = 0; i < txtSkills.length; i++)
            txtSkills[i].setText(Integer.toString(sheet.getSkills()[i]));
        for (int i = 0; i < chkSkillMarked.length; i++)
            chkSkillMarked[i].setSelected(sheet.getSkillMarked()[i]);
        
        for (int i = 0; i < equipments.size(); i++)
            removeEquipmentField(i);
        for (int i = 0; i < sheet.getEquipments().size(); i++)
            createEquipmentField(sheet.getEquipments().get(i));
        
        for (int i = 0; i < items.size(); i++)
            removeItem(i);
        for (int i = 0; i < sheet.getItems().size(); i++)
            createItemField(sheet.getItems().get(i));
    }
    
    public void load(int connectionID, SheetModel model, PlayerSheet sheet, boolean loadImage)
    {
        this.connectionID = connectionID;
        this.model = model;
        this.sheet = sheet;
        this.loadImage = loadImage;
        
        txtInfo = new JTextField[sheet.getInfo().length];
        txtAttributes = new JTextField[sheet.getAttributes().length];
        chkAttributeMarked = new JCheckBox[sheet.getAttributeMarked().length];
        txtSkills = new JTextField[sheet.getSkills().length];
        chkSkillMarked = new JCheckBox[sheet.getSkillMarked().length];
        prgStats = new JProgressBar[sheet.getCurrentStats().length];
        btnStatsUp = new JButton[prgStats.length];
        btnStatsDown = new JButton[prgStats.length];
        
        loadInfo();
        loadStats();
        loadAbout();
        loadExtras();
        loadAttributes();
        loadSkills();
        loadEquipments();
        loadItems();
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
                int intAux = (int) newValue;
                sheet.getCurrentStats()[propertyIndex] = intAux;
                prgStats[propertyIndex].setValue(intAux);
                prgStats[propertyIndex].setString(sheet.getCurrentStats()[propertyIndex] + "/" + sheet.getMaxStats()[propertyIndex]);
                break;
            case MAX_STATS:
                intAux = (int) newValue;
                
                sheet.getMaxStats()[propertyIndex] = intAux;
                
                if (intAux < prgStats[propertyIndex].getValue())
                    sheet.getCurrentStats()[propertyIndex] = intAux;
                
                prgStats[propertyIndex].setMaximum(intAux);
                
                prgStats[propertyIndex].setString(sheet.getCurrentStats()[propertyIndex] + "/" + sheet.getMaxStats()[propertyIndex]);
                break;
            case ATTRIBUTES:
                intAux = (int) newValue;
                sheet.getAttributes()[propertyIndex] = intAux;
                txtAttributes[propertyIndex].setText(Integer.toString(intAux));
                break;
            case MARK_ATTRIBUTES:
                boolean bAux = (boolean) newValue;
                sheet.getAttributeMarked()[propertyIndex] = bAux;
                chkAttributeMarked[propertyIndex].setSelected(bAux);
                break;
            case SKILLS:
                intAux = (int) newValue;
                sheet.getSkills()[propertyIndex] = intAux;
                txtSkills[propertyIndex].setText(Integer.toString(intAux));
                break;
            case MARK_SKILLS:
                bAux = (boolean) newValue;
                sheet.getSkillMarked()[propertyIndex] = bAux;
                chkSkillMarked[propertyIndex].setSelected(bAux);
                break;
            case EQUIPMENTS:
                String[] str = (String[]) newValue;
                sheet.getEquipments().set(propertyIndex, str);
                JLabel[] lbl = equipments.get(propertyIndex).getProperties();
                for (int i = 0; i < lbl.length; i++)
                    lbl[i].setText(str[i]);
                repaint();
                break;
            case ITEMS:
                str = (String[]) newValue;
                sheet.getItems().set(propertyIndex, str);
                lbl = items.get(propertyIndex).getProperties();
                for (int i = 0; i < lbl.length; i++)
                    lbl[i].setText(str[i]);
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
                createItemField(e);
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
    
    private void loadInfo()
    {
        int w = pnlInfoContent.getWidth();
        int infoH = 0;
        scrInfo.getVerticalScrollBar().setUnitIncrement(40);
        
        for (int i = 0; i < txtInfo.length; i++)
        {
            JPanel content = new JPanel(null, false);
            content.setBackground(Color.BLACK);
            JLabel lblInfoDesc = new JLabel(model.getInfo()[i]);
            lblInfoDesc.setFont(f);
            lblInfoDesc.setBounds(0, 0, getStringWidth(lblInfoDesc.getFont(), lblInfoDesc.getText()), lblInfoDesc.getFont().getSize());
            lblInfoDesc.setForeground(Color.WHITE);
            
            txtInfo[i] = new JTextField(sheet.getInfo()[i]);
            txtInfo[i].setBounds(0, lblInfoDesc.getHeight() + 5, w, 20);
            txtInfo[i].setBackground(Color.BLACK);
            txtInfo[i].setForeground(Color.WHITE);
            txtInfo[i].setBorder(new MatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
            
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
            
            int h = txtInfo[i].getY() + txtInfo[i].getHeight();
            infoH += h;
            content.setPreferredSize(new Dimension(w, h));
            content.add(lblInfoDesc);
            content.add(txtInfo[i]);
            pnlInfoContent.add(content);
        }
        pnlInfo.setPreferredSize(new Dimension(pnlInfo.getPreferredSize().width, infoH + 50));
    }
    
    private void loadStats()
    {
        if (loadImage)
        {
            int iconW = 150;
            int iconH = 150;
            ImageIcon icon = new ImageIcon(FileManager.app_dir + sheet.getAvatarRelativePath());
            Image aux = icon.getImage().getScaledInstance(iconW, iconH, Image.SCALE_SMOOTH);
            icon.setImage(aux);
            avatarImage = new JLabel(icon);
            avatarImage.setPreferredSize(new Dimension(iconW, iconH));
            pnlStatsContent.add(avatarImage);
        }
        
        for (int i = 0; i < prgStats.length; i++)
        {
            JPanel content = new JPanel(null, false);
            content.setBackground(Color.BLACK);
            
            JLabel desc = new JLabel(model.getStats()[i].getName());
            desc.setFont(f14);
            int lblH = desc.getFont().getSize();
            desc.setForeground(Color.WHITE);
            desc.setBounds(0, 0, getStringWidth(desc.getFont(), desc.getText()), lblH);
            
            prgStats[i] = new JProgressBar(0, sheet.getMaxStats()[i]);
            prgStats[i].setValue(sheet.getCurrentStats()[i]);
            prgStats[i].setBounds(0, desc.getHeight() + 5, pnlStatsContent.getWidth(), 25);
            prgStats[i].setBackground(Color.BLACK);
            prgStats[i].setForeground(new Color(model.getStats()[i].getColor()));
            prgStats[i].setStringPainted(true);
            prgStats[i].setString(sheet.getCurrentStats()[i] + "/" + sheet.getMaxStats()[i]);
            final int ii = i;
            prgStats[i].addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    if (e.getClickCount() >= 2)
                    {
                        String value = JOptionPane.showInputDialog("Type in the new maximum value to this field.", prgStats[ii].getMaximum());
                        
                        if (value == null || value.isEmpty())
                            return;
                        
                        try
                        {
                            int maxVal = Integer.parseInt(value);
                            
                            if (maxVal <= 0)
                                return;
                            
                            int curVal = prgStats[ii].getValue();
                            prgStats[ii].setMaximum(maxVal);
                            sheet.getMaxStats()[ii] = Integer.parseInt(value);
                            
                            if (maxVal < curVal)
                                sheet.getCurrentStats()[ii] = maxVal;
                            
                            prgStats[ii].setString(sheet.getCurrentStats()[ii] + "/" + sheet.getMaxStats()[ii]);
                            sendSheetUpdate(UpdateField.MAX_STATS, maxVal, ii, UpdateType.UPDATE);
                        }
                        catch (NumberFormatException ex)
                        {
                            JOptionPane.showMessageDialog(null, "Only numbers are allowed!");
                        }
                        
                    }
                }
            });
            
            btnStatsUp[i] = new JButton("+");
            btnStatsUp[i].setBounds(0, 
                    prgStats[i].getY() + prgStats[i].getHeight() + 10, 100, 20);
            btnStatsUp[i].setBackground(Color.BLACK);
            btnStatsUp[i].setForeground(Color.WHITE);
            btnStatsUp[i].addActionListener(l -> 
            {
                int val = prgStats[ii].getValue();
                int maxVal = prgStats[ii].getMaximum();
                
                if (++val > maxVal)
                    return;
                
                prgStats[ii].setValue(val);
                sheet.getCurrentStats()[ii] = val;
                prgStats[ii].setString(val + "/" + sheet.getMaxStats()[ii]);
                sendSheetUpdate(UpdateField.CUR_STATS, val, ii, UpdateType.UPDATE);
            });
            
            btnStatsDown[i] = new JButton("-");
            btnStatsDown[i].setBackground(Color.BLACK);
            btnStatsDown[i].setForeground(Color.WHITE);
            btnStatsDown[i].setBounds(prgStats[i].getX() + prgStats[i].getWidth()- 100, 
                    prgStats[i].getY() + prgStats[i].getHeight() + 10, 100, 20);
            btnStatsDown[i].addActionListener(l -> 
            {
                int val = prgStats[ii].getValue();
                
                if (--val < 0)
                    return;
                
                prgStats[ii].setValue(val);
                sheet.getCurrentStats()[ii] = val;
                prgStats[ii].setString(val + "/" + sheet.getMaxStats()[ii]);
                sendSheetUpdate(UpdateField.CUR_STATS, val, ii, UpdateType.UPDATE);
            });
            
            content.add(desc);
            content.add(prgStats[i]);
            content.add(btnStatsUp[i]);
            content.add(btnStatsDown[i]);
            
            pnlStatsContent.add(content);
        }
        
        
    }
    
    private void loadAbout()
    {
        //TODO: String serialization too large problem.
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
    }
    
    private void loadExtras()
    {
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
    }
    
    private void loadAttributes()
    {
        scrAttributes.getVerticalScrollBar().setUnitIncrement(30);
        
        FlowLayout layout = (FlowLayout) pnlAttributesContent.getLayout();
        
        int pnlW = 175;
        int pnlH = 75;
        int hGap = layout.getHgap();
        int vGap = layout.getVgap();
        int numRow = (int) Math.ceil(model.getAttributes().length / (pnlAttributesContent.getWidth() / (pnlW + hGap)));
        
        int height = numRow * pnlH + numRow * vGap + 20;
        
        pnlAttributesContent.setSize(pnlAttributesContent.getWidth(), height);
        pnlAttributes.setPreferredSize(new Dimension(pnlAttributes.getPreferredSize().width, pnlAttributes.getPreferredSize().height + height));
        
        for (int i = 0; i < model.getAttributes().length; i++)
        {
            JLabel lblAttributeDesc = new JLabel(model.getAttributes()[i].getName());
            txtAttributes[i] = new JTextField(Integer.toString(sheet.getAttributes()[i]));
            
            JPanel content = new JPanel(null, false);
            content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
            content.setPreferredSize(new Dimension(pnlW, pnlH));
            content.setBackground(Color.BLACK);
            final int ii = i;
            
            chkAttributeMarked[i] = new JCheckBox("", sheet.getAttributeMarked()[i]);
            chkAttributeMarked[i].setAlignmentX(CENTER_ALIGNMENT);
            chkAttributeMarked[i].setBackground(Color.BLACK);
            chkAttributeMarked[i].addActionListener(l ->
            {
                boolean val = chkAttributeMarked[ii].isSelected();
                sheet.getAttributeMarked()[ii] = val;
                sendSheetUpdate(UpdateField.MARK_ATTRIBUTES, val, ii, UpdateType.UPDATE);
            });
            
            lblAttributeDesc.setFont(f14);
            lblAttributeDesc.setBounds(0, 0, getStringWidth(lblAttributeDesc.getFont(), lblAttributeDesc.getText()), lblAttributeDesc.getFont().getSize());
            lblAttributeDesc.setForeground(Color.WHITE);
            lblAttributeDesc.setAlignmentX(CENTER_ALIGNMENT);
            lblAttributeDesc.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    try
                    {
                        int dice = rand.nextInt(model.getAttributes()[ii].getDiceNum() + 1);
                        
                        int val = Integer.parseInt(txtAttributes[ii].getText());
                        
                        System.out.println(dice + " - " + val);
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
                public void keyTyped(KeyEvent e)
                {
                    if (!Character.isDigit(e.getKeyChar()))
                        e.consume();
                    else
                        localChange = true;
                }
            });
            txtAttributes[i].getDocument().addDocumentListener(new SheetDocumentListener()
            {
                @Override
                public void update()
                {
                    if (!localChange || txtAttributes[ii].getText().isEmpty())
                        return;
                    
                    int attr = Integer.parseInt(txtAttributes[ii].getText());
                    
                    sheet.getAttributes()[ii] = attr;
                    
                    sendSheetUpdate(UpdateField.ATTRIBUTES, attr, ii, UpdateType.UPDATE);
                }
            });
            
            content.add(chkAttributeMarked[i]);
            content.add(lblAttributeDesc);
            content.add(txtAttributes[i]);
            
            pnlAttributesContent.add(content);
        }
        
    }
    
    private void loadSkills()
    {
        scrSkills.getVerticalScrollBar().setUnitIncrement(30);
        
        FlowLayout layout = (FlowLayout) pnlSkillsContent.getLayout();
        
        int pnlW = 175;
        int pnlH = 75;
        int hGap = layout.getHgap();
        int vGap = layout.getVgap();
        int numRow = (int) Math.ceil(model.getSkills().length / (pnlSkillsContent.getWidth() / (pnlW + hGap)));
        
        int height = numRow * pnlH + numRow * vGap + 20;
        
        pnlSkillsContent.setSize(pnlSkillsContent.getWidth(), height);
        pnlSkills.setPreferredSize(new Dimension(pnlSkills.getPreferredSize().width, pnlSkills.getPreferredSize().height + height));
        
        for (int i = 0; i < model.getSkills().length; i++)
        {
            JLabel lblSkillDesc = new JLabel(model.getSkills()[i].getName());
            txtSkills[i] = new JTextField(Integer.toString(sheet.getSkills()[i]));
            
            JPanel content = new JPanel(null, false);
            content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
            content.setPreferredSize(new Dimension(pnlW, pnlH));
            content.setBackground(Color.BLACK);
            final int ii = i;
            
            chkSkillMarked[i] = new JCheckBox("", sheet.getSkillMarked()[i]);
            chkSkillMarked[i].setAlignmentX(CENTER_ALIGNMENT);
            chkSkillMarked[i].setBackground(Color.BLACK);
            chkSkillMarked[i].addActionListener(l ->
            {
                boolean val = chkSkillMarked[ii].isSelected();
                sheet.getSkillMarked()[ii] = val;
                sendSheetUpdate(UpdateField.MARK_SKILLS, val, ii, UpdateType.UPDATE);
            });
            
            lblSkillDesc.setFont(f14);
            lblSkillDesc.setBounds(0, 0, getStringWidth(lblSkillDesc.getFont(), lblSkillDesc.getText()), lblSkillDesc.getFont().getSize());
            lblSkillDesc.setForeground(Color.WHITE);
            lblSkillDesc.setAlignmentX(CENTER_ALIGNMENT);
            lblSkillDesc.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    try
                    {
                        int dice = rand.nextInt(model.getSkills()[ii].getDiceNum() + 1);
                        
                        int val = Integer.parseInt(txtSkills[ii].getText());
                        
                        System.out.println(dice + " - " + val);
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
                public void keyTyped(KeyEvent e)
                {
                    if (!Character.isDigit(e.getKeyChar()))
                        e.consume();
                    else
                        localChange = true;
                }
            });
            txtSkills[i].getDocument().addDocumentListener(new SheetDocumentListener()
            {
                @Override
                public void update()
                {
                    if (!localChange || txtSkills[ii].getText().isEmpty())
                        return;
                    
                    int attr = Integer.parseInt(txtSkills[ii].getText());
                    
                    sheet.getSkills()[ii] = attr;
                    
                    sendSheetUpdate(UpdateField.SKILLS, attr, ii, UpdateType.UPDATE);
                }
            });
            
            content.add(chkSkillMarked[i]);
            content.add(lblSkillDesc);
            content.add(txtSkills[i]);
            
            pnlSkillsContent.add(content);
        }
    }
    
    private void loadEquipments()
    {
        scrEquipments.getVerticalScrollBar().setUnitIncrement(30);
        
        for (int i = 0; i < model.getEquipmentDescriptions().length; i++)
        {
            JLabel desc = new JLabel(model.getEquipmentDescriptions()[i]);
            desc.setForeground(Color.WHITE);
            desc.setFont(f);
            desc.setPreferredSize(new Dimension(getStringWidth(desc.getFont(), desc.getText()), desc.getFont().getSize()));
            pnlEquipmentsDescription.add(desc);
        }
        
        btnAddEquipment.setBackground(Color.BLACK);
        btnAddEquipment.setForeground(Color.WHITE);
        btnAddEquipment.setPreferredSize(new Dimension(50, 25));
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
        pnlEquipmentsDescription.add(btnAddEquipment);
        
        for (int i = 0; i < sheet.getEquipments().size(); i++)
            createEquipmentField(sheet.getEquipments().get(i));
    }
    
    private void loadItems()
    {
        scrItems.getVerticalScrollBar().setUnitIncrement(30);
        
        for (int i = 0; i < model.getItemDescriptions().length; i++)
        {
            JLabel desc = new JLabel(model.getItemDescriptions()[i]);
            desc.setForeground(Color.WHITE);
            desc.setFont(f);
            desc.setPreferredSize(new Dimension(getStringWidth(desc.getFont(), desc.getText()), desc.getFont().getSize()));
            pnlItemsDescription.add(desc);
        }
        
        btnAddItem.setBackground(Color.BLACK);
        btnAddItem.setForeground(Color.WHITE);
        btnAddItem.setPreferredSize(new Dimension(50, 25));
        btnAddItem.addActionListener(l ->
        {
            String[] obj = new String[model.getItemDescriptions().length];
            for (int i = 0; i < obj.length; i++)
                obj[i] = "none";
            obj[0] = "Item " + (items.size() + 1);
            JField e = createItemField(obj);
            sheet.getItems().add(obj);
            sendSheetUpdate(UpdateField.ITEMS, obj, items.indexOf(e), UpdateType.ADD);
        });
        pnlItemsDescription.add(btnAddItem);
        
        for (int i = 0; i < sheet.getItems().size(); i++)
            createItemField(sheet.getItems().get(i));
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrMain = new SheetScrollPane();
        ;
        pnlMain = new javax.swing.JPanel();
        scrInfo = new SheetScrollPane();
        pnlInfo = new javax.swing.JPanel();
        lblInfo = new javax.swing.JLabel();
        pnlInfoContent = new javax.swing.JPanel();
        scrStats = new SheetScrollPane();
        ;
        pnlStats = new javax.swing.JPanel();
        lblStats = new javax.swing.JLabel();
        pnlStatsContent = new javax.swing.JPanel();
        pnlAbout = new javax.swing.JPanel();
        scrAbout = new javax.swing.JScrollPane();
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
        lblAbout = new javax.swing.JLabel();
        pnlExtras = new javax.swing.JPanel();
        scrExtras = new javax.swing.JScrollPane();
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
        lblExtras = new javax.swing.JLabel();
        scrAttributes = new SheetScrollPane();
        ;
        pnlAttributes = new javax.swing.JPanel();
        lblAttributes = new javax.swing.JLabel();
        pnlAttributesContent = new javax.swing.JPanel();
        scrSkills = new SheetScrollPane();
        ;
        pnlSkills = new javax.swing.JPanel();
        lblSkills = new javax.swing.JLabel();
        pnlSkillsContent = new javax.swing.JPanel();
        scrEquipments = new SheetScrollPane();
        ;
        pnlEquipments = new javax.swing.JPanel();
        lblEquipments = new javax.swing.JLabel();
        pnlEquipmentsDescription = new javax.swing.JPanel();
        pnlEquipmentsContent = new javax.swing.JPanel();
        scrItems = new SheetScrollPane();
        ;
        pnlItems = new javax.swing.JPanel();
        lblItems = new javax.swing.JLabel();
        pnlItemsDescription = new javax.swing.JPanel();
        pnlItemsContent = new javax.swing.JPanel();

        setTitle("Character Sheet");

        pnlMain.setBackground(new java.awt.Color(0, 0, 0));
        pnlMain.setPreferredSize(new java.awt.Dimension(1260, 2256));

        pnlInfo.setBackground(new java.awt.Color(0, 0, 0));
        pnlInfo.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        pnlInfo.setPreferredSize(new java.awt.Dimension(550, 523));

        lblInfo.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblInfo.setForeground(new java.awt.Color(255, 255, 255));
        lblInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInfo.setText("Character Informations");

        pnlInfoContent.setBackground(new java.awt.Color(0, 0, 0));
        pnlInfoContent.setLayout(new javax.swing.BoxLayout(pnlInfoContent, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.GroupLayout pnlInfoLayout = new javax.swing.GroupLayout(pnlInfo);
        pnlInfo.setLayout(pnlInfoLayout);
        pnlInfoLayout.setHorizontalGroup(
            pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInfoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 526, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlInfoContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlInfoLayout.setVerticalGroup(
            pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlInfoContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        scrInfo.setViewportView(pnlInfo);

        pnlStats.setBackground(new java.awt.Color(0, 0, 0));
        pnlStats.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        pnlStats.setPreferredSize(new java.awt.Dimension(490, 510));

        lblStats.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblStats.setForeground(new java.awt.Color(255, 255, 255));
        lblStats.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStats.setText("Stats");

        pnlStatsContent.setBackground(new java.awt.Color(0, 0, 0));
        pnlStatsContent.setLayout(new javax.swing.BoxLayout(pnlStatsContent, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.GroupLayout pnlStatsLayout = new javax.swing.GroupLayout(pnlStats);
        pnlStats.setLayout(pnlStatsLayout);
        pnlStatsLayout.setHorizontalGroup(
            pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlStatsContent, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE)
                    .addComponent(lblStats, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlStatsLayout.setVerticalGroup(
            pnlStatsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStatsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblStats)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlStatsContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        scrStats.setViewportView(pnlStats);

        pnlAbout.setBackground(new java.awt.Color(0, 0, 0));
        pnlAbout.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));

        txaAbout.setBackground(new java.awt.Color(102, 102, 102));
        txaAbout.setColumns(20);
        txaAbout.setForeground(new java.awt.Color(255, 255, 255));
        txaAbout.setLineWrap(true);
        txaAbout.setRows(5);
        scrAbout.setViewportView(txaAbout);

        lblAbout.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblAbout.setForeground(new java.awt.Color(255, 255, 255));
        lblAbout.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAbout.setText("About");

        javax.swing.GroupLayout pnlAboutLayout = new javax.swing.GroupLayout(pnlAbout);
        pnlAbout.setLayout(pnlAboutLayout);
        pnlAboutLayout.setHorizontalGroup(
            pnlAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAboutLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrAbout)
                    .addComponent(lblAbout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlAboutLayout.setVerticalGroup(
            pnlAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAboutLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAbout)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrAbout, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlExtras.setBackground(new java.awt.Color(0, 0, 0));
        pnlExtras.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));

        txaExtras.setBackground(new java.awt.Color(102, 102, 102));
        txaExtras.setColumns(20);
        txaExtras.setForeground(new java.awt.Color(255, 255, 255));
        txaExtras.setLineWrap(true);
        txaExtras.setRows(5);
        scrExtras.setViewportView(txaExtras);

        lblExtras.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblExtras.setForeground(new java.awt.Color(255, 255, 255));
        lblExtras.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblExtras.setText("Extras");

        javax.swing.GroupLayout pnlExtrasLayout = new javax.swing.GroupLayout(pnlExtras);
        pnlExtras.setLayout(pnlExtrasLayout);
        pnlExtrasLayout.setHorizontalGroup(
            pnlExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlExtrasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrExtras, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(lblExtras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlExtrasLayout.setVerticalGroup(
            pnlExtrasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlExtrasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblExtras)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrExtras, javax.swing.GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
                .addContainerGap())
        );

        scrAttributes.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));

        pnlAttributes.setBackground(new java.awt.Color(0, 0, 0));
        pnlAttributes.setPreferredSize(new java.awt.Dimension(1050, 247));

        lblAttributes.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblAttributes.setForeground(new java.awt.Color(255, 255, 255));
        lblAttributes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAttributes.setText("Attributes");

        pnlAttributesContent.setBackground(new java.awt.Color(0, 0, 0));
        pnlAttributesContent.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 25, 50));

        javax.swing.GroupLayout pnlAttributesLayout = new javax.swing.GroupLayout(pnlAttributes);
        pnlAttributes.setLayout(pnlAttributesLayout);
        pnlAttributesLayout.setHorizontalGroup(
            pnlAttributesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAttributesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlAttributesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlAttributesContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblAttributes, javax.swing.GroupLayout.DEFAULT_SIZE, 1034, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlAttributesLayout.setVerticalGroup(
            pnlAttributesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAttributesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAttributes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlAttributesContent, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))
        );

        scrAttributes.setViewportView(pnlAttributes);

        scrSkills.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));

        pnlSkills.setBackground(new java.awt.Color(0, 0, 0));
        pnlSkills.setPreferredSize(new java.awt.Dimension(1050, 200));

        lblSkills.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblSkills.setForeground(new java.awt.Color(255, 255, 255));
        lblSkills.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSkills.setText("Skills");

        pnlSkillsContent.setBackground(new java.awt.Color(0, 0, 0));
        pnlSkillsContent.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 25, 50));

        javax.swing.GroupLayout pnlSkillsLayout = new javax.swing.GroupLayout(pnlSkills);
        pnlSkills.setLayout(pnlSkillsLayout);
        pnlSkillsLayout.setHorizontalGroup(
            pnlSkillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSkillsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSkillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSkills, javax.swing.GroupLayout.DEFAULT_SIZE, 1034, Short.MAX_VALUE)
                    .addGroup(pnlSkillsLayout.createSequentialGroup()
                        .addComponent(pnlSkillsContent, javax.swing.GroupLayout.PREFERRED_SIZE, 1022, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlSkillsLayout.setVerticalGroup(
            pnlSkillsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSkillsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSkills)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlSkillsContent, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE))
        );

        scrSkills.setViewportView(pnlSkills);

        pnlEquipments.setBackground(new java.awt.Color(0, 0, 0));
        pnlEquipments.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        pnlEquipments.setPreferredSize(new java.awt.Dimension(1000, 240));
        pnlEquipments.setRequestFocusEnabled(false);

        lblEquipments.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblEquipments.setForeground(new java.awt.Color(255, 255, 255));
        lblEquipments.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEquipments.setText("Equipments");

        pnlEquipmentsDescription.setBackground(new java.awt.Color(0, 0, 0));
        pnlEquipmentsDescription.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 60, 0));

        pnlEquipmentsContent.setBackground(new java.awt.Color(0, 0, 0));
        pnlEquipmentsContent.setLayout(new javax.swing.BoxLayout(pnlEquipmentsContent, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.GroupLayout pnlEquipmentsLayout = new javax.swing.GroupLayout(pnlEquipments);
        pnlEquipments.setLayout(pnlEquipmentsLayout);
        pnlEquipmentsLayout.setHorizontalGroup(
            pnlEquipmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEquipmentsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlEquipmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlEquipmentsContent, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblEquipments, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1031, Short.MAX_VALUE)
                    .addComponent(pnlEquipmentsDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlEquipmentsLayout.setVerticalGroup(
            pnlEquipmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEquipmentsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEquipments)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlEquipmentsDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlEquipmentsContent, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
        );

        scrEquipments.setViewportView(pnlEquipments);

        pnlItems.setBackground(new java.awt.Color(0, 0, 0));
        pnlItems.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        pnlItems.setPreferredSize(new java.awt.Dimension(1000, 240));

        lblItems.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        lblItems.setForeground(new java.awt.Color(255, 255, 255));
        lblItems.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblItems.setText("Items");

        pnlItemsDescription.setBackground(new java.awt.Color(0, 0, 0));
        pnlItemsDescription.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING, 100, 0));

        pnlItemsContent.setBackground(new java.awt.Color(0, 0, 0));
        pnlItemsContent.setLayout(new javax.swing.BoxLayout(pnlItemsContent, javax.swing.BoxLayout.Y_AXIS));

        javax.swing.GroupLayout pnlItemsLayout = new javax.swing.GroupLayout(pnlItems);
        pnlItems.setLayout(pnlItemsLayout);
        pnlItemsLayout.setHorizontalGroup(
            pnlItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlItemsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlItemsContent, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblItems, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1031, Short.MAX_VALUE)
                    .addComponent(pnlItemsDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlItemsLayout.setVerticalGroup(
            pnlItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlItemsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblItems)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlItemsDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlItemsContent, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
        );

        scrItems.setViewportView(pnlItems);

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrAttributes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1060, Short.MAX_VALUE)
                    .addComponent(scrItems, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(scrEquipments, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlMainLayout.createSequentialGroup()
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pnlAbout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(scrInfo))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlExtras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(scrStats)))
                    .addComponent(scrSkills, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(100, 100, 100))
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrStats, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE)
                    .addComponent(scrInfo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlAbout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlExtras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(scrAttributes, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(scrSkills, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(scrEquipments, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(scrItems, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
    private javax.swing.JLabel lblAbout;
    private javax.swing.JLabel lblAttributes;
    private javax.swing.JLabel lblEquipments;
    private javax.swing.JLabel lblExtras;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblItems;
    private javax.swing.JLabel lblSkills;
    private javax.swing.JLabel lblStats;
    private javax.swing.JPanel pnlAbout;
    private javax.swing.JPanel pnlAttributes;
    private javax.swing.JPanel pnlAttributesContent;
    private javax.swing.JPanel pnlEquipments;
    private javax.swing.JPanel pnlEquipmentsContent;
    private javax.swing.JPanel pnlEquipmentsDescription;
    private javax.swing.JPanel pnlExtras;
    private javax.swing.JPanel pnlInfo;
    private javax.swing.JPanel pnlInfoContent;
    private javax.swing.JPanel pnlItems;
    private javax.swing.JPanel pnlItemsContent;
    private javax.swing.JPanel pnlItemsDescription;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPanel pnlSkills;
    private javax.swing.JPanel pnlSkillsContent;
    private javax.swing.JPanel pnlStats;
    private javax.swing.JPanel pnlStatsContent;
    private javax.swing.JScrollPane scrAbout;
    private javax.swing.JScrollPane scrAttributes;
    private javax.swing.JScrollPane scrEquipments;
    private javax.swing.JScrollPane scrExtras;
    private javax.swing.JScrollPane scrInfo;
    private javax.swing.JScrollPane scrItems;
    private javax.swing.JScrollPane scrMain;
    private javax.swing.JScrollPane scrSkills;
    private javax.swing.JScrollPane scrStats;
    private javax.swing.JTextArea txaAbout;
    private javax.swing.JTextArea txaExtras;
    // End of variables declaration//GEN-END:variables
    
    
    private JField createEquipmentField(String[] equip)
    {
        FlowLayout layout = new FlowLayout(FlowLayout.LEADING, ((FlowLayout)pnlEquipmentsDescription.getLayout()).getHgap(), 0);
        JField field = new JField(layout, false);
        field.setBackground(Color.BLACK);
        
        JLabel[] labels = new JLabel[equip.length];
        for (int i = 0; i < equip.length; i++)
        {
            String str = equip[i];
            JLabel lbl = new JLabel(str);
            lbl.setForeground(Color.WHITE);
            labels[i] = lbl;
            
            final int ii = i;
            lbl.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    if (e.getClickCount() < 2)
                        return;
                    
                    String newVal = JOptionPane.showInputDialog("Please type in new value.", lbl.getText());
                
                    if (newVal == null)
                        return;

                    lbl.setText(newVal);

                    int equipIndex = equipments.indexOf(field);
                    
                    sheet.getEquipments().get(equipIndex)[ii] = newVal;

                    sendSheetUpdate(UpdateField.EQUIPMENTS, sheet.getEquipments().get(equipIndex), equipIndex, UpdateType.UPDATE);

                    repaint();
                }
            });
            
            field.add(lbl);
        }
        field.setProperties(labels);
        
        JButton remove = new JButton("-");
        remove.setBackground(Color.BLACK);
        remove.setForeground(Color.WHITE);
        remove.setPreferredSize(new Dimension(50, 15));
        remove.addActionListener(l ->
        {
            int index = equipments.indexOf(field);
            sendSheetUpdate(UpdateField.EQUIPMENTS, null, index, UpdateType.REMOVE);
            sheet.getEquipments().remove(equip);
            removeEquipmentField(index);
        });
        field.add(remove);
        
        equipments.add(field);
        
        pnlEquipmentsContent.add(field);
        int h = (f14.getSize() + 10) * equipments.size();
        
        pnlEquipmentsContent.setBounds(pnlEquipmentsContent.getX(), 
                pnlEquipmentsContent.getY(), 
                pnlEquipmentsContent.getWidth(), h);
        pnlEquipments.setPreferredSize(new Dimension(pnlEquipments.getPreferredSize().width, pnlEquipmentsContent.getY() + pnlEquipmentsContent.getHeight() + 20));
        scrEquipments.revalidate();
        pnlEquipments.revalidate();
        
        return field;
    }
    
    private void removeEquipmentField(int index)
    {
        equipments.remove(index);
        pnlEquipmentsContent.remove(index);
        
        int hh = (f14.getSize() + 10) * equipments.size();
        pnlEquipmentsContent.setBounds(pnlEquipmentsContent.getX(), 
            pnlEquipmentsContent.getY(), 
            pnlEquipmentsContent.getWidth(), hh);

        pnlEquipments.setPreferredSize(new Dimension(pnlEquipments.getPreferredSize().width, pnlEquipmentsContent.getY() + pnlEquipmentsContent.getHeight() + 20));

        scrEquipments.revalidate();
        pnlEquipments.revalidate();
    }
    
    private JField createItemField(String[] item)
    {
        FlowLayout layout = new FlowLayout(FlowLayout.LEADING, ((FlowLayout)pnlItemsDescription.getLayout()).getHgap(), 0);
        JField field = new JField(layout, false);
        field.setBackground(Color.BLACK);
        
        JLabel[] labels = new JLabel[item.length];
        for (int i = 0; i < item.length; i++)
        {
            String str = item[i];
            JLabel lbl = new JLabel(str);
            lbl.setForeground(Color.WHITE);
            labels[i] = lbl;
            
            final int ii = i;
            lbl.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    if (e.getClickCount() < 2)
                        return;
                    
                    String newVal = JOptionPane.showInputDialog("Please type in new value.", lbl.getText());
                
                    if (newVal == null)
                        return;

                    lbl.setText(newVal);

                    int itemIndex = items.indexOf(field);
                    
                    sheet.getItems().get(itemIndex)[ii] = newVal;

                    sendSheetUpdate(UpdateField.ITEMS, sheet.getItems().get(itemIndex), itemIndex, UpdateType.UPDATE);

                    repaint();
                }
            });
            
            field.add(lbl);
        }
        field.setProperties(labels);
        
        JButton remove = new JButton("-");
        remove.setBackground(Color.BLACK);
        remove.setForeground(Color.WHITE);
        remove.setPreferredSize(new Dimension(50, 15));
        remove.addActionListener(l ->
        {
            int index = items.indexOf(field);
            sendSheetUpdate(UpdateField.ITEMS, null, index, UpdateType.REMOVE);
            sheet.getEquipments().remove(item);
            removeItem(index);
        });
        field.add(remove);
        
        items.add(field);
        
        pnlItemsContent.add(field);
        int h = (f14.getSize() + 10) * items.size();
        
        pnlItemsContent.setBounds(pnlItemsContent.getX(), 
                pnlItemsContent.getY(), 
                pnlItemsContent.getWidth(), h);
        pnlItems.setPreferredSize(new Dimension(pnlItems.getPreferredSize().width, pnlItemsContent.getY() + pnlItemsContent.getHeight() + 20));
        scrItems.revalidate();
        pnlItems.revalidate();
        
        return field;
    }
    
    private void removeItem(int index)
    {
        items.remove(index);
        pnlItemsContent.remove(index);
        
        int hh = (f14.getSize() + 10) * items.size();
        pnlItemsContent.setBounds(pnlItemsContent.getX(), 
            pnlItemsContent.getY(), 
            pnlItemsContent.getWidth(), hh);

        pnlItems.setPreferredSize(new Dimension(pnlItems.getPreferredSize().width, pnlItemsContent.getY() + pnlItemsContent.getHeight() + 20));

        scrItems.revalidate();
        pnlItems.revalidate();
    }
    
    private int getStringWidth(Font font, String text)
    {
        return getFontMetrics(font).stringWidth(text);
    }
    
    public abstract void sendSheetUpdate(UpdateField field, Object newValue, int propertyIndex, UpdateType type);
    
    
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
    
    public PlayerSheet getSheet()
    {
        return sheet;
    }

    public int getConnectionID()
    {
        return connectionID;
    }
    
}
