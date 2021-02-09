package com.rpgsim.common.sheets;

import java.io.Serializable;

public class SheetModel implements Serializable
{
    private final String[] basicInfo;
    private final String[] basicStats;
    private final String[] attributes;
    private final String[] skills;

    public SheetModel()
    {
        this.basicInfo = null;
        this.basicStats = null;
        this.attributes = null;
        this.skills = null;
    }

    public SheetModel(String[] basicInfo, String[] basicStats, String[] attributes, String[] skills)
    {
        this.basicInfo = basicInfo;
        this.basicStats = basicStats;
        this.attributes = attributes;
        this.skills = skills;
    }

    public String[] getBasicInfo()
    {
        return basicInfo;
    }

    public String[] getBasicStats()
    {
        return basicStats;
    }

    public String[] getAttributes()
    {
        return attributes;
    }

    public String[] getSkills()
    {
        return skills;
    }
    
}
