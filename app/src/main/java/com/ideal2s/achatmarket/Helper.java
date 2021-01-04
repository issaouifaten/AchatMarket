package com.ideal2s.achatmarket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Helper extends SQLiteOpenHelper {

    public Helper(Context context) {
        super(context, "BaseACHAT", null, 1);
    }

    //LigneBE_Class(String codeArticle, String designationArticle, String quantite)
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE LigneBE" +
                "(_id INTEGER PRIMARY KEY,CodeArticle TEXT  ,Designation TEXT,Quantite INTEGER)");


        sqLiteDatabase.execSQL("CREATE TABLE LigneBR" +
                "(_id INTEGER PRIMARY KEY,CodeArticle TEXT  ,Designation TEXT,Quantite INTEGER)");


        sqLiteDatabase.execSQL("CREATE TABLE LigneBFacture" +
                "(_id INTEGER PRIMARY KEY,CodeArticle TEXT  ,Designation TEXT,Quantite INTEGER)");

        sqLiteDatabase.execSQL("CREATE TABLE LigneAvoir" +
                "(_id INTEGER PRIMARY KEY,CodeArticle TEXT  ,Designation TEXT,Quantite INTEGER)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS LigneBE");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS LigneBR");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS LigneBFacture");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS LigneAvoir");
        onCreate(sqLiteDatabase);
    }


    public void AddLigneBE(LigneBE_Class c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CodeArticle", c.getCodeArticle());
        cv.put("Designation", c.getDesignationArticle());
        cv.put("Quantite", c.getQuantite());

        db.insert("LigneBE", null, cv);

    }


    public void AddLigneBR(LigneBR_Class c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CodeArticle", c.getCodeArticle());
        cv.put("Designation", c.getDesignationArticle());
        cv.put("Quantite", c.getQuantite());

        db.insert("LigneBR", null, cv);

    }


    public void AddLigneBFacture(LigneFacture_Class c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CodeArticle", c.getCodeArticle());
        cv.put("Designation", c.getDesignationArticle());
        cv.put("Quantite", c.getQuantite());

        db.insert("LigneBFacture", null, cv);

    }


    public void AddLigneBAvoir(LigneAvoir_Class c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CodeArticle", c.getCodeArticle());
        cv.put("Designation", c.getDesignationArticle());
        cv.put("Quantite", c.getQuantite());

        db.insert("LigneAvoir", null, cv);

    }


    public Cursor getAllLigneBE() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM LigneBE  ", null);
        return c;

    }


    public Cursor getAllLigneBR() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM LigneBR  ", null);
        return c;

    }


    public Cursor getAllLigneBFacture() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM LigneBFacture  ", null);
        return c;

    }


    public Cursor getAllLigneAvoir() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM LigneAvoir  ", null);
        return c;

    }


    public void DeleteLigneBE() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("LigneBE", "", null);
    }


    public void RemoveLigneBE(LigneBE_Class c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("LigneBE", "CodeArticle='" + c.getCodeArticle() + "'", null);
    }


    public void RemoveLigneBR(LigneBR_Class c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("LigneBR", "CodeArticle='" + c.getCodeArticle() + "'", null);
    }

    public void RemoveLigneAvoir(LigneAvoir_Class c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("LigneAvoir", "CodeArticle='" + c.getCodeArticle() + "'", null);
    }

    public void RemoveLigneFacture(LigneFacture_Class c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("LigneBFacture", "CodeArticle='" + c.getCodeArticle() + "'", null);
    }

    public void UpdateLigneBE(LigneBE_Class c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CodeArticle", c.getCodeArticle());
        cv.put("Designation", c.getDesignationArticle());
        cv.put("Quantite", c.getQuantite());

        db.update("LigneBE", cv, "CodeArticle='" + c.getCodeArticle() + "'", null);


    }


    public void UpdateLigneAvoir(LigneAvoir_Class c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CodeArticle", c.getCodeArticle());
        cv.put("Designation", c.getDesignationArticle());
        cv.put("Quantite", c.getQuantite());

        db.update("LigneAvoir", cv, "CodeArticle='" + c.getCodeArticle() + "'", null);


    }


    public void UpdateLigneBR(LigneBR_Class c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CodeArticle", c.getCodeArticle());
        cv.put("Designation", c.getDesignationArticle());
        cv.put("Quantite", c.getQuantite());

        db.update("LigneBR", cv, "CodeArticle='" + c.getCodeArticle() + "'", null);


    }
    public void UpdateLigneFacture(LigneFacture_Class c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CodeArticle", c.getCodeArticle());
        cv.put("Designation", c.getDesignationArticle());
        cv.put("Quantite", c.getQuantite());

        db.update("LigneBFacture", cv, "CodeArticle='" + c.getCodeArticle() + "'", null);


    }

    public void DeleteLigneAvoir() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("LigneAvoir", "", null);
    }

    public void DeleteLigneBR() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("LigneBR", "", null);
    }

    //LigneBFacture

    public void DeleteLigneBFacture() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        db.delete("LigneBFacture", "", null);
    }

    public boolean testExistLigneBE(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean test = false;
        Cursor c = db.rawQuery("SELECT * from LigneBE where CodeArticle='" + code + "'  ", null);

        while (c.moveToNext()) {
            test = true;

        }
        return test;
    }


    public boolean testExistLigneBR(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean test = false;
        Cursor c = db.rawQuery("SELECT * from LigneBR where CodeArticle='" + code + "'  ", null);

        while (c.moveToNext()) {
            test = true;

        }
        return test;
    }

    public boolean testExistLigneBFacture(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean test = false;
        Cursor c = db.rawQuery("SELECT * from LigneBFacture where CodeArticle='" + code + "'  ", null);
        List<String> tables = new ArrayList<>();

        // iterate over the result set, adding every table name to a list
        while (c.moveToNext()) {
            test = true;

        }
        return test;
    }

    public boolean testExistLigneAvoir(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean test = false;
        Cursor c = db.rawQuery("SELECT * from LigneAvoir where CodeArticle='" + code + "'  ", null);
        List<String> tables = new ArrayList<>();

        // iterate over the result set, adding every table name to a list
        while (c.moveToNext()) {
            test = true;

        }
        return test;
    }

}
