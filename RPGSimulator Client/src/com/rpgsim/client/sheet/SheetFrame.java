package com.rpgsim.client.sheet;

import com.rpgsim.common.sheets.PlayerSheet;
import com.rpgsim.common.sheets.SheetModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowListener;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

public class SheetFrame extends javax.swing.JFrame
{
    private SheetModel model;
    private PlayerSheet sheet;
    
    private final Font f = new Font("Comic Sans MS", Font.PLAIN, 16);
    
    private SheetPanel pnlInfo, pnlAbout, pnlExtras, pnlStats, 
            pnlAttributes, pnlSkills, pnlEquipments, pnlItems;
    
    public SheetFrame(WindowListener l)
    {
        initComponents();
        super.addWindowListener(l);
        pnlMain.setBackground(Color.BLACK);
        scrMain.getHorizontalScrollBar().setUnitIncrement(40);
        scrMain.getVerticalScrollBar().setUnitIncrement(40);
    }
    
    public void loadSheet(SheetModel model, PlayerSheet sheet)
    {
        this.model = model;
        this.sheet = sheet;
        internalInit();
        int h = pnlSkills.getY() + pnlSkills.getHeight() + 50;
        pnlMain.setPreferredSize(new Dimension(pnlMain.getPreferredSize().width, h));
    }
    
    private void internalInit()
    {
        loadBasicInfoPanel();
        loadBasicStatsPanel();
        loadAboutPanel();
        loadAttributesPanel();
        loadSkillsPanel();
    }
    
