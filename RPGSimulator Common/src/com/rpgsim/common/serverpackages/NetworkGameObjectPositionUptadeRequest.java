package com.rpgsim.common.serverpackages;

import com.rpgsim.common.ServerActions;
import com.rpgsim.common.Vector2;

public class NetworkGameObjectPositionUptadeRequest extends ServerPackage
{
    private int id;
    private Vector2 position;
    private Vector2 scale;
    private float rotation;
    private boolean flipX, flipY;

    public NetworkGameObjectPositionUptadeRequest() {
    }

    public NetworkGameObjectPositionUptadeRequest(int id, Vector2 position, Vector2 scale, float rotation, boolean flipX, boolean flipY)
    {
        this.id = id;
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        this.flipX = flipX;
        this.flipY = flipY;
    }
    
    @Override
    public void executeServerAction(ServerActions server)
    {
        server.onNetworkGameObjectPositionChanged(id, position, scale, rotation, flipX, flipY);
    }
    
}
