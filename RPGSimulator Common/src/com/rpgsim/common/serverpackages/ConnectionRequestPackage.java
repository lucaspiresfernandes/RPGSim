package com.rpgsim.common.serverpackages;

import com.rpgsim.common.ConnectionType;
import com.rpgsim.common.ServerActions;

public class ConnectionRequestPackage extends ServerPackage
{
    private String username, password;
    private ConnectionType type;

    public ConnectionRequestPackage()
    {
    }

    public ConnectionRequestPackage(String username, String password, ConnectionType type)
    {
        this.username = username;
        this.password = password;
        this.type = type;
    }
    
    @Override
    public void executeServerAction(ServerActions server)
    {
        
    }

}
