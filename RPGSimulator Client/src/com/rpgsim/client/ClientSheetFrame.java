package com.rpgsim.client;

import com.rpgsim.common.serverpackages.CharacterSheetFieldUpdateRequest;
import com.rpgsim.common.serverpackages.UpdateType;
import com.rpgsim.common.sheets.UpdateField;
import com.rpgsim.common.sheets.graphics.SheetFrame;

public class ClientSheetFrame extends SheetFrame
{
    private final ClientManager client;

    public ClientSheetFrame(ClientManager client)
    {
        this.client = client;
    }
    
    public void onSheetFieldUpdate(UpdateField field, Object newValue, int propertyIndex, UpdateType type)
    {
        switch (type)
        {
            case ADD:
                networkAdd(field, newValue, propertyIndex);
                break;
            case REMOVE:
                networkRemove(field, newValue, propertyIndex);
                break;
            case UPDATE:
                networkUpdate(field, newValue, propertyIndex);
                break;
        }
    }

    @Override
    public void sendSheetUpdate(UpdateField field, Object newValue, int propertyIndex, UpdateType type)
    {
        client.sendPackage(new CharacterSheetFieldUpdateRequest(client.getAccount().getConnectionID(), field, newValue, propertyIndex, type));
    }
    
}
