package com.rpgsim.common.sheets;

import java.io.Serializable;

public class Equipment implements Serializable
{
    private String name;
    private EquipmentType type;
    private String damage;
    private String currentAmmo;
    private String maxAmmo;
    private String attacks;
    private String range;
    private boolean broken;
    private boolean areaOfEffect;

    public Equipment()
    {
        name = "none";
        type = EquipmentType.OTHER;
        damage = "0";
        currentAmmo = "0";
        maxAmmo = "0";
        attacks = "0";
        range = "0";
        broken = false;
        areaOfEffect = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EquipmentType getType() {
        return type;
    }

    public void setType(EquipmentType type) {
        this.type = type;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public String getCurrentAmmo() {
        return currentAmmo;
    }

    public void setCurrentAmmo(String currentAmmo) {
        this.currentAmmo = currentAmmo;
    }

    public String getMaxAmmo() {
        return maxAmmo;
    }

    public void setMaxAmmo(String maxAmmo) {
        this.maxAmmo = maxAmmo;
    }

    public String getAttacks() {
        return attacks;
    }

    public void setAttacks(String attacks) {
        this.attacks = attacks;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
    }

    public boolean isAreaOfEffect() {
        return areaOfEffect;
    }

    public void setAreaOfEffect(boolean areaOfEffect) {
        this.areaOfEffect = areaOfEffect;
    }
    
}
