package com.rpgsim.common.serverpackages;

import com.rpgsim.common.ServerActions;
import com.rpgsim.common.sheets.UpdateField;

public class CharacterSheetFieldUpdateRequest extends ServerPackage
{
    private int connectionID;
    private UpdateField field;
    private Object newValue;
    private int propertyIndex;
    private UpdateType type;

    public CharacterSheetFieldUpdateRequest()
    {
    }

    public CharacterSheetFieldUpdateRequest(int connectionID, UpdateField field, Object newValue, int propertyIndex, UpdateType type) {
        this.connectionID = connectionID;
        this.field = field;
        this.newValue = newValue;
        this.propertyIndex = propertyIndex;
        this.type = type;
    }
    
    @Override
    public void executeServerAction(ServerActions server)
    {
        server.onCharacterSheetFieldUpdate(connectionID, field, newValue, propertyIndex, type);
    }
}
