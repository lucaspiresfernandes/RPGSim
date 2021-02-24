package com.rpgsim.common.sheets;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerSheet implements Serializable
{
    private String[] info;
    private String about;
    private String extras;
    private String avatarRelativePath;
    private int[] currentStats;
    private int[] maxStats;
    private int[] attributes;
    private boolean[] attributeMarked;
    private int[] skills;
    private boolean[] skillMarked;
    private ArrayList<String[]> equipments;
    private ArrayList<String[]> items;

    private PlayerSheet() {}
    
    public PlayerSheet(SheetModel sheet)
    {
        info = new String[sheet.getInfo().length];
        for (int i = 0; i < info.length; i++)
            info[i] = "none";
        
        about = "";
        extras = "";
        
        avatarRelativePath = "data files\\objects\\character.png";
        
        currentStats = new int[sheet.getStats().length];
        for (int i = 0; i < currentStats.length; i++)
            currentStats[i] = 0;
        
        maxStats = new int[sheet.getStats().length];
        for (int i = 0; i < maxStats.length; i++)
            maxStats[i] = 0;
        
        attributes = new int[sheet.getAttributes().length];
        for (int i = 0; i < attributes.length; i++)
            attributes[i] = 0;
        attributeMarked = new boolean[attributes.length];
        for (int i = 0; i < attributeMarked.length; i++)
            attributeMarked[i] = false;
        
        skills = new int[sheet.getSkills().length];
        for (int i = 0; i < skills.length; i++)
            skills[i] = 0;
        skillMarked = new boolean[skills.length];
        for (int i = 0; i < skillMarked.length; i++)
            skillMarked[i] = false;
        
        equipments = new ArrayList<>();
        items = new ArrayList<>();
    }

    public String[] getInfo()
    {
        return info;
    }

    public void setInfo(String[] info)
    {
        this.info = info;
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

    public String getAvatarRelativePath()
    {
        return avatarRelativePath;
    }

    public void setAvatarRelativePath(String avatarRelativePath)
    {
        this.avatarRelativePath = avatarRelativePath;
    }
    
    public void setCurrentStats(int[] currentStats)
    {
        this.currentStats = currentStats;
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

    public boolean[] getAttributeMarked()
    {
        return attributeMarked;
    }

    public void setAttributeMarked(boolean[] attributeMarked)
    {
        this.attributeMarked = attributeMarked;
    }

    public int[] getSkills()
    {
        return skills;
    }

    public void setSkills(int[] skills)
    {
        this.skills = skills;
    }

    public boolean[] getSkillMarked()
    {
        return skillMarked;
    }

    public void setSkillMarked(boolean[] skillMarked)
    {
        this.skillMarked = skillMarked;
    }
    
    public ArrayList<String[]> getEquipments()
    {
        return equipments;
    }

    public void setEquipments(ArrayList<String[]> equipments)
    {
        this.equipments = equipments;
    }

    public ArrayList<String[]> getItems()
    {
        return items;
    }

    public void setItems(ArrayList<String[]> items)
    {
        this.items = items;
    }

    
    
}
