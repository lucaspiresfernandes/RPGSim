package com.rpgsim.client;

import com.rpgsim.common.DataFile;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager
{
    public static final String app_dir = System.getProperty("user.dir") + "\\";
    
    private static final DataFile[] files = new DataFile[]
    {
        new DataFile("config.dat",
                "SaveUsername=false\n"
                + "SavePassword=false\n"
                + "IP=localhost\n"
                + "TCPPort=7001\n"
                + "UDPPort=7002")
    };

    public static void checkFiles() throws IOException
    {
        File parent = new File(app_dir);
        
        for (DataFile df : files)
        {
            File f = new File(parent, df.getFileName());
            
            if (f.exists())
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
    
    
    
}
