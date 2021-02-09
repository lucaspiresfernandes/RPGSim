package com.rpgsim.server;

import com.rpgsim.common.DataFile;
import com.rpgsim.common.FileManager;
import com.rpgsim.common.sheets.SheetManager;
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
            SheetManager model = new SheetManager();
            model.checkSheetModel();
            new ServerManager().start();
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null, "Could not check essential files. "
                    + "Try deleting config.ini or sheets.json and reopen the app", 
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
