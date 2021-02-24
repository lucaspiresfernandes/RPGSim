package com.rpgsim.common.sheets;

import java.io.Serializable;

public class SheetDiceField implements Serializable
{
    private int diceNum;
    private boolean marked;

    private SheetDiceField()
    {
    }

    public SheetDiceField(int diceNum, boolean marked)
    {
        this.diceNum = diceNum;
        this.marked = marked;
    }

    public int getDiceNum()
    {
        return diceNum;
    }

    public void setDiceNum(int diceNum)
    {
        this.diceNum = diceNum;
    }

    public boolean isMarked()
    {
        return marked;
    }

    public void setMarked(boolean marked)
    {
        this.marked = marked;
    }
    
}
