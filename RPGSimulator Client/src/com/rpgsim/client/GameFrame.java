package com.rpgsim.client;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class GameFrame extends JFrame
{
    private final ClientGame game;

    public GameFrame(ClientManager manager, WindowListener l)
    {
        super("RPG Simulator");
        super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        super.setSize(800, 600);
        super.setResizable(false);
        super.setLocationRelativeTo(null);
        super.setLayout(null);
        super.addWindowListener(l);
        
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
        cursorImg, new Point(0, 0), "blank cursor");
        super.getContentPane().setCursor(blankCursor);
        
        this.game = new ClientGame(manager, super.getWidth(), super.getHeight());
        super.getContentPane().add(game);
    }
    
    public ClientGame getGame()
    {
        return game;
    }
    
    
    
}
