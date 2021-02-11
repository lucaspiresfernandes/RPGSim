package com.rpgsim.common.clientpackages;

import com.rpgsim.common.ClientActions;

public class NetworkGameObjectImageUpdateResponse extends ClientPackage
{
    private int objectID;
    private String relativePath;

    public NetworkGameObjectImageUpdateResponse() {
    }

    public NetworkGameObjectImageUpdateResponse(int objectID, String relativePath)
    {
        this.objectID = objectID;
        this.relativePath = relativePath;
    }
    
    @Override
    public void executeClientAction(ClientActions client)
    {
        client.onNetworkGameObjectImageChange(objectID, relativePath);
    }

}
