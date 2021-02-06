package com.rpgsim.common.clientpackages;

import com.rpgsim.common.ClientActions;
import com.rpgsim.common.ConnectionType;

public class ConnectionRequestResponsePackage extends ClientPackage
{
    private boolean accepted;
    private String message;
    private int sessionID;
    private ConnectionType type;

    public ConnectionRequestResponsePackage()
    {
    }

    public ConnectionRequestResponsePackage(boolean accepted, String message, int sessionID, ConnectionType type)
    {
        this.accepted = accepted;
        this.message = message;
        this.sessionID = sessionID;
        this.type = type;
    }
    
    @Override
    public void executeClientAction(ClientActions client)
    {
        
    }

}
