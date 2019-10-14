package com.jacob.mealplan.ui.mealcomponents;

import org.json.JSONObject;

import java.io.File;

public class componentPass {
    public File file;
    public JSONObject json;
    componentPass(File theFile, JSONObject theObject){
        file = theFile;
        json = theObject;
    }
}
