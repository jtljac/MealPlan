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
    // The instance of this
    private static ItemStorage instance;

    // Meals
    public SparseArray<ItemPass> meals = new SparseArray<ItemPass>();
    private int maxMealID = 0;

    // Meal Components
    public SparseArray<ItemPass> components = new SparseArray<ItemPass>();
    private int maxComponentID = 0;

    public static ItemStorage getInstance() {
        /*
         * Get the instance of this class
         * If none exists, then create one
         */
        if(instance == null) instance = new ItemStorage();
        return instance;
    }

    public void setup(Context context){
        /*
         * Setup the meals and components
         */
        loadComponents(context);
        loadMeals(context);
    }

    public void loadMeals(Context context){
        /*
         * Load all of the meals stored on the phone into memory
         */
        // Initialise array tracking conflicts
        ArrayList<ItemPass> conflicts = new ArrayList<>();
        try {
            // Get all the meal json files
            File[] mealFiles = context.getDir("Meals", Context.MODE_PRIVATE).listFiles();
            if (mealFiles != null) {
                Log.i("MEALS", String.valueOf(mealFiles.length));
                ItemPass item;
                // Iterate through all the meal files and add them to memory
                for (File mealFile:mealFiles) {
                    item = new ItemPass(mealFile, new JSONObject(getFileContents(mealFile)));

                    // If the meal's ID conflicts with an existing one, or doesn't exist, then mark it for getting a new one
                    if(!item.json.has("ID") || meals.get(item.json.getInt("ID")) != null){
                        Log.i("MEALS", "Meal ID is in use or Component doesn't have an ID, generating a new one");
                        conflicts.add(item);
                    } else {
                        int id = item.json.getInt("ID");
                        meals.append(id, item);
                        checkMaxMealID(id);
                    }
                }
                // Iterate through all the IDs that had conflicting or non-existent IDs and give them new ones
                for (ItemPass conflictedItem:conflicts) {
                    conflictedItem.file.delete();
                    addNewMeal(conflictedItem.json, context);
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void loadComponents(Context context){
        /*
         * Load all of the components stored on the phone into memory
         */
        // Initialise Array tracking conflicts
        ArrayList<ItemPass> conflicts = new ArrayList<>();
        try {
            // Get all the component Json Files
            File[] componentFiles = context.getDir("MealComponents", Context.MODE_PRIVATE).listFiles();
            if (componentFiles != null) {
                Log.i("MEALS", String.valueOf(componentFiles.length));
                ItemPass item;
                // Iterate through all the component files and add them to memory
                for (File componentFile:componentFiles) {
                    item = new ItemPass(componentFile, new JSONObject(getFileContents(componentFile)));

                    // If the component's ID conflicts with an existing one, or doesn't exist, then mark it for getting a new one
                    if(!item.json.has("ID") || components.get(item.json.getInt("ID")) != null){
                        Log.i("MEALS", "Component ID is in use or Component doesn't have an ID, generating a new one");
                        conflicts.add(item);
                    } else {
                        int id = item.json.getInt("ID");
                        components.append(id, item);
                        checkMaxComponentID(id);
                    }
                }
                // Iterate through all the IDs that had conflicting or non-existent IDs and give them new ones
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
        /*
         * Create a new component, adding it to memory and storing the JSON in a file
         */
        int id = maxComponentID + 1;
        item.put("ID", id);
        // Get the directory that stores the meal components
        File dir = context.getDir("MealComponents", Context.MODE_PRIVATE);
        // Create the name of the file, accounting for files that may already have that name
        StringBuilder name = new StringBuilder(item.getString("Name"));
        File file = new File(dir, name.toString());
        while (file.exists()){
            name.append("1");
            file = new File(dir, name.toString());
        }

        // Write the json to the file and add it to memory
        writeToFile(file, item);
        components.append(id, new ItemPass(file, item));
        checkMaxComponentID(id);
    }

    public void addNewMeal(JSONObject item, Context context) throws JSONException, IOException {
        Integer id = maxMealID + 1;
        item.put("ID", id);
        File dir = context.getDir("Meals", Context.MODE_PRIVATE);
        StringBuilder name = new StringBuilder(item.getString("Name"));
        File file = new File(dir, name.toString());
        while (file.exists()){
            name.append("1");
            file = new File(dir, name.toString());
        }
        writeToFile(file, item);
        meals.append(id, new ItemPass(file, item));
        checkMaxMealID(id);
    }

    public void modifyComponentByKey(ItemPass item, int key) throws IOException {
        writeToFile(item.file, item.json);
        components.append(key, item);
    }

    public void modifyComponentByPosition(ItemPass item, int position) throws IOException {
        modifyComponentByKey(item, components.keyAt(position));
    }

    public void modifyMealByKey(ItemPass item, int key) throws IOException {
        writeToFile(item.file, item.json);
        meals.append(key, item);
    }

    public void modifyMealByPosition(ItemPass item, int position) throws IOException {
        modifyMealByKey(item, meals.keyAt(position));
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
