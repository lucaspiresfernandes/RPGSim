package com.rpgsim.common;

public interface ServerActions
{
    public void clientLogin(String username, String password);
    public void clientRegister(String username, String password);
    public void clientLogoff(int sessionID);
}
