package com.rpgsim.common.sheets;

import java.io.Serializable;

public class ModelDiceField implements Serializable
{
    private final String name;
    private final int diceNumModel;

    private ModelDiceField()
    {
        this.name = null;
        this.diceNumModel = 0;
    }

    public ModelDiceField(String name, int diceNum)
    {
        this.name = name;
        this.diceNumModel = diceNum;
    }

    public String getName()
    {
        return name;
    }

    public int getDiceNumModel()
    {
        return diceNumModel;
    }

    @Override
    public String toString()
    {
        return name;
    }
    
}
