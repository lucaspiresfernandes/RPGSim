package com.rpgsim.server.util;

import com.rpgsim.common.sheets.DiceField;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rpgsim.common.FileManager;
import com.rpgsim.common.sheets.ColorField;
import com.rpgsim.common.sheets.SheetModel;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SheetManager
{
    private static SheetModel defaultSheetModel;

    public static SheetModel getDefaultSheetModel()
    {
        return defaultSheetModel;
    }
    
    public void checkSheetModel() throws IOException
    {
        File f = new File(FileManager.app_dir + "data\\sheetmodel.json");
        if (!f.exists())
        {
            write(f);
        }
        else
        {
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            
            try (FileReader reader = new FileReader(f))
            {
                defaultSheetModel = gson.fromJson(reader, SheetModel.class);
            }
        }
    }
    
    private void write(File f) throws IOException
    {
        f.createNewFile();
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        
        String[] info = new String[]
        {
            "Name",
            "Player",
            "Occupation",
            "Age",
            "Sex",
            "Residence",
            "Birthplace"
        };

        DiceField[] attributes = new DiceField[]
        {
            new DiceField("Strength", 100),
            new DiceField("Dexterity", 100),
            new DiceField("Power", 100),
            new DiceField("Constituition", 100),
            new DiceField("Appeal", 100),
            new DiceField("Education", 100),
            new DiceField("Size", 100),
            new DiceField("Inteligence", 100),
            new DiceField("Damage Bonus", 100),
            new DiceField("Build", 100),
            new DiceField("Build", 100),
            new DiceField("Move Rate", 100),
            new DiceField("Maximum Load", 100)
        };

        ColorField[] stats = new ColorField[]
        {
            new ColorField("HP", 0xff0000),
            new ColorField("Sanity", 0x0000ff),
            new ColorField("MP", 0x00ffff)
        };

        DiceField[] skills = new DiceField[]
        {
            new DiceField("Accounting", 100),
            new DiceField("Antrhopology", 100),
            new DiceField("Appraise", 100),
            new DiceField("Archaeology", 100),
            new DiceField("Specialization: Art/Craft", 100),
            new DiceField("Charm", 100),
            new DiceField("Climb", 100),
            new DiceField("Computer Use", 100),
            new DiceField("Credit Rating", 100),
            new DiceField("Cthulhu Mythos", 100),
            new DiceField("Disguise", 100),
            new DiceField("Dodge", 100),
            new DiceField("Drive Auto", 100),
            new DiceField("Elec Repair", 100),
            new DiceField("Electronics", 100),
            new DiceField("Fast Talk", 100),
            new DiceField("Specialization: Fighting", 100),
            new DiceField("Specialization: Firearms", 100),
            new DiceField("First Aid", 100),
            new DiceField("History", 100),
            new DiceField("Intimidate", 100),
            new DiceField("Jump", 100),
            new DiceField("Language (Other)", 100),
            new DiceField("Language (Own)", 100),
            new DiceField("Law", 100),
            new DiceField("Library Use", 100),
            new DiceField("Listen", 100),
            new DiceField("Locksmith", 100),
            new DiceField("Mech Repair", 100),
            new DiceField("Medicine", 100),
            new DiceField("Natural World", 100),
            new DiceField("Navigate", 100),
            new DiceField("Occult", 100),
            new DiceField("Op. Hv. Machine", 100),
            new DiceField("Persuade", 100),
            new DiceField("Specialization: Pilot", 100),
            new DiceField("Psychology", 100),
            new DiceField("Psychoanalysis", 100),
            new DiceField("Specialization: Science", 100),
            new DiceField("Sleight Of Hand", 100),
            new DiceField("Spot Hidden", 100),
            new DiceField("Stealth", 100),
            new DiceField("Specialization: Survival", 100),
            new DiceField("Swim", 100),
            new DiceField("Throw", 100),
            new DiceField("Track", 100)
        };
        
        String[] equipmentDescriptions = new String[]
        {
            "Name",
            "Skill",
            "Damage",
            "Range", 
            "Uses per round",
            "Ammo",
            "Malfunction"
        };
        
        String[] itemDescriptions = new String[]
        {
            "Name",
            "Weight"
        };
        
        defaultSheetModel = new SheetModel(info, stats, attributes, skills, equipmentDescriptions, itemDescriptions);
        
        try (FileWriter file = new FileWriter(f))
        {
            file.write(gson.toJson(defaultSheetModel));
            file.flush();
        }
    }
    
}
