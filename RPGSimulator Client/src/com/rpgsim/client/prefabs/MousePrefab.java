package com.rpgsim.client.prefabs;

import com.rpgsim.common.PrefabID;
import com.rpgsim.common.Vector2;
import com.rpgsim.common.game.Input;
import com.rpgsim.common.game.NetworkGameObject;

public class MousePrefab extends NetworkGameObject
{

    public MousePrefab(int id, int clientID, PrefabID prefabID)
    {
        super(id, clientID, prefabID);
    }

    @Override
    public void update(int clientID, float deltaTime)
    {
        super.update(clientID, deltaTime);
        
        if (clientID != this.getClientID())
            return;
        
        if (Input.mouseMoved())
            setDirty(true);
        
        Vector2 mousePosition = Input.mousePosition();
        transform().position(mousePosition);
    }
    
}
