package com.rpgsim.server;

import com.rpgsim.common.serverpackages.ServerPackage;

public class ClientRequest
{
    private int connectionID;
    private ServerPackage request;

    public ClientRequest(int connectionID, ServerPackage request)
    {
        this.connectionID = connectionID;
        this.request = request;
    }

    public int getConnectionID()
    {
        return connectionID;
    }

    public ServerPackage getRequest()
    {
        return request;
    }
    
}
