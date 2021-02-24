package com.rpgsim.server;

import com.rpgsim.common.clientpackages.CharacterSheetFieldUpdateResponse;
import com.rpgsim.common.serverpackages.UpdateType;
import com.rpgsim.common.sheets.PlayerSheet;
import com.rpgsim.common.sheets.SheetDiceField;
import com.rpgsim.common.sheets.UpdateField;
import com.rpgsim.common.sheets.graphics.SheetFrame;
import java.util.HashMap;

public class ServerSheetFrame extends SheetFrame
{
    private final ServerManager manager;
    private final HashMap<Integer, PlayerSheet> activeSheets = new HashMap<>();
    
    public ServerSheetFrame(ServerManager manager)
    {
        this.manager = manager;
    }
    
    public void addActiveSheet(int connectionID, PlayerSheet sheet)
    {
        this.activeSheets.put(connectionID, sheet);
    }
    
    public void removeActiveSheet(int connectionID)
    {
        this.activeSheets.remove(connectionID);
    }
    
    public void onSheetFieldUpdateReceived(int connectionID, UpdateField field, Object newValue, int propertyIndex)
    {
        
        if (connectionID == getConnectionID())
        {
            networkUpdate(field, newValue, propertyIndex);
        }
        else
        {
            PlayerSheet svSheet = activeSheets.get(connectionID);
            switch (field)
            {
                case INFO:
                    svSheet.getInfo()[propertyIndex] = (String) newValue;
                    break;
                case CUR_STATS:
                    svSheet.getCurrentStats()[propertyIndex] = (int) newValue;
                    break;
                case MAX_STATS:
                    svSheet.getMaxStats()[propertyIndex] = (int) newValue;
                    break;
                case ATTRIBUTES:
                    svSheet.getAttributes()[propertyIndex] = (int) newValue;
                    break;
                case ATTRIBUTES_MARK:
                    svSheet.getAttributesMarked()[propertyIndex] = (boolean) newValue;
                    break;
                case SKILLS:
                    svSheet.getSkills()[propertyIndex] = (int) newValue;
                    break;
                case SKILLS_MARK:
                    svSheet.getSkillsMarked()[propertyIndex] = (boolean) newValue;
                    break;
                case EQUIPMENTS:
                    svSheet.getEquipments().set(propertyIndex, (String[]) newValue);
                    break;
                case ITEMS:
                    svSheet.getItems().set(propertyIndex, (String[]) newValue);
                    break;
            }
        }
    }
    
    public void onSheetFieldAddReceived(int connectionID, UpdateField field, Object newValue, int propertyIndex)
    {
        if (connectionID == getConnectionID())
        {
            networkAdd(field, newValue, propertyIndex);
        }
        else
        {
            PlayerSheet svSheet = activeSheets.get(connectionID);
            switch (field)
            {
                case EQUIPMENTS:
                    svSheet.getEquipments().add((String[]) newValue);
                    break;
                case ITEMS:
                    svSheet.getItems().add((String[]) newValue);
                    break;
            }
        }
    }
    
    public void onSheetFieldRemoveReceived(int connectionID, UpdateField field, Object newValue, int propertyIndex)
    {
        if (connectionID == getConnectionID())
        {
            networkRemove(field, newValue, propertyIndex);
        }
        else
        {
            PlayerSheet svSheet = activeSheets.get(connectionID);
            switch (field)
            {    
                case EQUIPMENTS:
                    svSheet.getEquipments().remove(propertyIndex);
                    break;
                case ITEMS:
                    svSheet.getItems().remove(propertyIndex);
                    break;
            }
        }
    }

    @Override
    public void sendSheetUpdate(UpdateField field, Object newValue, int propertyIndex, UpdateType type)
    {
        manager.sendPackage(getConnectionID(), new CharacterSheetFieldUpdateResponse(field, newValue, propertyIndex, type));
    }
    
}
