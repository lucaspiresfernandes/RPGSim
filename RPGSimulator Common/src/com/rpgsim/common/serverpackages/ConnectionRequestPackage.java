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

    public ConnectionRequestPackage(ConnectionType type)
    {
        this.username = "null";
        this.password = "null";
        this.type = type;
    }
    
    
    @Override
    public void executeServerAction(ServerActions server)
    {
        switch (type)
        {
            case LOGIN:
                server.onClientLogin(username, password);
                break;
            case REGISTER:
                server.onClientRegister(username, password);
                break;
        }
    }

}
