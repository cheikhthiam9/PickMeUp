package com.example.pickmeup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    private DrawerLayout drawerLayout;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = database.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        toolbar = findViewById(R.id.toolbar_dashboard);
        //Setting new action bar created in xml file
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        //displaying a navigation menu from a menu resource.
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Making the Dashboard Fragment Appear as soon as the activity is created
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
        navigationView.setCheckedItem(R.id.dashboard);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.dashboard:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DashboardFragment()).commit();
                break;

            case R.id.add_package:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddPackageFragment()).commit();
                break;
            case R.id.shipment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PackagesFragment()).commit();
                break;
//            case R.id.request:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new PickUpRequestFragment()).commit();
//                break;
//            case R.id.report:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new ReportsFragment()).commit();
//                break;
//            case R.id.billing:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new BillingFragment()).commit();
//                break;
            case R.id.my_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MyAccountFragment()).commit();
                break;
//            case R.id.settings:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new SettingsFragment()).commit();
//                break;
            case R.id.how_it_works:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HelpFragment()).commit();
                break;
            case R.id.shipping_calculator:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ShippingCalculatorFragment()).commit();
                break;
            case R.id.sign_out:

                if (currentUser != null && firebaseAuth != null) {
                    firebaseAuth.signOut();
                    startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                    finish();
                }

                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
