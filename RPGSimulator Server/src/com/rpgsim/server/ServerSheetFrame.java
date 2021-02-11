package com.rpgsim.server;

import com.rpgsim.common.clientpackages.CharacterSheetFieldUpdateResponse;
import com.rpgsim.common.serverpackages.UpdateType;
import com.rpgsim.common.sheets.SheetModel;
import com.rpgsim.common.sheets.graphics.SheetFrame;

public class ServerSheetFrame extends SheetFrame
{
    private final ServerManager manager;

    public ServerSheetFrame(ServerManager manager, SheetModel model)
    {
        super(model);
        this.manager = manager;
    }
    
    @Override
    protected void sendCharacterSheetUpdate(int fieldID, int propertyID, Object newValue, UpdateType type)
    {
        manager.sendPackage(new CharacterSheetFieldUpdateResponse(fieldID, propertyID, newValue, type));
    }
    
}
