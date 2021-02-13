package com.rpgsim.common.clientpackages;

import com.rpgsim.common.ClientActions;
import com.rpgsim.common.serverpackages.UpdateType;
import com.rpgsim.common.sheets.UpdateField;

public class CharacterSheetFieldUpdateResponse extends ClientPackage
{
    private UpdateField field;
    private Object newValue;
    private int propertyIndex;
    private UpdateType type;

    public CharacterSheetFieldUpdateResponse()
    {
    }

    public CharacterSheetFieldUpdateResponse(UpdateField field, Object newValue, int propertyIndex, UpdateType type)
    {
        this.field = field;
        this.newValue = newValue;
        this.propertyIndex = propertyIndex;
        this.type = type;
    }
    
    @Override
    public void executeClientAction(ClientActions client)
    {
        client.onCharacterSheetUpdate(field, newValue, propertyIndex, type);
    }
}
