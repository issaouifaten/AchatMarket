package com.ideal2s.achatmarket;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.view.KeyEvent.KEYCODE_DEL;

public class AjoutBEActivity extends AppCompatActivity {
    GridView gridArticle, gridLigne;
    ConnectionClass connectionClass;
    String user, password, base, ip, NomUtilisateur = "", CodeFonction = "";
    final Context co = this;
    ProgressBar pbbar;
    EditText edt_recherche;
    String query_search = "", numBE = "", depot = "", FRS = "";
    public static ArrayList<LigneBE_Class> listLigneBE = new ArrayList<>();
    public static ArrayList<Observation> listObservation = new ArrayList<>();
    Spinner spinDepot, spinFrs;
    private ZXingScannerView scannerView;
    String observation = "", codeinexistant = "";
    Helper helper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_be);
        gridArticle = (GridView) findViewById(R.id.gridarticle);
        pbbar = (ProgressBar) findViewById(R.id.progressBar);
        pbbar.setVisibility(View.GONE);
        ////session base
        SharedPreferences pref = getSharedPreferences("usersessionsql", Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        user = pref.getString("user", user);
        ip = pref.getString("ip", ip);
        password = pref.getString("password", password);
        base = pref.getString("base", base);
        ////SESSION UTILISATEUR
        SharedPreferences prefe = getSharedPreferences("usersession", Context.MODE_PRIVATE);
        SharedPreferences.Editor edte = prefe.edit();
        NomUtilisateur = prefe.getString("NomUtilisateur", NomUtilisateur);
        CodeFonction = prefe.getString("CodeFonction", CodeFonction);
        helper = new Helper(getApplicationContext());


        connectionClass = new ConnectionClass();


        edt_recherche = (EditText) findViewById(R.id.edtrecherche);
        edt_recherche.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                FillListArticle fillListArticle = new FillListArticle();
                // fillListArticle.cancel(true);
                if (i != KEYCODE_DEL) {
                    String s = edt_recherche.getText().toString();
                    if (!s.equals("")) {
                        s = s.replace("'", "''");
                        query_search = "select  CodeArticle,Designation,CodeArticleParent,PrixAchatHT*(TauxTVA/100+1) as PrixAchatTTC ,\n" +
                                "PrixVenteTTC \n" +
                                "from Article \n" +
                                "inner join TVA on TVA.CodeTVA=Article.CodeTVA where ( CodeArticle like '%" + s + "%'" +
                                " or Designation like '%" + s + "%' )and  Actif=1 ORDER BY Designation  ";

                        fillListArticle.execute("");
                    }
                }
                return false;
            }
        });

        Button btlist = (Button) findViewById(R.id.btlist);
        btlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder alt = new AlertDialog.Builder(co, R.style.CustomDialogTheme);
                LayoutInflater li = LayoutInflater.from(co);

                alt.setTitle("Bon Entree : ");
                View px = li.inflate(R.layout.diaglignebe, null);
                alt.setView(px);
                alt.setCancelable(false);
                gridLigne = (GridView) px.findViewById(R.id.gridligne);
                spinDepot = (Spinner) px.findViewById(R.id.spindepot);
                RemplirDepot remplirDepot = new RemplirDepot();
                remplirDepot.execute("");
                spinFrs = (Spinner) px.findViewById(R.id.spinfrs);


                RemplirFrs remplirFrs = new RemplirFrs();
                remplirFrs.execute("");


                spinFrs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        FRS = spinFrs.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                spinDepot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        depot = spinDepot.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                FillListLigneBE fillListLigneBE = new FillListLigneBE();
                fillListLigneBE.execute("");


                alt.setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
