package com.bizcall.wayto.mentebit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityHome extends AppCompatActivity {
    Toolbar toolbar = null;
    DrawerLayout drawer;
    NavigationView navigationView;
    String clientUrl, clientId;
    private long back_pressed = 0;
    Intent intent;
    Toast toast;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private DrawerLayout mDrawerLayout;
    View view_Group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);

        sp = getSharedPreferences("Settings", 0); // 0 - for private mode
        clientUrl = sp.getString("ClientUrl", null);
        clientId = sp.getString("ClientId", null);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);

        navigationView.setItemIconTintList(null);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        prepareListData();
        mMenuAdapter = new ExpandableListAdapter(ActivityHome.this, listDataHeader, listDataChild);

        // setting list adapter
        expandableList.setAdapter(mMenuAdapter);
        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                // Toast.makeText(Home.this, "Header: " + String.valueOf(groupPosition) + "\nItem: " + String.valueOf(childPosition), Toast.LENGTH_SHORT).show();

                final String selected = (String) mMenuAdapter.getChild(groupPosition, childPosition);

                // id = (String) mMenuAdapter.getChild(groupPosition, childPosition);
                Intent intent;
                switch (selected) {
                    //--------------------------------Admin Reports-------------------------------
                    case "Refwise Lead":
                        intent = new Intent(ActivityHome.this, ActivityTotalLeadReport.class);
                        intent.putExtra("ActivityName", "Refwise Report");
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideLeft(ActivityHome.this);
                        break;
                    case "Statuswise Lead":
                        intent = new Intent(ActivityHome.this, ActivityTotalLeadReport.class);
                        intent.putExtra("ActivityName", "Statuswise Report");
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideLeft(ActivityHome.this);
                        break;
                    case "Datewise Lead":
                        intent = new Intent(ActivityHome.this, ActivityTotalLeadReport.class);
                        intent.putExtra("ActivityName", "Datewise Report");
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideLeft(ActivityHome.this);
                        break;
                    case "Counselorwise Lead":
                        intent = new Intent(ActivityHome.this, ActivityTotalLeadReport.class);
                        intent.putExtra("ActivityName", "Counselorwise Report");
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideLeft(ActivityHome.this);
                        break;

                    //--------------------------------Master Entry-------------------------------
                   /* case "Counselor Data Master":
                        intent = new Intent(ActivityHome.this, ActivityAddCounselor.class);
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideLeft(ActivityHome.this);
                        break;
                    case "Status Button Master":
                        intent = new Intent(ActivityHome.this, ActivityStatusButton.class);
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideLeft(ActivityHome.this);
                        break;
                    *//*case "College Mater Page":
                        intent = new Intent(ActivityHome.this, GraphReport.class);
                        intent.putExtra("ActivityName", "CallLog Report");
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideLeft(ActivityHome.this);
                        break;*//**/
                    //--------------------------------Report-------------------------------
                    case "Call Log Report":
                        intent = new Intent(ActivityHome.this, ActivityGraphReport.class);
                        intent.putExtra("ActivityName", "CallLog Report");
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideLeft(ActivityHome.this);
                        break;
                    case "Location Report":
                        intent = new Intent(ActivityHome.this, ActivityMapLocations.class);
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideLeft(ActivityHome.this);
                        break;
                    case "Total Calls Made":
                        intent = new Intent(ActivityHome.this, ActivityTotalCallMade.class);
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideLeft(ActivityHome.this);
                        break;
                    case "Reallocation Report":
                        intent = new Intent(ActivityHome.this, ActivityAllocationReport.class);
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideLeft(ActivityHome.this);
                        break;
                   /* case "First Call Report":
                        intent = new Intent(ActivityHome.this, FirstCallReport.class);
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideLeft(ActivityHome.this);
                        break;*/
                    case "Call Report":
                        intent = new Intent(ActivityHome.this, ActivityGraphReport.class);
                        intent.putExtra("ActivityName", "Call Report");
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideLeft(ActivityHome.this);
                        break;
                    case "Message Report":
                        intent = new Intent(ActivityHome.this, ActivityGraphReport.class);
                        intent.putExtra("ActivityName", "Message Report");
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideLeft(ActivityHome.this);
                        break;
                    case "Status Report":
                        intent = new Intent(ActivityHome.this, ActivityStatusReport.class);
                        intent.putExtra("ActivityName", "Status Report");
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideLeft(ActivityHome.this);
                        break;
                    case "Status Report DataRefwise":
                        intent = new Intent(ActivityHome.this, ActivityStatusReport.class);
                        intent.putExtra("ActivityName", "Status Report DataRefwise");
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideLeft(ActivityHome.this);
                        break;
                    case "Remark Report":
                        intent = new Intent(ActivityHome.this, ActivityGraphReport.class);
                        intent.putExtra("ActivityName", "Remark Report");
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideLeft(ActivityHome.this);
                        break;
                    case "Point Report":
                        intent = new Intent(ActivityHome.this, ActivityGraphReport.class);
                        intent.putExtra("ActivityName", "Point Report");
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideLeft(ActivityHome.this);
                        break;
                    case "Login Time Report":
                        intent = new Intent(ActivityHome.this, ActivityLastLoginDetails.class);
                        intent.putExtra("ActivityName", "Login Report");
                        startActivity(intent);
                        finish();
                        Animatoo.animateSlideLeft(ActivityHome.this);
                        break;
                    //--------------------------------LogOut-------------------------------
                    case "LogOut":
                        intent = new Intent(ActivityHome.this, Login.class);
                        startActivity(intent);
                        finish();
                        Animatoo.animateFade(ActivityHome.this);
                        break;
                }
                view.setSelected(true);
                if (view_Group != null) {
                    view_Group.setBackgroundColor(Color.parseColor("#ffffff"));
                }
                view_Group = view;
                view_Group.setBackgroundColor(Color.parseColor("#DDDDDD"));
                drawer.closeDrawers();
                return false;
            }
        });

        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {

                //Log.d("DEBUG", "heading clicked");

                return false;
            }
        });
    }

    private void prepareListData() {
        try {
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();
            // Adding data header
            listDataHeader.add("Admin Reports");
            listDataHeader.add("Master Entry");
            listDataHeader.add("Document Master");
            listDataHeader.add("Account Master");
            listDataHeader.add("Counselor Reports");
            listDataHeader.add("Logout");

            // Adding child data
            List<String> heading1 = new ArrayList<String>();
            heading1.add("Refwise Lead");
            heading1.add("Statuswise Lead");
            heading1.add("Datewise Lead");
            heading1.add("Counselorwise Lead");

            List<String> heading2 = new ArrayList<String>();
            heading2.add("Counselor Data Master");
            heading2.add("Status Button Master");
            heading2.add("College Master Page");

            List<String> heading3 = new ArrayList<String>();

            List<String> heading4 = new ArrayList<String>();

            List<String> heading5 = new ArrayList<String>();
            heading5.add("Call Log Report");
            heading5.add("Location Report");
            heading5.add("Total Calls Made");
            heading5.add("Reallocation Report");
            heading5.add("First Call Report");
            heading5.add("Call Report");
            heading5.add("Message Report");
            heading5.add("Status Report");
            heading5.add("Status Report DataRefwise");
            heading5.add("Remark Report");
            heading5.add("Point Report");
            heading5.add("Login Time Report");

            List<String> heading6 = new ArrayList<String>();
            heading6.add("LogOut");

            listDataChild.put(listDataHeader.get(0), heading1);// Header, Child data
            listDataChild.put(listDataHeader.get(1), heading2);
            listDataChild.put(listDataHeader.get(2), heading3);
            listDataChild.put(listDataHeader.get(3), heading4);
            listDataChild.put(listDataHeader.get(4), heading5);
            listDataChild.put(listDataHeader.get(5), heading6);
        } catch (Exception e) {
            Toast.makeText(ActivityHome.this, "Errorcode-129 Dashboard NavigationMenu " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        try {
                            menuItem.setChecked(true);
                            mDrawerLayout.closeDrawers();

                        } catch (Exception e) {
                            Toast.makeText(ActivityHome.this, "Errorcode-130 Dashboard NavigationItemSelected " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }

                });
    }

    @Override
    public void onBackPressed() {
        try {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (back_pressed + 2000 > System.currentTimeMillis()) {
                // need to cancel the toast here
                toast.cancel();
                intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //finish();
            } else {
                // ask user to press back button one more time to close app
                toast = Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT);
                toast.show();
            }
            back_pressed = System.currentTimeMillis();
        } catch (Exception e) {
            Toast.makeText(ActivityHome.this, "Errorcode-147 Dashboard OnBackpressed " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
