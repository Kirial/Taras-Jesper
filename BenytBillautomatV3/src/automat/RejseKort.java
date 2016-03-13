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
public class RejseKort {

    private int ID = 0;
    private String navn;
    private int password;
    private double konto;
    private long RejsekortNummer = 0;
    private String[] DocLine;
// denne constructor kører hvis man vil tjekke i databasen om der findes denne rejse kort 
    RejseKort(long RKnr) {
        boolean found = false;
        try {
            BufferedReader myFile = new BufferedReader(new FileReader("RejsekortListe.txt"));
            String str;
            String temp = RKnr + ""; // laver int om til string da det er meget nemmerer 
             
            // går en linje ad gang 
            while ((str = myFile.readLine()) != null) {
                DocLine = str.split("¤"); // alle min variabler er splitet med ¤ tegn så jeg bruger det til at sepererer variabler 
                if ((DocLine[3]).equals(temp)) { 
                    found = true; 
                    break;
                }
            }
            myFile.close(); 

            if (!found) {
                throw new IllegalArgumentException(); // fohindre oprettelse af objectet (rejse kort er ikke i db)
            } else {
                try {// opretter en object 
                    ID = Integer.parseInt(DocLine[1]);
                    RejsekortNummer = RKnr;
                    navn = DocLine[5];
                    password = Integer.parseInt(DocLine[7]);
                    konto = Double.parseDouble(DocLine[9]);
                    // 
                } catch (Exception e) {
                    System.out.println("Kontakt venlist Support error 300");
                    throw new IllegalArgumentException(); // hvis der er fejl i konvertering 
                }

            }
        } catch (Exception e) {
            throw new IllegalArgumentException(); // hvis der er fejl med fillen 
        }

    }
    // denne constructor kører hvis man opretter en ekstra rejse kort 
    RejseKort(String s, int p) {
        try {
            BufferedReader myFile = new BufferedReader(new FileReader("RejsekortListe.txt"));
            String str;
            String line = "";
            
            // finder den sidste linje (sidste rejsekort nr og sidste id 
            while ((str = myFile.readLine()) != null) {
                line = str; 
            }
            myFile.close();

            String[] arrayLine = line.split("¤"); // split efter ¤  
            ID = (Integer.parseInt(arrayLine[1])) + 1;  // id +1  
            RejsekortNummer = (Integer.parseInt(arrayLine[3])) + 1; // rknr +1 
        } catch (Exception e) {
            // hvis der findes ikke en rejse kort 
            System.out.println("Første Rejsekort");
            ID = 1;
            RejsekortNummer = 1000000;
        }

        navn = s;
        password = p;
        konto = 0;
        // string klar til at indsætte 
        String indsæt = "\nID=¤" + ID + "¤Rejsekortnummer=¤" + RejsekortNummer + "¤Navn=¤" + navn + "¤Password=¤" + password + "¤Saldo=¤" + konto;
        try {
            Writer myFile = new BufferedWriter(new FileWriter("RejsekortListe.txt", true));
            myFile.append(indsæt);
            myFile.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //opdaterer linje(focus på konto) 
    public void Money(double d) {
        try {
            FileOutputStream myFile = new FileOutputStream("RejsekortListe.txt");
            DocLine[9] = d + "";
            String temp = "";
            for (int i = 0; i <= 9; i++) {
                temp = temp + DocLine[i]+"¤";
            }
            myFile.write(temp.getBytes()); // opdaterer linjen 
            konto = konto + d;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String getName() {
        return navn;
    }

    public int getID() {
        return ID;
    }

    public double getSaldo() {
        return konto;
    }

    public boolean confPass(int p) {
        if (p == password) {
            return true;
        }
        return false;
    }
// skift password 
    public void changePass(int p, int np) {
        if (p == password) {
            try {
                FileOutputStream myFile = new FileOutputStream("RejsekortListe.txt");
                DocLine[7] = np + "";
                String temp = "";
                for (int i = 0; i <= 9; i++) {
                    temp = temp + DocLine[i]+"¤";
                }
                myFile.write(temp.getBytes());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Error");
            }
            System.out.println("Din password er skiftet");
        }else{
            System.out.println("Din password matcher ikke med din gamle password");
        }
    }
    // print menue bliver kaldt fra benyt automat 
    public void printManual() {
        System.out.println("hey "+getName());
        System.out.println("Tryk 1 for at se saldo");
        System.out.println("Tryk 2 for at tanke op");
        System.out.println("Tryk 3 for at Skifte password");
        System.out.println("Tryk 4 for at logge ud");
    }
    
    public void getRejsekortnr(){
        System.out.println("Dit kortNR er "+RejsekortNummer);
    }
}
