package com.rpgsim.common;

public interface ServerActions
{
    public void onClientConnection(String username, String password, ConnectionType type);
    
    public void onNetworkGameObjectPositionChanged(int id, Vector2 newPosition);
    
    public void onNetworkGameObjectRequest(boolean clientAuthority);
}
