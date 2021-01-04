package com.ideal2s.achatmarket;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MenuHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    GridView gridRayon;
    static String NumVersion = "2";
    ConnectionClass connectionClass;
    String user, password, base, ip, NomUtilisateur = "";
    final Context co = this;
    ProgressBar pbbar;
    private static final int REQUEST_CAMERARESULT=201;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(getApplicationContext().checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                ///method to get Images

            }else{
                if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, REQUEST_CAMERARESULT);
            }
        }else{

        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("usersession", Context.MODE_PRIVATE);
                SharedPreferences.Editor edt = pref.edit();
                edt.putBoolean("etat", false);
                edt.commit();
                Intent inte = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(inte);
            }
        });

        ////session base
        SharedPreferences pref = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        user = pref.getString("user", user);
        ip = pref.getString("ip", ip);
        password = pref.getString("password", password);
        base = pref.getString("base", base);
        connectionClass = new ConnectionClass();

        TestVerionTask testVerionTask=new TestVerionTask();
        testVerionTask.execute("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        CardView cardEntree = (CardView) findViewById(R.id.bonentree);
        CardView cardReception = (CardView) findViewById(R.id.bonreception);
        CardView cardFacture = (CardView) findViewById(R.id.factureachat);
        CardView cardAvoir = (CardView) findViewById(R.id.avoir);
        cardEntree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent(getApplicationContext(), AjoutBEActivity.class);
                startActivity(intent);

            }
        });

        cardReception.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AjoutBRActivity.class);
                startActivity(intent);

            }
        });

        cardFacture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AjoutFactureActivity.class);
                startActivity(intent);

            }
        });
        cardAvoir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AjoutBonAvoir.class);
                startActivity(intent);

            }
        });
        ////SESSION UTILISATEUR
        SharedPreferences prefe = getSharedPreferences("usersession", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();
        NomUtilisateur = prefe.getString("NomUtilisateur", NomUtilisateur);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.user);

        navUsername.setText(NomUtilisateur);







    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
            LayoutInflater li = LayoutInflater.from(co);
            View px = li.inflate(R.layout.print, null);
            final AlertDialog.Builder alt = new AlertDialog.Builder(co);
            alt.setIcon(R.drawable.i2s);
            alt.setTitle("Paramètre");
            alt.setView(px);


            final EditText edtuserid = (EditText) px.findViewById(R.id.edtuserid);
            final EditText edtpass = (EditText) px.findViewById(R.id.edtpass);

            alt.setCancelable(false)
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface di, int i) {

                                    if (edtuserid.getText().toString().equals("admin") && edtpass.getText().toString().equals("admin")) {
                                        SharedPreferences pref = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor edt = pref.edit();
                                        edt.putBoolean("etatsql", false);
                                        edt.commit();
                                        Intent inte = new Intent(getApplicationContext(), Parametrage.class);
                                        startActivity(inte);
                                    } else {


                                        Toast.makeText(getApplicationContext(), "Vérifiez vos données ", Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                    .setNegativeButton("Annuler",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface di, int i) {
                                    di.cancel();
                                }
                            });
            final AlertDialog d = alt.create();


            d.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    //   d.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundResource(R.drawable.bt);
                    //  d.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundResource(R.drawable.bt);


                }
            });

            d.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_deconnecter) {
            SharedPreferences pref = getSharedPreferences("usersession", Context.MODE_PRIVATE);
            SharedPreferences.Editor edt = pref.edit();
            edt.putBoolean("etat", false);
            edt.commit();
            Intent inte = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(inte);
        } else if (id == R.id.nav_bonentree) {

            Intent inte = new Intent(getApplicationContext(), AjoutBEActivity.class);
            startActivity(inte);
        } else if (id == R.id.nav_bonreception) {

            Intent inte = new Intent(getApplicationContext(), AjoutBRActivity.class);
            startActivity(inte);
        } else if (id == R.id.nav_facture) {

            Intent inte = new Intent(getApplicationContext(), AjoutFactureActivity.class);
            startActivity(inte);
        } else if (id == R.id.nav_parametre) {
            LayoutInflater li = LayoutInflater.from(co);
            View px = li.inflate(R.layout.print, null);
            final AlertDialog.Builder alt = new AlertDialog.Builder(co);
            alt.setIcon(R.drawable.i2s);
            alt.setTitle("Paramètre");
            alt.setView(px);


            final EditText edtuserid = (EditText) px.findViewById(R.id.edtuserid);
            final EditText edtpass = (EditText) px.findViewById(R.id.edtpass);

            alt.setCancelable(false)
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface di, int i) {

                                    if (edtuserid.getText().toString().equals("admin") && edtpass.getText().toString().equals("admin")) {
                                        SharedPreferences pref = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor edt = pref.edit();
                                        edt.putBoolean("etatsql", false);
                                        edt.commit();
                                        Intent inte = new Intent(getApplicationContext(), Parametrage.class);
                                        startActivity(inte);
                                    } else {


                                        Toast.makeText(getApplicationContext(), "Vérifiez vos données ", Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                    .setNegativeButton("Annuler",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface di, int i) {
                                    di.cancel();
                                }
                            });
            final AlertDialog d = alt.create();


            d.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    //   d.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundResource(R.drawable.bt);
                    //  d.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundResource(R.drawable.bt);


                }
            });

            d.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






    public class TestVerionTask extends AsyncTask<String, String, String> {


        Connection con;
        String res;
        String anciencompteur;
        String Version_Base="";
        Boolean TestVersion=true;

        @Override
        protected void onPreExecute() {
            Log.e("Version",Version_Base);
        }


        @Override
        protected String doInBackground(String... strings) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);

                // Connect to database
                Log.e("con", "" + con);
                if (con == null) {
                    res = "Check Your Internet Access!";
                } else {
                    String query2 = "SELECT * from VersionAndroid  " +
                            " where Libelle='AjoutAchat'";
                    Statement stmt_version = con.createStatement();
                    ResultSet rs_version = stmt_version.executeQuery(query2);
                    if (rs_version.next()) {
                        Version_Base = rs_version.getString("NumeroVersion");
                        Log.e("Version",Version_Base);
                    }
                    if (!Version_Base.equals(NumVersion )
                            ) {
                        TestVersion = false;
                    }


                }
            } catch (Exception ex) {
                res = ex.getMessage();
                Log.e("ERROR compt ", res);
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(TestVersion==false)
            {
                AlertDialog.Builder alt=new AlertDialog.Builder(co);
                alt.setTitle("Erreur Version");
                alt.setMessage("Verfier Votre Mise à jour \n Contactez votre programmeur");
                alt.setCancelable(false);
                alt.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SharedPreferences pref = getSharedPreferences("usersession", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edt = pref.edit();
                        edt.putBoolean("etat", false);
                        edt.commit();
                        Intent inte = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(inte);
                    }
                });
                AlertDialog d=alt.create();
                d.show();




            }

        }


    }














}
