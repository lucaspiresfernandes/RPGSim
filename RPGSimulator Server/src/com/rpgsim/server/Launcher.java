package com.rpgsim.server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Launcher
{
    public static void main(String[] args)
    {
        try
        {
            FileManager.checkFiles();
            new ServerManager().start();
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null, "Could not check essential files", "FATAL ERROR", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
