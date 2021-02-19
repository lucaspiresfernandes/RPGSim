package com.rpgsim.server.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rpgsim.common.FileManager;
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

        String[] attributes = new String[]
        {
            "Strength"
            , "Dexterity"
            , "Intelligence"
            , "Constituition"
            , "Appeal"
            , "Power"
            , "Size"
            , "Education"
            , "Luck"
            , "Move Rate"
        };
        
        String[] attributesDiceNumbers = new String[]
        {
            "100"
            , "100"
            , "100"
            , "100"
            , "100"
            , "100"
            , "100"
            , "100"
            , "100"
            , "100"
        };

        String[] stats = new String[]
        {
            "HP"
            , "Sanity"
            , "MP"
        };

        String[] statsColor = new String[]
        {
            "ff0000"
            , "0000ff"
            , "00ffff"
        };

        String[] skills = new String[]
        {
                "Accounting"
                , "Antrhopology"
                , "Appraise"
                , "Archaeology"
                , "Art/Craft"
                , "Charm"
                , "Climb"
                , "Computer Use"
                , "Disguise"
                , "Dodge"
                , "Drive Auto"
                , "Elec Repair"
                , "Electronics"
                , "Fast Talk"
                , "Fighting"
                , "Firearms(Handgun)"
                , "Firearms(Rifle)"
                , "First Aid"
                , "History"
                , "Intimidate"
                , "Jump"
                , "Language"
                , "Law"
                , "Library Use"
                , "Listen"
                , "Locksmith"
                , "Mech Repair"
                , "Medicine"
                , "Natural World"
                , "Navigate"
                , "Occult"
                , "Op. Hv. Machine"
                , "Persuade"
                , "Pilot"
                , "Psychology"
                , "Psychoanalysis"
                , "Science"
                , "Sleight Of Hand"
                , "Spot Hidden"
                , "Stealth"
                , "Survival"
                , "Swim"
                , "Throw"
                , "Track"
        };

        String[] skillsDiceRolls = new String[]
        {
                "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
                , "100"
        };
        
        String[] equipmentDescriptions = new String[]
        {
            "Name      ",
            "Type ",
            "Damage",
            "Cur. Ammo",
            "Max. Ammo",
            "Attacks",
            "Range",
            "Broken",
            "AoE"
        };
        
        String[] itemDescriptions = new String[]
        {
            "Name",
            "Description   ",
            "Weight"
        };
        
        defaultSheetModel = new SheetModel(info, stats, statsColor, attributes, attributesDiceNumbers, skills, skillsDiceRolls, equipmentDescriptions, itemDescriptions);
        
        try (FileWriter file = new FileWriter(f))
        {
            file.write(gson.toJson(defaultSheetModel));
            file.flush();
        }
    }
    
}
