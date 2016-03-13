/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automat;

import java.io.*;

/**
 *
 * @author Family
 */
public class BilletTyper {

    private int ID;  // billet id 
    private String type; // navn til billetten 
    private double pris; // pris 
    private double antalS; // anatal af soldte billeter 
    private double ZoneR; // ronne rabbat (fra 0.8 til 1)
    private static int lastID; // sidste leddig id 

    BilletTyper(String s, double p, double z, int i) { // denne constructo kør hvis da oprettes ny billet 
        lastID = ID + 1;
        ID = i;
        type = s;
        pris = p;
        antalS = 0;
        ZoneR = z;
        try {
            Writer myFile = new BufferedWriter(new FileWriter("BilletTypper.txt", true));
            String Str = "ID=¤" + i + "¤Navn=¤" + s + "¤Pris=¤" + p + "¤ZoneR=¤" + z + "¤AntalSoldt=¤0";
            myFile.append(Str);
            myFile.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // denne Kunstructor kør kun i starten af programmet når alle billeter indllæsses ind i programmet
    BilletTyper(String s, double p, double z, int i, int antal, String str) {
        lastID = ID + 1;
        ID = i;
        type = s;
        pris = p;
        antalS = antal;
        ZoneR = z;
    }

    public String getString() {
        return type;
    }

    public double getDouble() {
        return pris;
    }

    public double getAntalS() {
        return antalS;
    }

    public double getZoneR() {
        return ZoneR;
    }

    public int getID() {
        return ID;
    }

    public int getLastID() {
        return lastID;
    }

    public void addAntal(double i) {
        antalS = i;
        update();
    }

    public void changeZoner(double d) {
        ZoneR = d;
        update();
    }

    public void changePris(double d) {
        pris = d;
        update();
    }

    private void update() {
        try {
            FileOutputStream myFile = new FileOutputStream("BilletTypper.txt");
            String thisStr = "ID=¤" + ID + "¤Navn=¤" + type + "¤Pris=¤" + pris + "¤ZoneR=¤" + ZoneR + "¤AntalSoldt=¤0";
            myFile.write(thisStr.getBytes());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error");
        }
    }
}
