package com.rpgsim.common;

public interface ClientActions
{
    public void onLoginRequestResponse(boolean accepted, String message);
    public void onRegisterRequestResponse(boolean accepted, String message);
    
    public void onInstantiateNetworkGameObject(int id, Vector2 position);
}
