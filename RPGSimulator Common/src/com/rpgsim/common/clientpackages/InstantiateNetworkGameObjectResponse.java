package com.rpgsim.common.clientpackages;

import com.rpgsim.common.ClientActions;
import com.rpgsim.common.Vector2;

public class InstantiateNetworkGameObjectResponse extends ClientPackage
{
    private int id;
    private Vector2 position;

    public InstantiateNetworkGameObjectResponse() {
    }

    public InstantiateNetworkGameObjectResponse(int id, Vector2 position)
    {
        this.id = id;
        this.position = position;
    }
    
    @Override
    public void executeClientAction(ClientActions client)
    {
        client.onInstantiateNetworkGameObject(id, position);
    }
    
}
