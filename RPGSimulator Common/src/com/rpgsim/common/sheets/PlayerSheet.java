package com.rpgsim.common.sheets;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerSheet implements Serializable
{
    private String[] info;
    private String avatarRelativePath;
    private int[] currentStats;
    private int[] maxStats;
    private int[] attributes;
    private boolean[] attributesMarked;
    private int[] skills;
    private boolean[] skillsMarked;
    private ArrayList<String[]> equipments;
    private ArrayList<String[]> items;

    private PlayerSheet() {}
    
    public PlayerSheet(SheetModel sheet)
    {
        info = new String[sheet.getInfo().length];
        for (int i = 0; i < info.length; i++)
            info[i] = "none";
        
        avatarRelativePath = "data files\\assets\\character.png";
        
        currentStats = new int[sheet.getStats().length];
        for (int i = 0; i < currentStats.length; i++)
            currentStats[i] = 0;
        
        maxStats = new int[sheet.getStats().length];
        for (int i = 0; i < maxStats.length; i++)
            maxStats[i] = 0;
        
        attributes = new int[sheet.getAttributes().length];
        for (int i = 0; i < attributes.length; i++)
            attributes[i] = 0;
        
        attributesMarked = new boolean[sheet.getAttributes().length];
        for (int i = 0; i < attributesMarked.length; i++)
            attributesMarked[i] = false;
        
        skills = new int[sheet.getSkills().length];
        for (int i = 0; i < skills.length; i++)
            skills[i] =0;
        
        skillsMarked = new boolean[sheet.getSkills().length];
        for (int i = 0; i < skillsMarked.length; i++)
            skillsMarked[i] = false;
        
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

    public boolean[] getAttributesMarked()
    {
        return attributesMarked;
    }

    public void setAttributesMarked(boolean[] attributesMarked)
    {
        this.attributesMarked = attributesMarked;
    }

    public int[] getSkills()
    {
        return skills;
    }

    public void setSkills(int[] skills)
    {
        this.skills = skills;
    }

    public boolean[] getSkillsMarked()
    {
        return skillsMarked;
    }

    public void setSkillsMarked(boolean[] skillsMarked)
    {
        this.skillsMarked = skillsMarked;
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
