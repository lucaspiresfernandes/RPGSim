package com.rpgsim.server.util;

import com.rpgsim.common.sheets.ModelDiceField;
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

        ModelDiceField[] attributes = new ModelDiceField[]
        {
            new ModelDiceField("Strength", 100),
            new ModelDiceField("Dexterity", 100),
            new ModelDiceField("Power", 100),
            new ModelDiceField("Constituition", 100),
            new ModelDiceField("Appeal", 100),
            new ModelDiceField("Education", 100),
            new ModelDiceField("Size", 100),
            new ModelDiceField("Inteligence", 100),
            new ModelDiceField("Damage Bonus", 100),
            new ModelDiceField("Build", 100),
            new ModelDiceField("Build", 100),
            new ModelDiceField("Move Rate", 100),
            new ModelDiceField("Maximum Load", 100)
        };

        ColorField[] stats = new ColorField[]
        {
            new ColorField("HP", 0xff0000),
            new ColorField("Sanity", 0x0000ff),
            new ColorField("MP", 0x00ffff)
        };

        ModelDiceField[] skills = new ModelDiceField[]
        {
            new ModelDiceField("Accounting", 100),
            new ModelDiceField("Antrhopology", 100),
            new ModelDiceField("Appraise", 100),
            new ModelDiceField("Archaeology", 100),
            new ModelDiceField("Art/Craft: Acting", 100),
            new ModelDiceField("Art/Craft: Barber", 100),
            new ModelDiceField("Art/Craft: Calligraphy", 100),
            new ModelDiceField("Art/Craft: Carpenter", 100),
            new ModelDiceField("Art/Craft: Cook", 100),
            new ModelDiceField("Art/Craft: Dancer", 100),
            new ModelDiceField("Art/Craft: Fine Art", 100),
            new ModelDiceField("Art/Craft: Forgery", 100),
            new ModelDiceField("Art/Craft: Writer", 100),
            new ModelDiceField("Art/Craft: Morris Dancer", 100),
            new ModelDiceField("Art/Craft: Opera Singer", 100),
            new ModelDiceField("Art/Craft: Painter and Decorator", 100),
            new ModelDiceField("Art/Craft: Photographer", 100),
            new ModelDiceField("Art/Craft: Potter", 100),
            new ModelDiceField("Art/Craft: Sculptor", 100),
            new ModelDiceField("Charm", 100),
            new ModelDiceField("Climb", 100),
            new ModelDiceField("Computer Use", 100),
            new ModelDiceField("Credit Rating", 100),
            new ModelDiceField("Cthulhu Mythos", 100),
            new ModelDiceField("Disguise", 100),
            new ModelDiceField("Dodge", 100),
            new ModelDiceField("Drive Auto", 100),
            new ModelDiceField("Elec Repair", 100),
            new ModelDiceField("Electronics", 100),
            new ModelDiceField("Fast Talk", 100),
            new ModelDiceField("Fighting: Axe", 100),
            new ModelDiceField("Fighting: Brawl", 100),
            new ModelDiceField("Fighting: Flail", 100),
            new ModelDiceField("Fighting: Garrote", 100),
            new ModelDiceField("Fighting: Spear", 100),
            new ModelDiceField("Fighting: Sword", 100),
            new ModelDiceField("Fighting: Whip", 100),
            new ModelDiceField("Fighting: Bow", 100),
            new ModelDiceField("Firearms: Handgun", 100),
            new ModelDiceField("Firearms: Heavy Weapons", 100),
            new ModelDiceField("Firearms: Flamethrower", 100),
            new ModelDiceField("Firearms: Machine Gun", 100),
            new ModelDiceField("Firearms: Rifle/Shotgun", 100),
            new ModelDiceField("Firearms: Submachine Gun", 100),
            new ModelDiceField("First Aid", 100),
            new ModelDiceField("History", 100),
            new ModelDiceField("Intimidate", 100),
            new ModelDiceField("Jump", 100),
            new ModelDiceField("Language (Other)", 100),
            new ModelDiceField("Language (Own)", 100),
            new ModelDiceField("Law", 100),
            new ModelDiceField("Library Use", 100),
            new ModelDiceField("Listen", 100),
            new ModelDiceField("Locksmith", 100),
            new ModelDiceField("Mech Repair", 100),
            new ModelDiceField("Medicine", 100),
            new ModelDiceField("Natural World", 100),
            new ModelDiceField("Navigate", 100),
            new ModelDiceField("Occult", 100),
            new ModelDiceField("Op. Hv. Machine", 100),
            new ModelDiceField("Persuade", 100),
            new ModelDiceField("Pilot: Aircraft", 100),
            new ModelDiceField("Pilot: Boat", 100),
            new ModelDiceField("Psychology", 100),
            new ModelDiceField("Psychoanalysis", 100),
            new ModelDiceField("Science", 100),
            new ModelDiceField("Sleight Of Hand", 100),
            new ModelDiceField("Spot Hidden", 100),
            new ModelDiceField("Stealth", 100),
            new ModelDiceField("Survival", 100),
            new ModelDiceField("Swim", 100),
            new ModelDiceField("Throw", 100),
            new ModelDiceField("Track", 100)
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
            "Description",
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
