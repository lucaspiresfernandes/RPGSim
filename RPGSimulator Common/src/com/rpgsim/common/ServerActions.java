package com.rpgsim.common;

public interface ServerActions
{
    public void onClientConnection(String username, String password, ConnectionType type);
    
    public void onNetworkGameObjectTransformUpdate(int id, Vector2 position, Vector2 scale, float rotation, boolean flipX, boolean flipY);
    
    public void onNetworkGameObjectRequest(Vector2 position, PrefabID pID);
}
