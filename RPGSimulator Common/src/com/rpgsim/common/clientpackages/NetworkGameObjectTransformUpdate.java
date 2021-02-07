package com.rpgsim.common.clientpackages;

import com.rpgsim.common.ClientActions;
import com.rpgsim.common.Vector2;

public class NetworkGameObjectTransformUpdate extends ClientPackage
{
    private int id;
    private Vector2 position;
    private Vector2 scale;
    private float rotation;
    private boolean flipX, flipY;

    public NetworkGameObjectTransformUpdate()
    {
    }

    public NetworkGameObjectTransformUpdate(int id, Vector2 position, Vector2 scale, float rotation, boolean flipX, boolean flipY)
    {
        this.id = id;
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        this.flipX = flipX;
        this.flipY = flipY;
    }
    
    @Override
    public void executeClientAction(ClientActions client)
    {
        client.updateNetworkGameObjectTransform(id, position, scale, rotation, flipX, flipY);
    }
    
    
}
