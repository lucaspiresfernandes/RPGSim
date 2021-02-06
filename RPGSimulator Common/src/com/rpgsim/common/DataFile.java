package com.rpgsim.common;

public class DataFile
{
    private final String fileName;
    private final String data;

    public DataFile(String fileName)
    {
        this.fileName = fileName;
        this.data = null;
    }
    
    public DataFile(String fileName, String data)
    {
        this.fileName = fileName;
        this.data = data;
    }

    public String getFileName()
    {
        return fileName;
    }

    public String getData()
    {
        return data;
    }
    
}
