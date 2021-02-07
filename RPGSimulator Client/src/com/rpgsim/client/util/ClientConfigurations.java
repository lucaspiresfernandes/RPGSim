package com.rpgsim.client.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientConfigurations
{
    private final HashMap<String, String> configHash = new HashMap<>();
    
    public ClientConfigurations() throws IOException
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
    }
    
    public int getIntegerProperty(String key)
    {
        return Integer.parseInt(getProperty(key));
    }
    
    public String getProperty(String key)
    {
        return configHash.get(key);
    }
    
    public boolean getBooleanProperty(String key)
    {
        return Boolean.parseBoolean(configHash.get(key));
    }
    
    public void setProperty(String key, String value)
    {
        configHash.replace(key, value);
    }
    
    public void saveConfigurations() throws IOException
    {
        String[] keys = configHash.keySet().toArray(new String[0]);
        String[] values = configHash.values().toArray(new String[0]);
        try (BufferedWriter out = new BufferedWriter(new FileWriter(new File(FileManager.app_dir + "config.dat"), false)))
        {
            String config = "";
            for (int i = 0; i < values.length; i++)
                config += keys[i] + "=" + values[i] + "\n";
            out.write(config);
        }
    }
    
}
