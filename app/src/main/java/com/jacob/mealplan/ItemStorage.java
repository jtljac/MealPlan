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
    public SparseArray<ItemPass> meals = new SparseArray<>();
    private int maxMealID = 0;

    // Meal Components
    public SparseArray<ItemPass> components = new SparseArray<>();
    private int maxComponentID = 0;

    public static ItemStorage getInstance() {
        /*
         * Get the instance of this class
         * If none exists, then create one
         */
        if(instance == null) instance = new ItemStorage();
        return instance;
    }

    void setup(Context context){
        /*
         * Setup the meals and components
         */
        loadComponents(context);
        loadMeals(context);
    }

    private void loadMeals(Context context){
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

    private void loadComponents(Context context){
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
        writeToJsonFile(file, item);
        components.append(id, new ItemPass(file, item));
        checkMaxComponentID(id);
    }

    public void addNewMeal(JSONObject item, Context context) throws JSONException, IOException {
        /*
         * Create a new meal, adding it to memory and stores the json in a file
         */
        int id = maxMealID + 1;
        item.put("ID", id);
        // Get the Meal Directory
        File dir = context.getDir("Meals", Context.MODE_PRIVATE);

        // Create the name of the file
        StringBuilder name = new StringBuilder(item.getString("Name"));
        File file = new File(dir, name.toString());
        while (file.exists()){
            name.append("1");
            file = new File(dir, name.toString());
        }

        // Write the JSON to the file and add it to memory
        writeToJsonFile(file, item);
        meals.append(id, new ItemPass(file, item));
        checkMaxMealID(id);
    }

    public void modifyComponentByKey(ItemPass item, int key) throws IOException {
        /*
         * Modify a component
         * Takes the item to modify, and the key of the object
         */
        writeToJsonFile(item.file, item.json);
        components.append(key, item);
    }

    public void modifyComponentByPosition(ItemPass item, int position) throws IOException {
        /*
         * Modify a component
         * Takes the item to modify, and the position of the object
         */
        modifyComponentByKey(item, components.keyAt(position));
    }

    public void modifyMealByKey(ItemPass item, int key) throws IOException {
        /*
         * Modify a meal
         * Takes the item to modify, and the key of the object
         */
        writeToJsonFile(item.file, item.json);
        meals.append(key, item);
    }

    public void deleteMealByKey(int key){
        /*
         * Delete a meal
         * Takes the key of the meal to delete
         */
        ItemPass meal = meals.get(key);
        meal.file.delete();
        meals.remove(key);
    }

    public void deleteMealByPosition(int position){
        /*
         * Delete a meal
         * Takes the position of the meal to delete
         */
        deleteMealByKey(meals.keyAt(position));
    }

    public void deleteComponentByKey(int key){
        /*
         * Delete a component
         * Takes the key of the meal to delete
         */
        ItemPass component = components.get(key);
        component.file.delete();
        components.remove(key);
    }

    public void deleteAllMeals(){
        for (int i=0; i < meals.size();i++){
            meals.valueAt(i).file.delete();
        }
        meals.clear();
    }

    public void deleteComponentByPosition(int position){
        /*
         * Delete a component
         * Takes the position of the meal to delete
         */
        deleteComponentByKey(components.keyAt(position));
    }

    public void modifyMealByPosition(ItemPass item, int position) throws IOException {
        /*
         * Modify a Meal
         * Takes the item to modify, and the position of the object
         */
        modifyMealByKey(item, meals.keyAt(position));
    }

    public void deleteAllComponents(){
        for (int i=0; i < components.size();i++){
            components.valueAt(i).file.delete();
        }
        components.clear();
    }

    private void checkMaxComponentID(int id){
        /*
         * Check if the given ID is larger than the current stored one, stores the new ID if it is
         */
        if (id > maxComponentID) maxComponentID = id;
    }
    private void checkMaxMealID(int id){
        /*
         * Check if the given ID is larger than the current stored one, stores the new ID if it is
         */
        if (id > maxMealID) maxMealID = id;
    }

    private File writeToJsonFile(File file, JSONObject json) throws IOException {
        /*
         * Writes the given json to the given file
         */
        // Delete the file if it already exists, we're definitely overwriting it
        if(file.exists()) file.delete();
        file.createNewFile();

        // Create a writer
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
        writer.write(json.toString());

        // Force the writer to save the file and close it
        writer.flush();
        writer.close();
        return file;
    }

    private static String getFileContents(File file) throws IOException {
        /*
         * Get the Contents of the given File
         */
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
