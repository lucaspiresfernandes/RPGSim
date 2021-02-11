package com.rpgsim.common.serverpackages;

import com.rpgsim.common.ServerActions;
import com.rpgsim.common.Vector2;

public class NetworkGameObjectStateUptadeRequest extends ServerPackage
{
    private int id;
    private Vector2 position;
    private Vector2 scale;
    private float rotation;
    private boolean flipX, flipY;
    private boolean destroyed;

    public NetworkGameObjectStateUptadeRequest() {
    }

    public NetworkGameObjectStateUptadeRequest(int id, Vector2 position, Vector2 scale, float rotation, boolean flipX, boolean flipY, boolean destroyed)
    {
        this.id = id;
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        this.flipX = flipX;
        this.flipY = flipY;
        this.destroyed = destroyed;
    }
    
    @Override
    public void executeServerAction(ServerActions server)
    {
        if (destroyed)
            server.onNetworkGameObjectDestroy(id);
        else
            server.onNetworkGameObjectTransformUpdate(id, position, scale, rotation, flipX, flipY);
    }
    
}
