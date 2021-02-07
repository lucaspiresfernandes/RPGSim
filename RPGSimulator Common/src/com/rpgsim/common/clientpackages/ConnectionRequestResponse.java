package com.rpgsim.common.clientpackages;

import com.rpgsim.common.ClientActions;
import com.rpgsim.common.ConnectionType;

public class ConnectionRequestResponse extends ClientPackage
{
    private boolean accepted;
    private String message;
    private ConnectionType type;

    public ConnectionRequestResponse()
    {
    }

    public ConnectionRequestResponse(boolean accepted, String message, ConnectionType type)
    {
        this.accepted = accepted;
        this.message = message;
        this.type = type;
    }
    
    @Override
    public void executeClientAction(ClientActions client)
    {
        switch (type)
        {
            case LOGIN:
                client.onLoginRequestResponse(accepted, message);
                break;
            case REGISTER:
                client.onRegisterRequestResponse(accepted, message);
                break;
        }
    }

}
