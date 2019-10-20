package com.jacob.mealplan;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemStorage {
    private static ItemStorage instance;

    public SparseArray<ItemPass> meals = new SparseArray<ItemPass>();
    private int maxMealID = 0;

    public SparseArray<ItemPass> components = new SparseArray<ItemPass>();
    private int maxComponentID = 0;

    public static ItemStorage getInstance() {
        if(instance == null) instance = new ItemStorage();
        return instance;
    }

    public void loadComponents(Context context){
        ArrayList<ItemPass> conflicts = new ArrayList<>();
        try {
            File[] componentFiles = context.getDir("MealComponents", Context.MODE_PRIVATE).listFiles();
            if (componentFiles != null) {
                Log.i("MEALS", String.valueOf(componentFiles.length));
                ItemPass item;
                for (File componentFile:componentFiles) {
                    item = new ItemPass(componentFile, new JSONObject(getFileContents(componentFile)));

                    if(!item.json.has("ID") || components.get(item.json.getInt("ID")) != null){
                        Log.i("MEALS", "Component ID is in use or Component doesn't have an ID, generating a new one");
                        conflicts.add(item);
                    } else {
                        components.append(maxComponentID, item);
                        checkMaxComponentID(item.json.getInt("ID"));
                    }
                }
                for (item:conflicts) {

                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addComponent(ItemPass item) throws JSONException {
        Integer id = maxComponentID + 1;
        item.json.put("ID", id);
        checkMaxComponentID(id);
    }

    private void checkMaxComponentID(int id){
        if (id > maxComponentID) maxComponentID = id;
    }
    private void checkMaxMealID(int id){
        if (id > maxMealID) maxMealID = id;
    }

    private static String getFileContents(File file) throws IOException {
        // http://www.java2s.com/Code/Java/File-Input-Output/ConvertInputStreamtoString.htm
        FileInputStream fin = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fin));
        StringBuilder sb = new StringBuilder();
        String line = null;
        boolean firstLine = true;
        while ((line = reader.readLine()) != null) {
            if(firstLine){
                sb.append(line);
                firstLine = false;
            } else {
                sb.append("\n").append(line);
            }
        }
        reader.close();
        fin.close();
        return sb.toString();
    }
}
