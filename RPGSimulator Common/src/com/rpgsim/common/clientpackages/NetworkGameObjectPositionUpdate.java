package com.rpgsim.common.clientpackages;

import com.rpgsim.common.ClientActions;
import com.rpgsim.common.Vector2;

public class NetworkGameObjectPositionUpdate extends ClientPackage
{
    private int id;
    private Vector2 position;

    public NetworkGameObjectPositionUpdate() {
    }

    public NetworkGameObjectPositionUpdate(int id, Vector2 position)
    {
        this.id = id;
        this.position = position;
    }

    @Override
    public void executeClientAction(ClientActions client)
    {
        
    }
    
    
}
