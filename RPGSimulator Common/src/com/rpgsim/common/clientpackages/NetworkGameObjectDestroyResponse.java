package com.rpgsim.common.clientpackages;

import com.rpgsim.common.ClientActions;

public class NetworkGameObjectDestroyResponse extends ClientPackage
{
    private int id;

    public NetworkGameObjectDestroyResponse() {
    }

    public NetworkGameObjectDestroyResponse(int id) {
        this.id = id;
    }
    
    @Override
    public void executeClientAction(ClientActions client)
    {
        client.onNetworkGameObjectDestroy(id);
    }

}
