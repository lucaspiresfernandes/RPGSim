package com.rpgsim.common.sheets;

import java.io.Serializable;
import java.util.Objects;

public class Account implements Serializable
{
    private final String username, password;
    private PlayerSheet playerSheet;

    public Account()
    {
        this.username = null;
        this.password = null;
    }
    
    public Account(SheetModel model)
    {
        username = "null";
        password = "null";
        playerSheet = new PlayerSheet(model);
    }

    public Account(String username, String password, SheetModel model)
    {
        this.username = username;
        this.password = password;
        playerSheet = new PlayerSheet(model);
    }

    public void setPlayerSheet(PlayerSheet playerSheet)
    {
        this.playerSheet = playerSheet;
    }

    public PlayerSheet getPlayerSheet()
    {
        return playerSheet;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    @Override
    public String toString()
    {
        return username;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.username);
        hash = 67 * hash + Objects.hashCode(this.password);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Account other = (Account) obj;
        if (!Objects.equals(this.username, other.username))
        {
            return false;
        }
        if (!Objects.equals(this.password, other.password))
        {
            return false;
        }
        return true;
    }
    
}
