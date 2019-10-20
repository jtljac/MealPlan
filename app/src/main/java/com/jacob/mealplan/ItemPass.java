package com.jacob.mealplan;

import org.json.JSONObject;

import java.io.File;

public class ItemPass {
    public File file;
    public JSONObject json;
    public ItemPass(File theFile, JSONObject theObject){
        file = theFile;
        json = theObject;
    }
}
