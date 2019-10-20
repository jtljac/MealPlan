package com.jacob.mealplan;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

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

    public void setup(Context context){
        loadComponents(context);
        loadMeals(context);
    }

    public void loadMeals(Context context){

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
                for (ItemPass conflictedItem:conflicts) {
                    conflictedItem.file.delete();
                    addNewComponent(conflictedItem.json, context);
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewComponent(JSONObject item, Context context) throws JSONException, IOException {
        Integer id = maxComponentID + 1;
        item.put("ID", id);
        File dir = context.getDir("MealComponents", Context.MODE_PRIVATE);
        File file = writeToFile(new File(dir, String.valueOf(id)), item);
        components.append(id, new ItemPass(file, item));
        checkMaxComponentID(id);
    }

    public void modifyComponentByKey(ItemPass item, int key) throws IOException {
        writeToFile(item.file, item.json);
        components.append(key, item);
    }

    public void modifyComponentByPosition(ItemPass item, int position) throws IOException {
        modifyComponentByKey(item, components.keyAt(position));
    }

    private void checkMaxComponentID(int id){
        if (id > maxComponentID) maxComponentID = id;
    }
    private void checkMaxMealID(int id){
        if (id > maxMealID) maxMealID = id;
    }

    private File writeToFile(File file, JSONObject json) throws IOException {
        if(file.exists()) file.delete();
        file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        writer.write(json.toString());
        writer.flush();
        writer.close();
        return file;
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
