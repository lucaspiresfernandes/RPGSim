package com.rpgsim.common.serverpackages;

import com.rpgsim.common.PrefabID;
import com.rpgsim.common.ServerActions;
import com.rpgsim.common.Vector2;

public class InstantiatePrefabRequest extends ServerPackage
{
    private Vector2 position;
    private PrefabID pID;
    int clientID;
    private String imageRelativePath;

    public InstantiatePrefabRequest()
    {
    }

    public InstantiatePrefabRequest(Vector2 position, PrefabID pID, int clientID, String imageRelativePath) {
        this.position = position;
        this.pID = pID;
        this.clientID = clientID;
        this.imageRelativePath = imageRelativePath;
    }
    
    @Override
    public void executeServerAction(ServerActions server)
    {
        server.onNetworkGameObjectRequest(position, pID, clientID, imageRelativePath);
    }
}
