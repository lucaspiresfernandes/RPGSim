package com.rpgsim.client;

import com.rpgsim.common.serverpackages.CharacterSheetFieldUpdateRequest;
import com.rpgsim.common.serverpackages.UpdateType;
import com.rpgsim.common.sheets.PlayerSheet;
import com.rpgsim.common.sheets.SheetModel;
import com.rpgsim.common.sheets.graphics.SheetFrame;

public class ClientSheetFrame extends SheetFrame
{
    private final ClientManager client;

    public ClientSheetFrame(ClientManager client, SheetModel model, PlayerSheet sheet)
    {
        super(model, sheet);
        this.client = client;
    }
    
    @Override
    protected void sendCharacterSheetUpdate(int fieldID, int propertyID, Object newValue, UpdateType type)
    {
        client.sendPackage(new CharacterSheetFieldUpdateRequest(
                client.getConnectionID(), 
                fieldID, 
                propertyID, 
                newValue, 
                type));
    }

}
