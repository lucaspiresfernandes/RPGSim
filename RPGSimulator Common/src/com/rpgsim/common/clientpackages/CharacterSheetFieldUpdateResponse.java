package com.rpgsim.common.clientpackages;

import com.rpgsim.common.ClientActions;
import com.rpgsim.common.Vector2;
import com.rpgsim.common.serverpackages.UpdateType;

public class CharacterSheetFieldUpdateResponse extends ClientPackage
{
    private int fieldID;
    private int propertyID;
    private Object value;
    private UpdateType type;

    public CharacterSheetFieldUpdateResponse()
    {
    }

    public CharacterSheetFieldUpdateResponse(int fieldID, int propertyID, Object value, UpdateType type)
    {
        this.fieldID = fieldID;
        this.propertyID = propertyID;
        this.value = value;
        this.type = type;
    }
    
    
    @Override
    public void executeClientAction(ClientActions client)
    {
        client.onCharacterSheetUpdate(fieldID, propertyID, value, type);
    }
}
