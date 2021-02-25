package com.rpgsim.common.sheets;

import java.io.Serializable;

public class ModelDiceField implements Serializable
{
    private final String name;
    private final int diceWeight;

    private ModelDiceField()
    {
        this.name = null;
        this.diceWeight = 0;
    }

    public ModelDiceField(String name, int diceNum)
    {
        this.name = name;
        this.diceWeight = diceNum;
    }

    public String getName()
    {
        return name;
    }

    public int getDiceWeight()
    {
        return diceWeight;
    }

    @Override
    public String toString()
    {
        return name;
    }
    
}
