package com.rpgsim.server;

import com.rpgsim.common.sheets.graphics.SheetFrame;

public class ServerSheetFrame extends SheetFrame
{
    private final ServerManager manager;
    
    public ServerSheetFrame(int connectionID, ServerManager manager)
    {
        super(connectionID);
        this.manager = manager;
    }
    
}
