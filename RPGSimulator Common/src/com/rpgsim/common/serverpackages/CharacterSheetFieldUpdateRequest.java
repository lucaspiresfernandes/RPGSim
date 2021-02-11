package com.rpgsim.common.serverpackages;

import com.rpgsim.common.ServerActions;

public class CharacterSheetFieldUpdateRequest extends ServerPackage
{
    private int connectionID;
    private int fieldID;
    private int propertyID;
    private Object value;
    private UpdateType type;

    public CharacterSheetFieldUpdateRequest()
    {
    }

    public CharacterSheetFieldUpdateRequest(int connectionID, int fieldID, int propertyID, Object value, UpdateType type)
    {
        this.connectionID = connectionID;
        this.fieldID = fieldID;
        this.propertyID = propertyID;
        this.value = value;
        this.type = type;
    }
    
    @Override
    public void executeServerAction(ServerActions server)
    {
        server.onCharacterSheetFieldUpdate(connectionID, fieldID, propertyID, value, type);
    }
}
