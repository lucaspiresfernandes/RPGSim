package com.rpgsim.common.clientpackages;

import com.rpgsim.common.ClientActions;

public class DestroyNetworkGameObject extends ClientPackage
{
    private int id;

    public DestroyNetworkGameObject() {
    }

    public DestroyNetworkGameObject(int id) {
        this.id = id;
    }
    
    @Override
    public void executeClientAction(ClientActions client)
    {
        client.onNetworkGameObjectDestroy(id);
    }

}
