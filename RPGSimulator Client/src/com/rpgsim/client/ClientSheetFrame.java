package com.rpgsim.client;

import com.rpgsim.common.sheets.graphics.SheetFrame;

public class ClientSheetFrame extends SheetFrame
{
    private final ClientManager client;

    public ClientSheetFrame(ClientManager client)
    {
        super(client.getConnectionID());
        this.client = client;
    }

}
