package com.jacob.mealplan;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.jacob.mealplan.meal.MealViewActivity;
import com.jacob.mealplan.ui.mealcomponents.MakeMealComponentDialogFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ItemStorage.getInstance().setup(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_meals, R.id.nav_mealcomponents)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        if(!getIntent().getBooleanExtra("Meal", true)){
            NavigationUI.onNavDestinationSelected(navigationView.getMenu().getItem(1), navController);
            Log.i("Test", "TESTER");
        } else {
            Log.i("Test", "TESTES");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DialogFragment confirmBox;
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_delete_meals:
                // Popup are you sure
                confirmBox = new ConfirmDeleteAllDialogFragment(true, navigationView.getMenu().getItem(0).isChecked(), this);
                confirmBox.show(getSupportFragmentManager(), "TAG");
                return true;
            case R.id.action_delete_components:
                confirmBox = new ConfirmDeleteAllDialogFragment(false, navigationView.getMenu().getItem(0).isChecked(), this);
                confirmBox.show(getSupportFragmentManager(), "TAG");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void rebootActivity(boolean meal){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Meal", meal);
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
