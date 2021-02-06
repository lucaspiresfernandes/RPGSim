package com.rpgsim.common;

public interface ServerActions
{
    public void onClientLogin(String username, String password);
    public void onClientRegister(String username, String password);
}
