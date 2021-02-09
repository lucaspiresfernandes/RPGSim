package com.rpgsim.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class ApplicationConfigurations
{
    private final String path;
    private final HashMap<String, String> configHash = new HashMap<>();
    
    public ApplicationConfigurations(String path) throws IOException
    {
        this.path = path;
        try (BufferedReader in = new BufferedReader(new FileReader(new File(path))))
        {
            String line;
            while ((line = in.readLine()) != null)
            {
                if (line.isEmpty() || line.contains("#"))
                    continue;
                String[] config = line.split("=");
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
        try (BufferedWriter out = new BufferedWriter(new FileWriter(new File(path), false)))
        {
            String config = "";
            for (int i = 0; i < values.length; i++)
                config += keys[i] + "=" + values[i] + "\n";
            out.write(config);
        }
    }
    
}
