package com.rpgsim.common.sheets;

import java.io.Serializable;

public class SheetModel implements Serializable
{
    private final String[] info;
    private final String[] currentStats;
    private final String[] maxStats;
    private final String[] attributes;
    private final String[] skills;
    private final String[] equipments;

    private SheetModel()
    {
        this.info = null;
        this.currentStats = null;
        this.maxStats = null;
        this.attributes = null;
        this.skills = null;
        this.equipments = null;
    }

    public SheetModel(String[] info, String[] currentStats, String[] maxStats, String[] attributes, String[] skills, String[] equipments)
    {
        this.info = info;
        this.currentStats = currentStats;
        this.maxStats = maxStats;
        this.attributes = attributes;
        this.skills = skills;
        this.equipments = equipments;
    }

    public String[] getInfo()
    {
        return info;
    }

    public String[] getCurrentStats()
    {
        return currentStats;
    }

    public String[] getMaxStats()
    {
        return maxStats;
    }

    public String[] getAttributes()
    {
        return attributes;
    }

    public String[] getSkills()
    {
        return skills;
    }

    public String[] getEquipments()
    {
        return equipments;
    }
    
}
