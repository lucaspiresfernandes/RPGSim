package com.rpgsim.server.util;

import com.rpgsim.common.sheets.Account;
import com.rpgsim.common.FileManager;
import com.rpgsim.common.sheets.PlayerSheet;
import com.rpgsim.common.sheets.SheetDiceField;
import com.rpgsim.common.sheets.SheetModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountManager
{
    private final HashMap<Integer, Account> activeAccounts = new HashMap<>();
    private final File accountFile = new File(FileManager.app_dir + "data\\accounts\\");
    
    /**
     * Check all accounts and its sheets.
     * 
     * @throws java.io.IOException standard I/O exceptions.
     */
    public void checkAccounts() throws IOException
    {
        for (File f : accountFile.listFiles())
        {
            boolean changed = false;
            Account acc = null;
            
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f)))
            {
                acc = (Account) in.readObject();
                PlayerSheet sheet = acc.getPlayerSheet();
                SheetModel model = SheetManager.getDefaultSheetModel();
                
                if (model.getInfo().length != sheet.getInfo().length)
                {
                    int oldLength = sheet.getInfo().length;
                    sheet.setInfo(Arrays.copyOf(sheet.getInfo(), model.getInfo().length));
                    if (oldLength < sheet.getInfo().length)
                    {
                        for (int i = oldLength; i < sheet.getInfo().length; i++)
                        {
                            sheet.getInfo()[i] = "none";
                        }
                    }
                    changed = true;
                }
                
                if (model.getStats().length != sheet.getCurrentStats().length)
                {
                    sheet.setCurrentStats(Arrays.copyOf(sheet.getCurrentStats(), model.getStats().length));
                    sheet.setMaxStats(Arrays.copyOf(sheet.getMaxStats(), model.getStats().length));
                    changed = true;
                }
                
                if (model.getAttributes().length != sheet.getAttributes().length)
                {
                    sheet.setAttributes(Arrays.copyOf(sheet.getAttributes(), model.getAttributes().length));
                    sheet.setAttributesMarked(Arrays.copyOf(sheet.getAttributesMarked(), model.getAttributes().length));
                    changed = true;
                }
                
                if (model.getSkills().length != sheet.getSkills().length)
                {
                    sheet.setSkills(Arrays.copyOf(sheet.getSkills(), model.getSkills().length));
                    sheet.setSkillsMarked(Arrays.copyOf(sheet.getSkillsMarked(), model.getSkills().length));
                    changed = true;
                }
                
                for (int i = 0; i < sheet.getEquipments().size(); i++)
                {
                    String[] equipment = sheet.getEquipments().get(i);
                    if (equipment.length != model.getEquipmentDescriptions().length)
                    {
                        int oldLength = equipment.length;
                        sheet.getEquipments().set(i, Arrays.copyOf(equipment, model.getEquipmentDescriptions().length));
                        
                        if (oldLength < model.getEquipmentDescriptions().length)
                        {
                            for (int j = oldLength; j < model.getEquipmentDescriptions().length; j++)
                            {
                                sheet.getEquipments().get(i)[j] = "none";
                            }
                        }
                        changed = true;
                    }
                }
                
                for (int i = 0; i < sheet.getItems().size(); i++)
                {
                    String[] item = sheet.getItems().get(i);
                    if (item.length != model.getItemDescriptions().length)
                    {
                        int oldLength = item.length;
                        sheet.getItems().set(i, Arrays.copyOf(item, model.getItemDescriptions().length));
                        
                        if (oldLength < model.getItemDescriptions().length)
                        {
                            for (int j = oldLength; j < model.getItemDescriptions().length; j++)
                            {
                                sheet.getItems().get(i)[j] = "none";
                            }
                        }
                        changed = true;
                    }
                }
                
            }
            catch (ClassNotFoundException ex)
            {
                Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (changed)
            {
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f)))
                {
                    out.writeObject(acc);
                }
            }
        }
    }
    
    public boolean isAccountRegistered(String username, String password)
    {
        File f = new File(accountFile, username + ".dat");
        return f.exists();
    }
    
    public Account getRegisteredAccount(String username, String password) throws IOException
    {
        File f = new File(accountFile, username + ".dat");
        if (!f.exists())
            return null;
        
        Account acc = null;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f)))
        {
            acc = (Account) in.readObject();
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return acc;
    }
    
    public Account registerNewAccount(int connectionID, String username, String password, SheetModel model) throws IOException
    {
        File f = new File(accountFile, username + ".dat");
        Account acc = new Account(connectionID, username, password, model);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f)))
        {
            out.writeObject(acc);
        }
        return acc;
    }
    
    public void updateAccountSheet(Account acc) throws IOException
    {
        File f = new File(accountFile, acc.getUsername() + ".dat");
        acc.setPlayerSheet(acc.getPlayerSheet());
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f)))
        {
            out.writeObject(acc);
        }
    }

    public boolean isAccountActive(String username, String password)
    {
        for (Account acc : activeAccounts.values())
        {
            if (acc.getUsername().equals(username) && acc.getPassword().equals(password))
                return true;
        }
        return false;
    }
    
    public Account getActiveAccount(int sessionID)
    {
        return activeAccounts.get(sessionID);
    }

    public HashMap<Integer, Account> getActiveAccounts()
    {
        return activeAccounts;
    }

    public void setAccountActive(int sessionID, Account acc, boolean active)
    {
        if (active)
            activeAccounts.put(sessionID, acc);
        else
            activeAccounts.remove(sessionID, acc);
    }

}
