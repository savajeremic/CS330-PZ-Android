package com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.R;
import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.fragments.GamesFragment;
import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.fragments.HelpFragment;
import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.fragments.MapsActivity;
import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.fragments.UsersProfileFragment;
import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.fragments.WelcomeFragment;
import com.gwentdb.projekat.cs330_pz_sava_jeremic_2733.sql.DBHelper;

import java.io.ByteArrayInputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView welcomeUser;
    private String email, username;
    private TextView tvEmail, tvUsername;
    static final String STATE_USER = "user";
    DBHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbhelper = new DBHelper(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Contact Via")
                        .setItems(R.array.helper, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                if(which == 0){
                                    Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: +381643395733"));
                                    startActivity(intentCall);
                                }else if(which == 1){
                                    String[] to =
                                            {"jack9666@gmail.com"};
                                    String[] cc = null;
                                    sendEmail(to,null,"HELP - [YOUR SUBJECT PROBLEM HERE]", "Dear Sava, \n[DESCRIBE PROBLEM HERE] \n Regards,\n" + username);
                                }
                            }
                        });
                builder.create();
                builder.show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String email = getIntent().getStringExtra("EMAIL");
        String username = getIntent().getStringExtra("NAME");

        System.out.println("NAME: " + username + " EMAIL: " + email + "  ");

        View v =  navigationView.getHeaderView(0);
        CircleImageView avatar = (CircleImageView)v.findViewById(R.id.profile_image);
        byte[] photo = dbhelper.getImage(email);
        if(photo != null){
            ByteArrayInputStream imgStream = new ByteArrayInputStream(photo);
            Bitmap bm= BitmapFactory.decodeStream(imgStream);
            try{
                if(bm != null){
                    avatar.setImageBitmap(bm);
                } else{
                    Toast.makeText(this, "Image is null", Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        try {
            tvUsername = (TextView) v.findViewById(R.id.welcomeName);
            tvUsername.setText(username);

            tvEmail = (TextView) v.findViewById(R.id.welcomeMail);
            tvEmail.setText(email);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(STATE_USER, email);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState = getIntent().getExtras();
        //email = getIntent().getStringExtra("EMAIL");
        //username = getIntent().getStringExtra("NAME");
        try {
            if (email.toString() != null || !TextUtils.isEmpty(email)) {
                welcomeUser.setText("Welcome " + email);
                //tvUsername.setText(username);
                //tvEmail.setText(email);
            } else if (savedInstanceState == null) {
                Toast.makeText(this, "Bundle is null", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e){
            e.printStackTrace();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_help:
                Fragment fragment;
                fragment = new HelpFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        FragmentActivity fragmentActivity = null;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            try {
                fragment = new WelcomeFragment();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else if (id == R.id.nav_profile) {
            String username = getIntent().getStringExtra("NAME");
            String email = getIntent().getStringExtra("EMAIL");
            try {
                Class fragmentClass;
                fragmentClass = UsersProfileFragment.class;
                fragment = (Fragment) fragmentClass.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString("NAME", username);
                bundle.putString("EMAIL", email);
                fragment.setArguments(bundle);
                fragment = new UsersProfileFragment();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else if (id == R.id.nav_map) {
            try {
                Intent maps = new Intent(this, MapsActivity.class);
                startActivity(maps);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else if (id == R.id.nav_games) {
            try {
                fragment = new GamesFragment();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else if (id == R.id.nav_help) {
            try {
                fragment = new HelpFragment();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else if (id == R.id.nav_logout){
            finish();
        }

        if (fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.main_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(!drawer.isDrawerOpen(GravityCompat.START)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to logout?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.show();
        } else {
            super.onBackPressed();
        }
    }

    public void call(View v){
        Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: +381643395733"));
        startActivity(intentCall);
    }

    public void visit(View v){
        Intent mapsIntent = new Intent(this, MapsActivity.class);
        startActivity(mapsIntent);
    }

    public void mail(View v){
        String[] to =
                {"jack9666@gmail.com"};
        String[] cc = null;
        sendEmail(to,null,"HELP - [YOUR SUBJECT PROBLEM HERE]", "Dear Sava, \n[DESCRIBE PROBLEM HERE] \n Regards,\n" + username);

    }

    private void sendEmail(String[] emailAddresses, String[] carbonCopies, String subject, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        String[] to = emailAddresses;
        String[] cc = carbonCopies;
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_CC, cc);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Email"));
    }

    /*
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
     */
}
