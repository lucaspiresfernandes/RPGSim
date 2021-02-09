package com.rpgsim.common.clientpackages;

import com.rpgsim.common.ClientActions;
import com.rpgsim.common.ConnectionType;
import com.rpgsim.common.sheets.Account;
import com.rpgsim.common.sheets.SheetModel;

public class ConnectionRequestResponse extends ClientPackage
{
    private boolean accepted;
    private String message;
    private Account account;
    private SheetModel model;
    private ConnectionType type;

    public ConnectionRequestResponse()
    {
    }

    public ConnectionRequestResponse(boolean accepted, String message, Account account, SheetModel model, ConnectionType type) {
        this.accepted = accepted;
        this.message = message;
        this.account = account;
        this.model = model;
        this.type = type;
    }
    
    @Override
    public void executeClientAction(ClientActions client)
    {
        client.onConnectionRequestResponse(accepted, message, account, model, type);
    }

}
