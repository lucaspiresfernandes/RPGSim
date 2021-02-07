package com.rpgsim.client;

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
                "SaveUsername=false\n"
                + "SavePassword=false\n"
                + "Username=null\n"
                + "Password=null\n"
                + "IP=localhost\n"
                + "TCPPort=7001\n"
                + "UDPPort=7002"),
                new DataFile("data files\\images"),
                new DataFile("data files\\prefabs")
            };
            FileManager.setFiles(files);
            FileManager.checkFiles();
            new MainFrame().setVisible(true);
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null, "Could not check essential files", "FATAL ERROR", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Launcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
