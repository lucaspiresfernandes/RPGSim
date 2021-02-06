package com.rpgsim.server;

import com.rpgsim.common.Account;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class AccountManager
{
    private ArrayList<Account> registeredAccounts;
    private final HashMap<Integer, Account> activeAccounts = new HashMap<>();

    public AccountManager()
    {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(FileManager.app_dir + "accounts.bin"))))
        {
            registeredAccounts = (ArrayList<Account>) in.readObject();
        }
        catch (EOFException ex)
        {
            registeredAccounts = new ArrayList<>();
            System.out.println("accounts.bin is empty.");
        }
        catch (IOException ex)
        {
            registeredAccounts = new ArrayList<>();
            JOptionPane.showMessageDialog(null, "Accounts.bin is either corrupted or not found. Reseting accounts.");
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex)
        {
            JOptionPane.showMessageDialog(null, "Unknown error. Please contact application owner at <GITHUB>", "Unknown error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        }
    }

    public boolean isAccountRegistered(Account acc)
    {
        return registeredAccounts.contains(acc);
    }
    
    public void registerNewAccount(Account acc)
    {
        registeredAccounts.add(acc);
    }

    public boolean isAccountActive(Account acc)
    {
        return activeAccounts.containsValue(acc);
    }
    
    public Account getActiveAccount(int sessionID)
    {
        return activeAccounts.get(sessionID);
    }

    public void setAccountActive(int sessionID, Account acc, boolean active)
    {
        
        if (active)
        {
            if (!isAccountRegistered(acc))
                throw new IllegalStateException("This account is not registered.");
            activeAccounts.put(sessionID, acc);
        }
        else
            activeAccounts.remove(sessionID, acc);
    }
    
    public void saveAccounts()
    {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(FileManager.app_dir + "accounts.bin"))))
        {
            out.writeObject(registeredAccounts);
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null, "Could not save accounts.");
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
