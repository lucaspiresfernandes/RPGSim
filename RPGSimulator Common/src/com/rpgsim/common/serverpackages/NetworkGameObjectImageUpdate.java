package com.rpgsim.common.serverpackages;

import com.rpgsim.common.ServerActions;

public class NetworkGameObjectImageUpdate extends ServerPackage
{
    private int objectID;
    private String relativePath;

    public NetworkGameObjectImageUpdate() {
    }

    public NetworkGameObjectImageUpdate(int objectID, String relativePath) {
        this.objectID = objectID;
        this.relativePath = relativePath;
    }
    
    @Override
    public void executeServerAction(ServerActions server)
    {
        server.onNetworkGameObjectImageUpdate(objectID, relativePath);
    }

}