    private void loadBasicInfoPanel()
    {
        pnlInfo = new SheetPanel("Character Information", f.deriveFont(24f), null, true);
        pnlMain.add(pnlInfo);
        
        JLabel[] lblBasicInfo = new JLabel[model.getBasicInfo().length];
        JTextField[] txtBasicInfo = new JTextField[model.getBasicInfo().length];
        
        int lines = lblBasicInfo.length;
        int gap = 40;
        
        int txtGap = 10;
        
        pnlInfo.setBounds(10, 10, getWidth() / 2 - 25, lines * f.getSize() + lines * txtGap + lines * gap);
        pnlInfo.setBackground(Color.BLACK);
        
        int x = 10;
        int y = 50;
        
        for (int i = 0; i < lblBasicInfo.length; i++)
        {
            lblBasicInfo[i] = new JLabel(model.getBasicInfo()[i]);
            txtBasicInfo[i] = new JTextField(sheet.getBasicInfo()[i]);
            pnlInfo.add(txtBasicInfo[i]);
            pnlInfo.add(lblBasicInfo[i]);
            
            JLabel lbl = lblBasicInfo[i];
            JTextField txt = txtBasicInfo[i];
            
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
        pnlStats = new SheetPanel("Stats", f.deriveFont(24f), null, false);
        pnlMain.add(pnlStats);
        
        pnlStats.setBounds(pnlInfo.getWidth() + 20, 10, getWidth() / 2 - 25, pnlInfo.getHeight());
        pnlStats.setBackground(Color.BLACK);
        
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
        
        pnlAbout = new SheetPanel("About", f.deriveFont(24f), null, true);
        pnlAbout.setBounds(pnlInfo.getWidth() + 20, pnlStats.getHeight() + 20, getWidth() / 2 - 25, txtH + 60);
        pnlAbout.setBackground(Color.BLACK);
        pnlMain.add(pnlAbout);
        
        JTextArea aboutTxt = new JTextArea()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                if (this.getText().isEmpty())
                {
                    g.setColor(Color.LIGHT_GRAY);
                    g.setFont(f);
                    g.drawString("Write about your character's likes, dislikes, ", 0, f.getSize());
                    g.drawString("dreams, etc.", 0, f.getSize() * 2);
                }
            }
        };
        aboutTxt.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                if (aboutTxt.getText().length() < 1)
                {
                    aboutTxt.repaint();
                }
            }
        });
        aboutTxt.setBackground(Color.DARK_GRAY);
        aboutTxt.setForeground(Color.WHITE);
        aboutTxt.setBorder(new MatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
        aboutTxt.setFont(f);
        aboutTxt.setBounds(10, 50, pnlAbout.getWidth() - 10 - 20, txtH);
        aboutTxt.setWrapStyleWord(true);
        aboutTxt.setLineWrap(true);
        pnlAbout.add(aboutTxt);
        
        pnlExtras = new SheetPanel("Extras", f.deriveFont(24f), null, true);
        pnlExtras.setBounds(pnlInfo.getWidth() + 20, pnlAbout.getY() + pnlAbout.getHeight() + 20, getWidth() / 2 - 25, txtH + 60);
        pnlExtras.setBackground(Color.BLACK);
        pnlMain.add(pnlExtras);
        
        System.out.println(pnlExtras.getY());
        
        JTextArea extrasTxt = new JTextArea()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                if (this.getText().isEmpty())
                {
                    g.setColor(Color.LIGHT_GRAY);
                    g.setFont(f);
                    g.drawString("You can write anything you want here. E.g:", 0, f.getSize());
                    g.drawString("Notes, things you want to do, etc.", 0, f.getSize() * 2);
                }
            }
        };
        extrasTxt.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                if (extrasTxt.getText().length() < 1)
                {
                    extrasTxt.repaint();
                }
            }
        });
        extrasTxt.setBackground(Color.DARK_GRAY);
        extrasTxt.setForeground(Color.WHITE);
        extrasTxt.setBorder(new MatteBorder(0, 0, 1, 0, Color.DARK_GRAY));
        extrasTxt.setFont(f);
        extrasTxt.setBounds(10, 50, pnlAbout.getWidth() - 10 - 20, txtH);
        extrasTxt.setWrapStyleWord(true);
        extrasTxt.setLineWrap(true);
        
        pnlExtras.add(extrasTxt);
    }
    
    private void loadAttributesPanel()
    {
        FlowLayout l = new FlowLayout(FlowLayout.CENTER, 15, 50);
        pnlAttributes = new SheetPanel("Attributes", f.deriveFont(24f), l, true);
        pnlMain.add(pnlAttributes);
        
        JLabel[] lblAttributes = new JLabel[model.getAttributes().length];
        JTextField[] txtAttributes = new JTextField[model.getAttributes().length];
        
        int charH = 75;
        
        int numRow = (int) Math.ceil(lblAttributes.length / 3.0);
        pnlAttributes.setBounds(10, 
                pnlInfo.getHeight() + 20, 
                getWidth() / 2 - 25, 
                numRow * charH + numRow * l.getVgap() + 20);
        pnlAttributes.setBackground(Color.BLACK);
        
        for (int i = 0; i < lblAttributes.length; i++)
        {
            lblAttributes[i] = new JLabel(model.getAttributes()[i]);
            txtAttributes[i] = new JTextField(sheet.getAttributes()[i] + "");
            
            JLabel lbl = lblAttributes[i];
            JTextField txt = txtAttributes[i];
            
            JPanel miniChar = new JPanel(null, false);
            miniChar.setLayout(new BoxLayout(miniChar, BoxLayout.PAGE_AXIS));
            miniChar.setPreferredSize(new Dimension(100, charH));
            miniChar.setBackground(Color.BLACK);
            
            Font f12 = f.deriveFont(12f);
            
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
            
            miniChar.add(lbl);
            miniChar.add(txt);
            
            pnlAttributes.add(miniChar);
        }
    }
    
    private void loadSkillsPanel()
    {
        int x = 10;
        int y = Math.max(pnlAttributes.getY() + pnlAttributes.getHeight(), pnlExtras.getY() + pnlExtras.getHeight());
        int charH = 75;
        
        JLabel[] lblSkills = new JLabel[model.getSkills().length];
        JTextField[] txtSkills = new JTextField[model.getSkills().length];
        
        FlowLayout l = new FlowLayout(FlowLayout.CENTER, 50, 50);
        pnlSkills = new SheetPanel("Skills", f.deriveFont(24f), l, true);
        
        int numRow = (int) Math.ceil(lblSkills.length / 5.0);
        pnlSkills.setBounds(10, 
                y, 
                getWidth() - x - 30, 
                numRow * charH + numRow * l.getVgap() + 20);
        pnlSkills.setBackground(Color.BLACK);
        pnlMain.add(pnlSkills);
        
        
        for (int i = 0; i < lblSkills.length; i++)
        {
            lblSkills[i] = new JLabel(model.getSkills()[i]);
            txtSkills[i] = new JTextField(sheet.getSkills()[i] + "");
            
            JLabel lbl = lblSkills[i];
            JTextField txt = txtSkills[i];
            
            JPanel miniChar = new JPanel(null, false);
            miniChar.setLayout(new BoxLayout(miniChar, BoxLayout.PAGE_AXIS));
            miniChar.setPreferredSize(new Dimension(100, charH));
            miniChar.setBackground(Color.BLACK);
            
            Font f12 = f.deriveFont(12f);
            
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
            
            miniChar.add(lbl);
            miniChar.add(txt);
            
            pnlSkills.add(miniChar);
        }
        
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

        scrMain = new javax.swing.JScrollPane();
        pnlMain = new javax.swing.JPanel();

        setTitle("RPG Simulator Sheet");
        setResizable(false);

        scrMain.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        pnlMain.setMaximumSize(new java.awt.Dimension(770, 32767));
        pnlMain.setPreferredSize(new java.awt.Dimension(770, 1500));

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 802, Short.MAX_VALUE)
        );
        pnlMainLayout.setVerticalGroup(
            pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1500, Short.MAX_VALUE)
        );

        scrMain.setViewportView(pnlMain);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrMain, javax.swing.GroupLayout.DEFAULT_SIZE, 820, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrMain, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlMain;
    private javax.swing.JScrollPane scrMain;
    // End of variables declaration//GEN-END:variables
    
}
