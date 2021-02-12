package com.rpgsim.common.serverpackages;

import com.rpgsim.common.ConnectionType;
import com.rpgsim.common.ServerActions;

public class ConnectionRequest extends ServerPackage
{
    private int connectionID;
    private String username, password;
    private ConnectionType type;

    public ConnectionRequest()
    {
    }

    public ConnectionRequest(int connectionID, String username, String password, ConnectionType type)
    {
        this.connectionID = connectionID;
        this.username = username;
        this.password = password;
        this.type = type;
    }
    
    @Override
    public void executeServerAction(ServerActions server)
    {
        server.onClientConnection(connectionID, username, password, type);
    }

}
