package com.rpgsim.common.clientpackages;

import com.rpgsim.common.ClientActions;
import com.rpgsim.common.PrefabID;
import com.rpgsim.common.Vector2;

public class InstantiateNetworkGameObjectResponse extends ClientPackage
{
    private int id;
    private int clientID;
    private Vector2 position;
    private PrefabID pID;
    private String imageRelativePath;

    public InstantiateNetworkGameObjectResponse()
    {
    }

    public InstantiateNetworkGameObjectResponse(int id, int clientID, Vector2 position, PrefabID pID, String imageRelativePath) {
        this.id = id;
        this.clientID = clientID;
        this.position = position;
        this.pID = pID;
        this.imageRelativePath = imageRelativePath;
    }
    
    @Override
    public void executeClientAction(ClientActions client)
    {
        client.onInstantiateNetworkGameObject(id, clientID, position, pID, imageRelativePath);
    }
    
}