//                        FillListArticle fillListArticle = new FillListArticle();
//                        fillListArticle.execute("");


                    }
                });


                alt.setNeutralButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        AlertDialog.Builder alt2 = new AlertDialog.Builder(co);
                        LayoutInflater li2 = LayoutInflater.from(co);

                        alt2.setTitle("Bon Transfert");
                        alt2.setMessage("Voulez vous vraiment annuler ");
                        alt2.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                helper.DeleteLigneBE();
                                dialogInterface.cancel();
                            }
                        });

                        alt2.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        AlertDialog d = alt2.create();
                        d.show();


                    }
                });

                if (helper.getAllLigneBE().getCount() > 0) {
                    alt.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (spinDepot.getSelectedItem().toString().equals("choisir un depot") || spinFrs.getSelectedItem().toString().equals("choisir un fournisseur")) {

                                final AlertDialog.Builder alt = new AlertDialog.Builder(co, R.style.CustomDialogTheme);
                                alt.setTitle("choisir un fournisseur et un depot ");
                                alt.setMessage("");
                                AlertDialog d = alt.create();
                                d.show();

                            } else {
                                if (helper.getAllLigneBE().getCount() > 0) {
                                    AjoutBeTask ajoutBeTask = new AjoutBeTask();
                                    ajoutBeTask.execute("");
                                } else {


                                    final AlertDialog.Builder alt = new AlertDialog.Builder(co, R.style.CustomDialogTheme);
                                    alt.setTitle("Ajouter des articles");
                                    alt.setMessage("");
                                    AlertDialog d = alt.create();
                                    d.show();

                                }


                            }

                        }
                    });
                }


                AlertDialog d2 = alt.create();
                d2.show();


            }
        });


        Button b = (Button) findViewById(R.id.btscan);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scannerView = new ZXingScannerView(AjoutBEActivity.this);
                scannerView.setResultHandler(new ZXingScannerResultHandler());

                setContentView(scannerView);
                scannerView.startCamera();
            }
        });


    }


    /////////////***************************************************/////////////

    public class FillListArticle extends AsyncTask<String, String, String> {
        String z = "";

        List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();
        ArrayList<String> list = new ArrayList<String>();
        boolean exist, verif = false;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
            verif = false;
        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(getApplicationContext(), codeinexistant, Toast.LENGTH_SHORT).show();

            if (exist) {
                final AlertDialog.Builder alt = new AlertDialog.Builder(co, R.style.CustomDialogTheme);
                alt.setTitle("Article est déja saisi");
                alt.setMessage("");
                AlertDialog d = alt.create();
                d.show();


            }
            if (!verif) {
                final AlertDialog.Builder alt = new AlertDialog.Builder(co, R.style.CustomDialogTheme);
                alt.setTitle("Article inexistant");
                alt.setMessage("Vous etes sur que l'article est inexistant: ");
                alt.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        AlertDialog.Builder alt2 = new AlertDialog.Builder(co, R.style.CustomDialogTheme);
                        alt2.setTitle("Entrer Article");
                        LayoutInflater li = LayoutInflater.from(co);

                        View px2 = li.inflate(R.layout.item_article_inexistant, null);
                        final EditText edtqt = px2.findViewById(R.id.qt);

                        alt2.setView(px2);

                        alt2.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                observation += codeinexistant + "(" + edtqt.getText().toString() + "),";
                                Observation obser = new Observation(codeinexistant, edtqt.getText().toString());
                                listObservation.add(obser);

                                Toast.makeText(getApplicationContext(), "obs=" + observation, Toast.LENGTH_SHORT).show();
                                Log.e("obser", observation);
                            }
                        });

                        AlertDialog d = alt2.create();
                        d.show();

                    }
                });
                alt.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog d = alt.create();
                d.show();

            }
            // Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();
            pbbar.setVisibility(View.GONE);
            String[] from = {"A", "B", "C", "D"};
            int[] views = {R.id.code, R.id.designation};
            final SimpleAdapter ADA = new SimpleAdapter(getApplicationContext(),
                    prolist, R.layout.item_article, from,
                    views);


            final BaseAdapter baseAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return prolist.size();
                }

                @Override
                public Object getItem(int position) {
                    return null;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    final LayoutInflater layoutInflater = LayoutInflater.from(co);
                    convertView = layoutInflater.inflate(R.layout.item_article, null);
                    final TextView code = (TextView) convertView.findViewById(R.id.code);
                    final TextView prixachat = (TextView) convertView.findViewById(R.id.prixachat);
                    final TextView prixvente = (TextView) convertView.findViewById(R.id.prixvente);
                    final TextView designation = (TextView) convertView.findViewById(R.id.designation);
                    final EditText edtQt = (EditText) convertView.findViewById(R.id.edtqt);
                    final HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(position);


                    prixachat.setText((String) obj.get("D"));
                    prixvente.setText((String) obj.get("C"));
                    code.setText((String) obj.get("A"));
                    designation.setText((String) obj.get("B"));
                    convertView.findViewById(R.id.btmoin).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Float qt = Float.valueOf(edtQt.getText().toString());
                            qt--;
                            edtQt.setText("" + qt);

                        }
                    });
                    convertView.findViewById(R.id.btplus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Float qt = Float.valueOf(edtQt.getText().toString());
                            qt++;
                            edtQt.setText("" + qt);

                        }
                    });
                    final LinearLayout layout = convertView.findViewById(R.id.layouttop);
                    final Button bt = convertView.findViewById(R.id.btadd);
                    final Button btmoin = convertView.findViewById(R.id.btmoin);
                    final Button btplus = convertView.findViewById(R.id.btplus);
                    convertView.findViewById(R.id.btadd).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String qt = edtQt.getText().toString();
                            String codeArticle = code.getText().toString();
                            String desArticle = designation.getText().toString();
                            LigneBE_Class ligneBE_class = new LigneBE_Class(codeArticle, desArticle, qt);
                            helper.AddLigneBE(ligneBE_class);


                            listLigneBE.add(ligneBE_class);
                            layout.setBackgroundColor(Color.parseColor("#d9d9d9"));
                            bt.setEnabled(false);
                            btmoin.setEnabled(false);
                            btplus.setEnabled(false);


                        }
                    });

                    return convertView;
                }
            };
            gridArticle.setAdapter(baseAdapter);


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";

                } else {
                    String querylist = query_search;

                    Log.e("querylist", querylist);

                    PreparedStatement ps = con.prepareStatement(querylist);
                    ResultSet rs = ps.executeQuery();

                    ArrayList data1 = new ArrayList();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        String desArticle = "", codArticle = "";
                        exist = false;
                        verif = true;
                        String codea = "";
                        if (rs.getString("CodeArticleParent").equals("")) {
                            codea = rs.getString("CodeArticle");
                        } else {
                            codea = rs.getString("CodeArticleParent");
                        }
                        exist = helper.testExistLigneBE(codea);
                        if (!exist) {
                            datanum.put("A", codea);
                            datanum.put("B", rs.getString("Designation"));


                            float p = Float.parseFloat(rs.getString("PrixAchatTTC"));
                            float pve = Float.parseFloat(rs.getString("PrixVenteTTC"));

                            DecimalFormat df = new DecimalFormat("#######0.000");
                            String prixa = df.format(p);
                            String prixv = df.format(pve);

                            if (CodeFonction.equals("AD")) {
                                datanum.put("C", "Prix Vente : " + prixv);
                                datanum.put("D", "Prix Achat : " + prixa);
                            } else {
                                datanum.put("C", "");
                                datanum.put("D", "");

                            }


                            boolean doublage = false;
                            for (int i = 0; i < list.size(); i++) {
                                String cod = list.get(i);
                                if (cod.equals(codea)) {
                                    doublage = true;
                                }
                            }


                            if (!doublage) {
                                prolist.add(datanum);
                                list.add(codea);
                            }
                        }

                        z = "Success";
                    }


                }
            } catch (SQLException ex) {
                z = "list" + ex.toString();

            }
            return z;
        }
    }


    /////////////******************** fin Affichage list ***********************/////////////
    /////


    public class FillListLigneBE extends AsyncTask<String, String, String> {
        String z = "";

        List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();
        ArrayList<String> list;
        Cursor cr;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(getApplicationContext(), r, Toast.LENGTH_SHORT).show();
            Log.e("erreur", r);
            pbbar.setVisibility(View.GONE);
            String[] from = {"A", "B", "C"};
            int[] views = {R.id.code, R.id.designation, R.id.edtqt};


            final SimpleCursorAdapter adapter = new SimpleCursorAdapter
                    (getApplicationContext(), R.layout.item_ligne_be, cr, new String[]{cr.getColumnName(1), cr.getColumnName(2), cr.getColumnName(0)},
                            new int[]{R.id.code, R.id.designation, R.id.edtqt});


            BaseAdapter baseAdapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return cr.getCount();
                }

                @Override
                public Object getItem(int position) {
                    return null;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(final int pos, View convertView, ViewGroup parent) {
                    final LayoutInflater layoutInflater = LayoutInflater.from(co);
                    convertView = layoutInflater.inflate(R.layout.item_ligne_be, null);
                    final TextView code = (TextView) convertView.findViewById(R.id.code);
                    final TextView designation = (TextView) convertView.findViewById(R.id.designation);
                    final EditText edtQt = (EditText) convertView.findViewById(R.id.edtqt);
//                    final HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getCursor().move(pos);


                    cr = helper.getAllLigneBE();
                    if (cr.move(pos + 1)) {

                        code.setText(cr.getString(cr.getColumnIndex("CodeArticle")));
                        designation.setText(cr.getString(cr.getColumnIndex("Designation")));
                        edtQt.setText(cr.getString(cr.getColumnIndex("Quantite")));

                    }


                    final LinearLayout layout = convertView.findViewById(R.id.layouttop);
                    convertView.findViewById(R.id.btmodif).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            try {
                                String qt = edtQt.getText().toString();

                                layout.setBackgroundColor(Color.parseColor("#d9d9d9"));
                                cr.move(pos);
                                LigneBE_Class ligneBE_class = new LigneBE_Class(cr.getString(cr.getColumnIndex("CodeArticle")),
                                        cr.getString(cr.getColumnIndex("Designation")),
                                        "" + cr.getString(cr.getColumnIndex("Quantite")));
                                ligneBE_class.setQuantite(edtQt.getText().toString());
                                helper.UpdateLigneBE(ligneBE_class);
                                Log.e("cursor2", cr.getString(cr.getColumnIndex("CodeArticle")) + "qt" + cr.getString(cr.getColumnIndex("Quantite")));

                                edtQt.setText("" + qt);

                            } catch (Exception e) {
                                Log.e("erreur", e.toString());
                            }


                        }
                    });


                    convertView.findViewById(R.id.btdelete).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                cr = helper.getAllLigneBE();

                                cr.move(pos + 1);
                                LigneBE_Class ligneBE_class = new
                                        LigneBE_Class(cr.getString(cr.getColumnIndex("CodeArticle")),
                                        cr.getString(cr.getColumnIndex("Designation")),
                                        "" + cr.getString(cr.getColumnIndex("Quantite")));

                                helper.RemoveLigneBE(ligneBE_class);
                                FillListLigneBE fillListLigneBE = new FillListLigneBE();
                                fillListLigneBE.execute("");
                            } catch (Exception e) {
                                Log.e("erreur", e.toString());
                            }


                        }
                    });
                    convertView.findViewById(R.id.btmoin).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {

                                Float qt = Float.valueOf(edtQt.getText().toString());
                                qt--;


                                edtQt.setText("" + qt);
                            } catch (Exception e) {
                                Log.e("erreur", e.toString());
                            }
                        }
                    });
                    convertView.findViewById(R.id.btplus).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Float qt = Float.valueOf(edtQt.getText().toString());
                                qt++;

                                edtQt.setText("" + qt);
                                edtQt.setText("" + qt);
                            } catch (Exception e) {
                                Log.e("erreur", e.toString());
                            }

                        }
                    });


                    return convertView;
                }
            };
            gridLigne.setAdapter(baseAdapter);


        }

        @Override
        protected String doInBackground(String... params) {

            try {
                cr = helper.getAllLigneBE();
                Log.e("back", cr.getColumnIndex("CodeArticle") + "");

                if (cr.moveToFirst()) {
                    do {
                        Log.e("cursor", cr.getString(cr.getColumnIndex("CodeArticle")));
                    } while (cr.moveToNext());
                }


            } catch (Exception ex) {
                z = "list" + ex.toString();

            }
            return z;
        }
    }

    /////


    public class AjoutBeTask extends AsyncTask<String, String, String> {
        String z = "";

        List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();
        ArrayList<String> list;
        Boolean testValidation = true;
        Boolean testConnexion = true;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
            Compteur compteur = new Compteur();
            compteur.execute("");
        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(getApplicationContext(),r,Toast.LENGTH_LONG).show();


            pbbar.setVisibility(View.GONE);
            if (testValidation) {
                Intent intent = new Intent(getApplicationContext(), MenuHome.class);
                startActivity(intent);
            } else {
                AlertDialog.Builder alt = new AlertDialog.Builder(co);
                alt.setMessage("erreur Insertion ");
                alt.setTitle("Erreur");
                AlertDialog d=alt.create();
                d.show();

            }
            if (!testConnexion) {
                AlertDialog.Builder alt = new AlertDialog.Builder(co);
                alt.setMessage("Verifier votre Connexion");
                alt.setTitle("Erreur");
                AlertDialog d=alt.create();
                d.show();

            }

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN(ip, password, user, base);
                if (con == null) {
                    z = "Error in connection with SQL server";
                    testConnexion = false;

                } else {
                    if (numBE.equals("") || numBE.equals(null) || numBE.equals("null")||
                            NomUtilisateur.equals("null")||NomUtilisateur.equals("")||NomUtilisateur.equals(null))

                    {
                        testValidation = false;
                    }

                    if (testValidation) {
                        String desArticle = "", codArticle = "", qt, OB = "";
                        boolean exist = false;

                        for (int i = 0; i < listObservation.size(); i++) {
                            Observation element = listObservation.get(i);
                            OB += element.getObservation() + "(" + element.getQT() + "),";


                        }
                        Cursor cr = helper.getAllLigneBE();
                        if (cr.moveToFirst()) {
                            do {
                                Log.e("cursor", cr.getString(cr.getColumnIndex("CodeArticle")));
                                codArticle = cr.getString(cr.getColumnIndex("CodeArticle"));
                                desArticle = cr.getString(cr.getColumnIndex("Designation"));
                                qt = cr.getString(cr.getColumnIndex("Quantite"));


                                desArticle = desArticle.replace("'", "''");
                                String query = " insert into  LigneBonLivraisonAchat(  NumeroBonLivraisonAchat \n" +
                                        "      , CodeArticle \n" +
                                        "      , DesignationArticle \n" +
                                        "      , NumeroOrdre \n" +
                                        "      , PrixAchatHT \n" +
                                        "      , Quantite \n" +
                                        "      , MontantHT \n" +
                                        "      , TauxRemise \n" +
                                        "      , MontantRemise \n" +
                                        "      , MontantFodec \n" +
                                        "      , NetHT \n" +
                                        "      , TauxTVA \n" +
                                        "      , MontantTVA \n" +
                                        "      , MontantTTC \n" +
                                        "      , Observation \n" +
                                        "      , NumeroBonCommandeAchat \n" +
                                        "      , QuantiteGratuite \n" +
                                        "      , TauxRemise2 \n" +
                                        "      , MontantRemise2 \n" +
                                        "      , Colisage \n" +
                                        "      , Volume \n" +
                                        "      , Poids \n" +
                                        "      , CodeUnite \n" +
                                        "      , ValeurUniteVente \n" +
                                        "      , PrixUnitaireNetTTC )values('" + numBE + "','" + codArticle + "', (select Designation from Article where CodeArticle='" + codArticle + "'),\n" +
                                        "      (select ISNULL(max(NumeroOrdre)+1,1) from LigneBonLivraisonAchat where NumeroBonLivraisonAchat='" + numBE + "')\n" +
                                        "      ,(select DernierPrixAchatHT from Article where CodeArticle='" + codArticle + "')\n" +
                                        "      ," + qt + ",(select DernierPrixAchatHT*" + qt + " from Article where CodeArticle='" + codArticle + "')\n" +
                                        "      ,0,0,0,(select DernierPrixAchatHT*" + qt + " from Article where CodeArticle='" + codArticle + "'),\n" +
                                        "      (select TauxTVA from Article inner join TVA on TVA.CodeTVA=Article.CodeTVA where CodeArticle='" + codArticle + "')\n" +
                                        "      ,(select TauxTVA*" + qt + "/100  *DernierPrixAchatHT from Article inner join TVA on TVA.CodeTVA=Article.CodeTVA where CodeArticle='" + codArticle + "')\n" +
                                        "      ,(select (DernierPrixAchatHT*" + qt + " + TauxTVA *" + qt + "/100*DernierPrixAchatHT ) from Article inner join TVA on TVA.CodeTVA=Article.CodeTVA  where CodeArticle='" + codArticle + "')\n" +
                                        "      ,'','',0,0,0,\n" +
                                        "      (select " + qt + "/ Colisage from Article where CodeArticle='" + codArticle + "'),\n" +
                                        "       (select Volume from Article where CodeArticle='" + codArticle + "'),\n" +
                                        "        (select Poids from Article where CodeArticle='" + codArticle + "'),\n" +
                                        "         (select CodeUnite from Article where CodeArticle='" + codArticle + "'),\n" +
                                        "          (select ValeurUniteVente from Article where CodeArticle='" + codArticle + "'),\n" +
                                        "      (select (DernierPrixAchatHT*" + 1 + " + TauxTVA *" + 1 + "/100*DernierPrixAchatHT ) from Article  inner join TVA on TVA.CodeTVA=Article.CodeTVA where CodeArticle='" + codArticle + "')\n" +

                                        "      )" +
                                        "";
                                PreparedStatement preparedStatement = con.prepareStatement(query);


                                preparedStatement.executeUpdate();


                            } while (cr.moveToNext());
                        }
                     FRS=   FRS.replace("'","''");


                        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                                .format(Calendar.getInstance().getTime());
                        String heur = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.FRENCH)
                                .format(Calendar.getInstance().getTime());

                        String queryBE = "  insert into BonLivraisonAchat( NumeroBonLivraisonAchat      , DateBonLivraisonAchat       , \n" +
                                " BonCommandeAchatRelatif \n" +
                                "          , CodeFournisseur    , RaisonSociale  , MartriculeFiscale     , Adresse1      , ReferenceFournisseur   \n" +
                                "           , DateReferenceFournisseur \n" +
                                "          , CodeDepot \n" +
                                "          , TotalHT \n" +
                                "          , TotalRemise \n" +
                                "          , TotalRemise2 \n" +
                                "          , TotalNetHT \n" +
                                "          , TotalFodec \n" +
                                "          , TotalTVA \n" +
                                "          , TotalTTC \n" +
                                "          , TotalVolume \n" +
                                "          , TotalPoids \n" +
                                "          , TotalColisage \n" +
                                "          , NumeroEtat \n" +
                                "          , NomUtilisateur \n" +
                                "          , DateCreation \n" +
                                "          , HeureCreation \n" +
                                "          , Observation    , NoteInterne        , CodeVoiture     , CodeChauffeur         , LibelleMission \n" +
                                "          , NomValidateur    , DateValidation   , NomAnnulation    , DateAnnulation \n" +
                                "          , NbImpression   , MontantFournisseur  , NumeroEtatPaiement   , NumeroFactureAchat   ,\n" +
                                "           Utilisateurintervenant \n" +
                                "          , Economat    , Immobilisation )\n" +
                                "          values\n" +
                                "('" + numBE + "',convert(date,GETDATE()),'',(select CodeFournisseur From Fournisseur where RaisonSociale='" + FRS + "'),'" + FRS + "',(select MatriculeFiscale From Fournisseur where RaisonSociale='" + FRS + "'),(select Adresse From Fournisseur where RaisonSociale='" + FRS + "'),'','" + date + "',\n" +
                                "(select CodeDepot from Depot where Libelle='" + depot + "'),\n" +
                                "(select ISNULL(SUM(MontantHT),0) from LigneBonLivraisonAchat WHERE NumeroBonLivraisonAchat='" + numBE + "'),\n" +
                                "(select ISNULL(SUM(MontantRemise),0) from LigneBonLivraisonAchat WHERE NumeroBonLivraisonAchat='" + numBE + "'),\n" +
                                "(select ISNULL(SUM(MontantRemise2),0) from LigneBonLivraisonAchat WHERE NumeroBonLivraisonAchat='" + numBE + "'),\n" +
                                "(select ISNULL(SUM(MontantTTC),0) from LigneBonLivraisonAchat WHERE NumeroBonLivraisonAchat='" + numBE + "'),\n" +
                                "(select ISNULL(SUM(MontantFodec),0) from LigneBonLivraisonAchat WHERE NumeroBonLivraisonAchat='" + numBE + "'),\n" +
                                "(select ISNULL(SUM(MontantTVA),0) from LigneBonLivraisonAchat WHERE NumeroBonLivraisonAchat='" + numBE + "'),\n" +
                                "(select ISNULL(SUM(MontantTTC),0) from LigneBonLivraisonAchat WHERE NumeroBonLivraisonAchat='" + numBE + "'),\n" +
                                "(select ISNULL(SUM(Volume),0) from LigneBonLivraisonAchat WHERE NumeroBonLivraisonAchat='" + numBE + "'),\n" +
                                "(select ISNULL(SUM(Poids),0) from LigneBonLivraisonAchat WHERE NumeroBonLivraisonAchat='" + numBE + "'),\n" +
                                "(select ISNULL(SUM(Colisage),0) from LigneBonLivraisonAchat WHERE NumeroBonLivraisonAchat='" + numBE + "'),\n" +
                                " \n" +
                                "'E40','" + NomUtilisateur + "',convert(date,GETDATE()),GETDATE(),\n" +
                                "'" + OB + "','','','','','','','','',0,(select ISNULL(SUM(MontantTTC),0) from LigneBonLivraisonAchat WHERE NumeroBonLivraisonAchat='" + numBE + "'),'','','',0,0\n" +
                                "          )";

                        Log.e("querybe=", queryBE);
                        PreparedStatement preparedStatementBE = con.prepareStatement(queryBE);
                        preparedStatementBE.executeUpdate();
                        listLigneBE.clear();
                        helper.DeleteLigneBE();
                        listObservation.clear();

                    }
                }


            } catch (SQLException ex) {
                z = "list" + ex.toString();
                Log.e("erreur",z);

            }
            return z;
        }
    }

    public class Compteur extends AsyncTask<String, String, String> {


        Connection con;
        String res;
        String anciencompteur;

        @Override
        protected void onPreExecute() {

        }


        @Override
        protected String doInBackground(String... strings) {

            try {


                Connection con = connectionClass.CONN(ip, password, user, base);   // Connect to database
                Log.e("con", "" + con);
                if (con == null) {
                    res = "Check Your Internet Access!";
                } else {


                    String query = "Select * from CompteurPiece  where NomPiecer='BonEntrerFournisseur'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        anciencompteur = rs.getString("Compteur");

                        String annee = rs.getString("Annee");
                        String PrefixPiece = rs.getString("PrefixPiece");
                        numBE = PrefixPiece + annee + anciencompteur;

                    }

                    Float comp = Float.parseFloat(anciencompteur);
                    comp++;

                    DecimalFormat numberFormat = new DecimalFormat("00000");
                    String st = numberFormat.format(comp);

                    String queryupdate = "update CompteurPiece  set Compteur='" + st + "' where NomPiecer='BonEntrerFournisseur'";
                    PreparedStatement preparedStatement = con.prepareStatement(queryupdate);
                    preparedStatement.executeUpdate();
                    Log.e("query", query);
                    con.close();

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


        }


    }


    public class RemplirDepot extends AsyncTask<String, String, String> {
        String res = "";
        ArrayList<String> data = new ArrayList<String>();


        @Override
        protected void onPreExecute() {
            data.add("choisir un depot");

        }

        @Override
        protected void onPostExecute(String r) {


            spinDepot.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, data));


        }


        @Override
        protected String doInBackground(String... params) {


            try {


                Connection con = connectionClass.CONN(ip, password, user, base);   // Connect to database
                Log.e("con", "" + con);
                if (con == null) {
                    res = "Check Your Internet Access!";
                } else {


                    String query = "Select * from Depot";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        data.add(rs.getString("Libelle"));


                    }


                }
            } catch (Exception ex) {
                res = ex.getMessage();
                Log.e("ERROR ", res);
            }

            return null;
        }


        public class RemplirFrs extends AsyncTask<String, String, String> {
            String res = "";
            ArrayList<String> data = new ArrayList<String>();


            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onPostExecute(String r) {


                spinFrs.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, data));


            }


            @Override
            protected String doInBackground(String... params) {


                try {


                    Connection con = connectionClass.CONN(ip, password, user, base);   // Connect to database
                    Log.e("con", "" + con);
                    if (con == null) {
                        res = "Check Your Internet Access!";
                    } else {


                        String query = "Select * from Fournisseur";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        while (rs.next()) {
                            data.add(rs.getString("RaisonSociale"));


                        }


                    }
                } catch (Exception ex) {
                    res = ex.getMessage();
                    Log.e("ERROR ", res);
                }

                return null;
            }
        }


    }


    public class RemplirFrs extends AsyncTask<String, String, String> {
        String res = "";
        ArrayList<String> data = new ArrayList<String>();


        @Override
        protected void onPreExecute() {

            data.add("choisir un fournisseur");
        }

        @Override
        protected void onPostExecute(String r) {


            spinFrs.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, data));


        }


        @Override
        protected String doInBackground(String... params) {


            try {


                Connection con = connectionClass.CONN(ip, password, user, base);   // Connect to database
                Log.e("con", "" + con);
                if (con == null) {
                    res = "Check Your Internet Access!";
                } else {


                    String query = "Select RaisonSociale from Fournisseur";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        data.add(rs.getString("RaisonSociale"));


                    }


                }
            } catch (Exception ex) {
                res = ex.getMessage();
                Log.e("ERROR ", res);
            }

            return null;
        }
    }

    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler {


        @Override
        public void handleResult(com.google.zxing.Result result) {

            String resultCode = result.toString();
            codeinexistant = result.toString();


            Toast.makeText(AjoutBEActivity.this, resultCode, Toast.LENGTH_SHORT).show();


            setContentView(R.layout.activity_ajout_be);


            final AlertDialog.Builder alt = new AlertDialog.Builder(co, R.style.CustomDialogTheme);
            LayoutInflater li = LayoutInflater.from(co);

            alt.setTitle("Article  : " + resultCode);
            View px = li.inflate(R.layout.diagarticle, null);
            gridArticle = (GridView) px.findViewById(R.id.grid_article);
            query_search = "select  CodeArticle,Designation,CodeArticleParent,DernierPrixAchatHT*(TauxTVA/100+1) as PrixAchatTTC ,\n" +
                    "PrixVenteTTC \n" +
                    "from Article \n" +
                    "inner join TVA on TVA.CodeTVA=Article.CodeTVA   where CodeArticle='" + resultCode + "'";


            FillListArticle fillListArticle = new FillListArticle();
            fillListArticle.execute("");

            alt.setCancelable(false);


            alt.setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getApplicationContext(), AjoutBEActivity.class);
                    startActivity(intent);
                }
            });
            alt.setView(px);

            AlertDialog d = alt.create();
            d.show();
            scannerView.stopCamera();
        }
    }


}