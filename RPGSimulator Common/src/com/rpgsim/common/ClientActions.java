package com.rpgsim.common;

public interface ClientActions
{
    public void onConnectionRequestResponse(boolean accepted, String message, ConnectionType type);
    
    public void onInstantiateNetworkGameObject(int id, int clientID, Vector2 position, PrefabID pID);
    public void updateNetworkGameObjectTransform(int id, Vector2 position, Vector2 scale, float rotation, boolean flipX, boolean flipY);
    public void onNetworkGameObjectDestroy(int id);
}
