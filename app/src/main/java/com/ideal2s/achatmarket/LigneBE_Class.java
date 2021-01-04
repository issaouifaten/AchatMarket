package com.ideal2s.achatmarket;

public class LigneBE_Class {

String  CodeArticle;
String  DesignationArticle;
String  Quantite;

    public LigneBE_Class(String codeArticle, String designationArticle, String quantite) {
        CodeArticle = codeArticle;
        DesignationArticle = designationArticle;
        Quantite = quantite;
    }

    public String getCodeArticle() {
        return CodeArticle;
    }

    public void setCodeArticle(String codeArticle) {
        CodeArticle = codeArticle;
    }

    public String getDesignationArticle() {
        return DesignationArticle;
    }

    public void setDesignationArticle(String designationArticle) {
        DesignationArticle = designationArticle;
    }

    public String getQuantite() {
        return Quantite;
    }

    public void setQuantite(String quantite) {
        Quantite = quantite;
    }

    @Override
    public String toString() {
        return "LigneBE_Class{" +
                "CodeArticle='" + CodeArticle + '\'' +
                ", DesignationArticle='" + DesignationArticle + '\'' +
                ", Quantite='" + Quantite + '\'' +
                '}';
    }
}
