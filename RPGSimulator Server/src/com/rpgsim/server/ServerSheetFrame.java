package com.rpgsim.server;

import com.rpgsim.common.clientpackages.CharacterSheetFieldUpdateResponse;
import com.rpgsim.common.serverpackages.UpdateType;
import com.rpgsim.common.sheets.PlayerSheet;
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
//            PlayerSheet svSheet = activeSheets.get(connectionID);
//            if (sheet.getInfo() != null)
//            {
//                for (int i = 0; i < sheet.getInfo().length; i++)
//                {
//                    String info = sheet.getInfo()[i];
//                    if (info != null)
//                    {
//                        svSheet.getInfo()[i] = info;
//                    }
//                }
//            }
//            
//            if (sheet.getAbout() != null)
//            {
//                svSheet.setAbout(sheet.getAbout());
//            }
//            
//            if (sheet.getExtras() != null)
//            {
//                svSheet.setExtras(sheet.getExtras());
//            }
//            
//            if (sheet.getCurrentStats() != null)
//            {
//                for (int i = 0; i < sheet.getCurrentStats().length; i++)
//                {
//                    String stats = sheet.getCurrentStats()[i];
//                    if (stats != null)
//                    {
//                        svSheet.getCurrentStats()[i] = stats;
//                    }
//                }
//            }
//            
//            if (sheet.getMaxStats() != null)
//            {
//                for (int i = 0; i < sheet.getMaxStats().length; i++)
//                {
//                    String stats = sheet.getMaxStats()[i];
//                    if (stats != null)
//                    {
//                        svSheet.getMaxStats()[i] = stats;
//                    }
//                }
//            }
//            
//            if (sheet.getAttributes() != null)
//            {
//                for (int i = 0; i < sheet.getAttributes().length; i++)
//                {
//                    String attribute = sheet.getAttributes()[i];
//                    if (attribute != null)
//                    {
//                        svSheet.getAttributes()[i] = attribute;
//                    }
//                }
//            }
//            
//            if (sheet.getSkills() != null)
//            {
//                for (int i = 0; i < sheet.getSkills().length; i++)
//                {
//                    String skill = sheet.getSkills()[i];
//                    if (skill != null)
//                    {
//                        svSheet.getSkills()[i] = skill;
//                    }
//                }
//            }
//            
//            if (sheet.getEquipments() != null)
//            {
//                for (String[] equipment : sheet.getEquipments())
//                {
//                    if (svSheet.getEquipments().contains(equipment))
//                    {
//                        svSheet.getEquipments().set(svSheet.getEquipments().indexOf(equipment), equipment);
//                    }
//                }
//            }
//            
//            if (sheet.getItems() != null)
//            {
//                for (String item : sheet.getItems())
//                {
//                    if (svSheet.getItems().contains(item))
//                    {
//                        svSheet.getItems().set(svSheet.getItems().indexOf(item), item);
//                    }
//                }
//            }
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
//            PlayerSheet svSheet = activeSheets.get(connectionID);
//            if (sheet.getEquipments() != null)
//            {
//                for (String[] equipment : sheet.getEquipments())
//                {
//                    svSheet.getEquipments().add(equipment);
//                }
//            }
//
//            if (sheet.getItems() != null)
//            {
//                for (String item : sheet.getItems())
//                {
//                    svSheet.getItems().add(item);
//                }
//            }
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
//            PlayerSheet svSheet = activeSheets.get(connectionID);
//            if (sheet.getEquipments() != null)
//            {
//                for (String[] equipment : sheet.getEquipments())
//                {
//                    svSheet.getEquipments().remove(equipment);
//                }
//            }
//
//            if (sheet.getItems() != null)
//            {
//                for (String item : sheet.getItems())
//                {
//                    svSheet.getItems().remove(item);
//                }
//            }
        }
    }

    @Override
    public void sendSheetUpdate(UpdateField field, Object newValue, int propertyIndex, UpdateType type)
    {
        manager.sendPackage(getConnectionID(), new CharacterSheetFieldUpdateResponse(field, newValue, propertyIndex, type));
    }
    
}
