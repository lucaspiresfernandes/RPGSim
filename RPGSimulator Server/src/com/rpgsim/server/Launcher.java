package com.rpgsim.server;

import com.rpgsim.common.DataFile;
import com.rpgsim.common.FileManager;
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
            DataFile[] files = new DataFile[]
            {
                new DataFile("config.dat",
                        "TCPPort=7001\n"
                        + "UDPPort=7002"),
                new DataFile("accounts.bin")
            };
            FileManager.setFiles(files);
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
