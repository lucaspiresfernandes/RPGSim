package com.rpgsim.common.sheets;

import java.io.Serializable;

public class ColorField implements Serializable
{
    private final String name;
    private final int color;

    private ColorField()
    {
        this.name = null;
        this.color = 0;
    }

    public ColorField(String name, int color)
    {
        this.name = name;
        this.color = color;
    }

    public String getName()
    {
        return name;
    }

    public int getColor()
    {
        return color;
    }
    
}
