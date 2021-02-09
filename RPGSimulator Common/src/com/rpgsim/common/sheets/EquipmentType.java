package com.rpgsim.common.sheets;

public enum EquipmentType
{
    ARMOR("Armor"), LIGHT_MELEE("Light Melee"), HEAVY_MELEE("Heavy Melee"), 
    LIGHT_RANGED("Light Ranged"), HEAVY_RANGED("Heavy Ranged"), OTHER("Other");
    
    private final String name;

    private EquipmentType(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    
}
