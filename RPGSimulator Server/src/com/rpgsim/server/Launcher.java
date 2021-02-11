package com.rpgsim.server;

import com.rpgsim.common.DataFile;
import com.rpgsim.common.FileManager;
import com.rpgsim.server.util.SheetManager;
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
            FileManager manager = new FileManager(generateFiles());
            manager.checkFiles();
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null, "Could not check and initialize "
                    + "essential files (They are either corrupted or inacessible). "
                    + "Try deleting all configuration files and run the server again.", 
                    "FATAL ERROR", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        try
        {
            SheetManager model = new SheetManager();
            model.checkSheetModel();
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null, "Could not check and initialize "
                    + "sheet.json file (It is either corrupted or inacessible). "
                    + "Try deleting it and run the server again.", 
                    "FATAL ERROR", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        try
        {
            ServerManager manager = new ServerManager();
            manager.start();
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null, "Could not check and initialize "
                    + "server configurations files (It is either corrupted or "
                    + "inacessible). Try deleteting configurations and run the server again.", 
                    "FATAL ERROR", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static DataFile[] generateFiles()
    {
        return new DataFile[]
            {
            new DataFile("README.txt",
                    "Welcome to RPG Simulator!\n"
                    + "To configure your character sheet, you can access data/sheet.json.\n"
                    + "You can add, remove or edit data inside, but do not change description\n"
                    + "names. (it is bounded to the program code.)"),
                new DataFile("data"),
                new DataFile("data\\config.ini",
                        "TCPPort=7001\n"
                        + "UDPPort=7002"),
                new DataFile("data\\accounts")
            };
    }
}
