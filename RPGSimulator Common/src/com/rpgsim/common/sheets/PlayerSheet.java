package com.rpgsim.common.sheets;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerSheet implements Serializable
{
    private String[] info;
    private String about;
    private String extras;
    private String[] currentStats;
    private String[] maxStats;
    private String[] attributes;
    private String[] skills;
    private ArrayList<String[]> equipments;
    private ArrayList<String> items;

    public PlayerSheet()
    {
        this.info = null;
        this.about = null;
        this.extras = null;
        this.currentStats = null;
        this.maxStats = null;
        this.attributes = null;
        this.skills = null;
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
        currentStats = new String[sheet.getCurrentStats().length];
        for (int i = 0; i < currentStats.length; i++)
            currentStats[i] = "0";
        maxStats = new String[sheet.getMaxStats().length];
        for (int i = 0; i < maxStats.length; i++)
            maxStats[i] = "0";
        attributes = new String[sheet.getAttributes().length];
        for (int i = 0; i < attributes.length; i++)
            attributes[i] = "0";
        skills = new String[sheet.getSkills().length];
        for (int i = 0; i < skills.length; i++)
            skills[i] = "0";
        equipments = new ArrayList<>();
        items = new ArrayList<>();
        items.add("Bottle of Water");
        items.add("10x Coins");
    }

    public String[] getInfo() {
        return info;
    }

    public void setInfo(String[] info) {
        this.info = info;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public String[] getCurrentStats() {
        return currentStats;
    }

    public void setCurrentStats(String[] currentStats) {
        this.currentStats = currentStats;
    }

    public String[] getMaxStats() {
        return maxStats;
    }

    public void setMaxStats(String[] maxStats) {
        this.maxStats = maxStats;
    }

    public String[] getAttributes() {
        return attributes;
    }

    public void setAttributes(String[] attributes) {
        this.attributes = attributes;
    }

    public String[] getSkills() {
        return skills;
    }

    public void setSkills(String[] skills) {
        this.skills = skills;
    }

    public ArrayList<String[]> getEquipments() {
        return equipments;
    }

    public void setEquipments(ArrayList<String[]> equipments) {
        this.equipments = equipments;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    
    
}
