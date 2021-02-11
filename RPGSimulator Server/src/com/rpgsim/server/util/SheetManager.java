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
    public static SheetModel defaultSheetModel;
    
    private final String[] basicInformations = new String[]
    {
        "Name",
        "Player",
        "Occupation",
        "Age",
        "Sex",
        "Residence",
        "Birthplace"
    };
    
    private final String[] attributes = new String[]
    {
        "Strength"
        , "Dexterity"
        , "Intelligence"
        , "Constituition"
        , "Appeal"
        , "Power"
        , "Size"
        , "Education"
        , "Move Rate"
    };
    
    private final String[] basicStats = new String[]
    {
        "Current HP"
        , "Max HP"
        , "Current Sanity"
        , "Max Sanity"
        , "Luck"
        , "Current MP"
        , "Max MP"
    };
    
    private final String[] skills = new String[]
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
        
        defaultSheetModel = new SheetModel(basicInformations, basicStats, attributes, skills);
        
        try (FileWriter file = new FileWriter(f))
        {
            file.write(gson.toJson(defaultSheetModel));
            file.flush();
        }
    }
    
}
