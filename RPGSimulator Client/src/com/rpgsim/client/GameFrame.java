package com.rpgsim.client;

import java.awt.event.WindowListener;
import javax.swing.JFrame;

public class GameFrame extends JFrame
{
    private final ClientGame game;

    public GameFrame(ClientManager manager, WindowListener l)
    {
        super("RPG Simulator");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        addWindowListener(l);
        this.game = new ClientGame(manager, super.getWidth(), super.getHeight());
        getContentPane().add(game);
    }
    
    public ClientGame getGame()
    {
        return game;
    }
    
    
    
}
