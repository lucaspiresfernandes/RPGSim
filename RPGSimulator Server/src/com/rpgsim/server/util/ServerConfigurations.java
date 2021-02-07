package com.rpgsim.server.util;

import com.rpgsim.common.FileManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerConfigurations
{
    private final HashMap<String, String> configHash = new HashMap<>();
    
    public ServerConfigurations()
    {
        try (BufferedReader in = new BufferedReader(new FileReader(new File(FileManager.app_dir + "config.dat"))))
        {
            String l;
            while ((l = in.readLine()) != null)
            {
                String[] config = l.split("=");
                configHash.put(config[0], config[1]);
            }
        }
        catch (IOException ex)
        {
            Logger.getLogger(ServerConfigurations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getIntegerProperty(String key)
    {
        return Integer.parseInt(getProperty(key));
    }
    
    public String getProperty(String key)
    {
        return configHash.get(key);
    }

}
