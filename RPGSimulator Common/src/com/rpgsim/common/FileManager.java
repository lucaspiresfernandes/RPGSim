package com.rpgsim.common;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class FileManager
{
    public static final String app_dir = System.getProperty("user.dir") + "\\";
    private static DataFile[] files;

    public static void setFiles(DataFile[] files)
    {
        FileManager.files = files;
    }
    
    public static void checkFiles() throws IOException
    {
        File parent = new File(app_dir);
        
        for (DataFile df : files)
        {
            File f = new File(parent, df.getFileName());
            
            if (f.exists() && df.getFileName().contains("."))
            {
                String line;
                try (BufferedReader in = new BufferedReader(new FileReader(f)))
                {
                    line = in.readLine();
                }
                
                if (line == null && df.getData() != null && !df.getData().isEmpty())
                {
                    try (BufferedWriter out = new BufferedWriter(new FileWriter(f)))
                    {
                        out.write(df.getData());
                    }
                }
            }
            else
            {
                //It's a file.
                if (df.getFileName().contains("."))
                {
                    f.createNewFile();
                    if (df.getData() != null && !df.getData().isEmpty())
                    {
                        try (BufferedWriter out = new BufferedWriter(new FileWriter(f)))
                        {
                            out.write(df.getData());
                        }
                    }
                }
                //It's a directory.
                else
                {
                    f.mkdirs();
                }
            }
        }
    }
    
    private static BufferedImage defaultImage;
    public static Image getDefaultImage()
    {
        if (defaultImage == null)
        {
            try
            {
                defaultImage = ImageIO.read(new File(FileManager.app_dir + "data files\\images\\null.png"));
            }
            catch (IOException ex)
            {
                System.out.println("could not read file.");
            }
        }
        return defaultImage;
    }
    
    
}
