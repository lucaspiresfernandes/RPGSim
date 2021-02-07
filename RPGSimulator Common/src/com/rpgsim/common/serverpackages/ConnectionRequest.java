package com.rpgsim.common.serverpackages;

import com.rpgsim.common.ConnectionType;
import com.rpgsim.common.ServerActions;

public class ConnectionRequest extends ServerPackage
{
    private String username, password;
    private ConnectionType type;

    public ConnectionRequest()
    {
    }

    public ConnectionRequest(String username, String password, ConnectionType type)
    {
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public ConnectionRequest(ConnectionType type)
    {
        this.username = "null";
        this.password = "null";
        this.type = type;
    }
    
    
    @Override
    public void executeServerAction(ServerActions server)
    {
        server.onClientConnection(username, password, type);
    }

}
