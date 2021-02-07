package com.rpgsim.common.serverpackages;

import com.rpgsim.common.PrefabID;
import com.rpgsim.common.ServerActions;
import com.rpgsim.common.Vector2;

public class InstantiatePrefabRequest extends ServerPackage
{
    private Vector2 position;
    private PrefabID pID;

    public InstantiatePrefabRequest()
    {
    }

    public InstantiatePrefabRequest(Vector2 position, PrefabID pID)
    {
        this.position = position;
        this.pID = pID;
    }
    
    @Override
    public void executeServerAction(ServerActions server)
    {
        server.onNetworkGameObjectRequest(position, pID);
    }
}
