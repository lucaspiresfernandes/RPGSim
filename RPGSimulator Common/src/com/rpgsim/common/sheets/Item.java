package com.rpgsim.common.sheets;

import java.io.Serializable;

public class Item implements Serializable
{
    private String name;
    private String description;

    public Item()
    {
        name = "item";
        description = "description";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
    
}
