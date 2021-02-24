package com.rpgsim.common.sheets;

import java.io.Serializable;

public class SheetModel implements Serializable
{
    private final String[] info;
    private final ColorField[] stats;
    private final DiceField[] attributes;
    private final DiceField[] skills;
    private final String[] equipmentDescriptions;
    private final String[] itemDescriptions;

    private SheetModel()
    {
        this.info = null;
        this.stats = null;
        this.attributes = null;
        this.skills = null;
        this.equipmentDescriptions = null;
        this.itemDescriptions = null;
    }

    public SheetModel(String[] info, 
            ColorField[] stats, 
            DiceField[] attributes, 
            DiceField[] skills, 
            String[] equipmentDescriptions,
            String[] itemDescriptions)
    {
        this.info = info;
        this.stats = stats;
        this.attributes = attributes;
        this.skills = skills;
        this.equipmentDescriptions = equipmentDescriptions;
        this.itemDescriptions = itemDescriptions;
    }

    public String[] getInfo()
    {
        return info;
    }

    public ColorField[] getStats()
    {
        return stats;
    }

    public DiceField[] getAttributes()
    {
        return attributes;
    }

    public DiceField[] getSkills()
    {
        return skills;
    }

    public String[] getEquipmentDescriptions()
    {
        return equipmentDescriptions;
    }

    public String[] getItemDescriptions()
    {
        return itemDescriptions;
    }
    
}
