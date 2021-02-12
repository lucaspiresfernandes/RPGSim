package com.rpgsim.common.sheets;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerSheet implements Serializable
{
    private String[] info;
    private String about;
    private String extras;
    private int[] currentStats;
    private int[] maxStats;
    private int[] attributes;
    private int[] skills;
    private final ArrayList<Object[]> equipments;
    private final ArrayList<String> items;

    private PlayerSheet()
    {
        this.equipments = null;
        this.items = null;
    }
    
    public PlayerSheet(SheetModel sheet)
    {
        info = new String[sheet.getInfo().length];
        for (int i = 0; i < info.length; i++)
            info[i] = "none";
        about = "";
        extras = "";
        currentStats = new int[sheet.getCurrentStats().length];
        maxStats = new int[sheet.getMaxStats().length];
        attributes = new int[sheet.getAttributes().length];
        skills = new int[sheet.getSkills().length];
        equipments = new ArrayList<>();
        items = new ArrayList<>();
        items.add("Bottle of Water");
        items.add("10x Coins");
    }

    public String[] getInfo()
    {
        return info;
    }

    public void setBasicInfo(String[] basicInfo)
    {
        this.info = basicInfo;
    }

    public String getAbout()
    {
        return about;
    }

    public void setAbout(String about)
    {
        this.about = about;
    }

    public String getExtras()
    {
        return extras;
    }

    public void setExtras(String extras)
    {
        this.extras = extras;
    }

    public int[] getCurrentStats()
    {
        return currentStats;
    }

    public void setCurrentStats(int[] basicStats)
    {
        this.currentStats = basicStats;
    }

    public int[] getMaxStats()
    {
        return maxStats;
    }

    public void setMaxStats(int[] maxStats)
    {
        this.maxStats = maxStats;
    }

    public int[] getAttributes()
    {
        return attributes;
    }

    public void setAttributes(int[] attributes)
    {
        this.attributes = attributes;
    }

    public int[] getSkills()
    {
        return skills;
    }

    public void setSkills(int[] skills)
    {
        this.skills = skills;
    }

    public ArrayList<Object[]> getEquipments()
    {
        return equipments;
    }

    public ArrayList<String> getItems()
    {
        return items;
    }
    
}
