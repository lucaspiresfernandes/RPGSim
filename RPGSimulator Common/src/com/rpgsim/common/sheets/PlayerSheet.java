package com.rpgsim.common.sheets;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class PlayerSheet implements Serializable
{
    private String[] basicInfo;
    private String about;
    private String extras;
    private int[] basicStats;
    private int[] attributes;
    private int[] skills;
    private ArrayList<Equipment> equipments;
    private ArrayList<Item> items;

    public PlayerSheet()
    {
    }
    
    public PlayerSheet(SheetModel sheet)
    {
        basicInfo = new String[sheet.getBasicInfo().length];
        for (int i = 0; i < basicInfo.length; i++)
            basicInfo[i] = "none";
        about = "";
        extras = "";
        basicStats = new int[sheet.getBasicStats().length];
        attributes = new int[sheet.getAttributes().length];
        skills = new int[sheet.getSkills().length];
        equipments = new ArrayList<>();
        equipments.add(new Equipment("Sword", EquipmentType.HEAVY_MELEE, "10 + 1d10", "0", "0", "1", "2m", false, false));
        equipments.add(new Equipment("Shotgun", EquipmentType.HEAVY_RANGED, "20 + 1d15", "3", "6", "1", "15m", false, true));
        items = new ArrayList<>();
    }

    public String[] getBasicInfo()
    {
        return basicInfo;
    }

    public void setBasicInfo(String[] basicInfo)
    {
        this.basicInfo = basicInfo;
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

    public int[] getBasicStats()
    {
        return basicStats;
    }

    public void setBasicStats(int[] basicStats)
    {
        this.basicStats = basicStats;
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

    public ArrayList<Equipment> getEquipments()
    {
        return equipments;
    }

    public void setEquipments(ArrayList<Equipment> equipments)
    {
        this.equipments = equipments;
    }

    public ArrayList<Item> getItems()
    {
        return items;
    }

    public void setItems(ArrayList<Item> items)
    {
        this.items = items;
    }
    
}
