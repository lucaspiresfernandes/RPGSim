package com.rpgsim.common.sheets;

import java.io.Serializable;

public class DiceField implements Serializable
{
    private final String name;
    private final int diceNum;

    private DiceField()
    {
        this.name = null;
        this.diceNum = 0;
    }

    public DiceField(String name, int diceNum)
    {
        this.name = name;
        this.diceNum = diceNum;
    }

    public String getName()
    {
        return name;
    }

    public int getDiceNum()
    {
        return diceNum;
    }
    
    
}
