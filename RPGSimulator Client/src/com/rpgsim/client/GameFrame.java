package com.rpgsim.client;

import java.awt.event.WindowListener;
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
        this.game = new ClientGame(manager, super.getWidth(), super.getHeight());
        super.addWindowFocusListener(this.game.getInput());
        super.getContentPane().add(game);
    }
    
    public ClientGame getGame()
    {
        return game;
    }
    
    
    
}
