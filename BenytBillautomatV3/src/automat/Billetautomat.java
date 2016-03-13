package automat;

import java.util.*;
import java.io.*;
import java.util.Date; // tips fra stackoverflow 
import java.text.SimpleDateFormat;// tips fra stackoverflow 

/**
 * Model af en simpel billetautomat til enkeltbilletter med én fast pris.
 */
public class Billetautomat {

    private double balance; // Hvor mange penge kunden p.t. har puttet i automaten
    private boolean montørtilstand;
    private ArrayList<BilletTyper> billeter = new ArrayList<>();

    public Billetautomat() {
        balance = 0;
        // tjekker om der findes en document men en billet type hvis der ikke gøre det så opretter den en ny 
        try {
            BufferedReader myFile = new BufferedReader(new FileReader("BilletTypper.txt"));
            String s;
            String[] arraySTR = {""};
            while ((s = myFile.readLine()) != null) {
                arraySTR = s.split("¤");
                try {
                    int id = Integer.parseInt(arraySTR[1]);
                    String Navn = arraySTR[3];
                    double thisPris = Double.parseDouble(arraySTR[5]);
                    double zone = Double.parseDouble(arraySTR[7]);
                    int myAntalS = Integer.parseInt(arraySTR[9]);
                    BilletTyper billet = new BilletTyper(Navn, thisPris, zone, id, myAntalS, "read");
                    billet.addAntal(myAntalS);
                    billeter.add(billet);
                } catch (NumberFormatException f) {
                    System.out.println("Error call support" + f.getMessage());
                }

            }
            myFile.close();
        } catch (IOException e) {
            // hvis der ikke finde document så opretter den en ny document

            BilletTyper billet = new BilletTyper("Voksen Billet", 10, 0.9, 1);
            billeter.add(billet);

            System.out.println("Error call support" + e.getMessage());
        }
    }

    public void AddBillet() {
        if (montørtilstand) {
            double number = 0;
            double zoneRBT = 0;
            String Navn;
            System.out.println("Test navn til Billeten");
            java.util.Scanner tastatur = new java.util.Scanner(System.in, "windows-1252");
            Navn = tastatur.nextLine();

            for (int i = 0; i < 2; i++) {
                if (i == 1) {
                    System.out.println("Test Zone Rabbat");
                } else {
                    System.out.println("Test Pris");
                }
                try {
                    zoneRBT = tastatur.nextDouble();
                    number = number + zoneRBT; // letteste måde (for mig) at får variabler værdier
                } catch (Exception e) {
                    System.out.println("du skal skrive et tal (husk ',' hvis det er ikke en heltal)");
                    System.out.println(e.getMessage());
                    zoneRBT = 0;
                    i--; // går en trin tilbage 
                }

                tastatur.nextLine();

            }
            number = number - zoneRBT; // retur til standart vædier 

            try {
                BilletTyper billet = null;
                billet = new BilletTyper(Navn, number, zoneRBT, billet.getLastID()); // opretter ny billet
                billeter.add(billet); // tilføre til listen
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }

    /**
     * Giver prisen for en billet.
     */
    public void getBilletpris(double d) {
        System.out.print("ID Type                ");
        for (int i = 2; i < 10; i++) {
            System.out.print("Zone " + i + "   ");
        }
        System.out.println("");
        // går igennem alle billeter og udskriver zone
        for (BilletTyper b : billeter) {
            int l = (b.getString()).length();
            int PrintID = b.getID();
            System.out.print(PrintID);
            while (PrintID < 100) { //spasing justering
                System.out.print(" ");
                PrintID = PrintID * 10;
            }
            System.out.print(b.getString());
            while (l <= 15) { // spasing justering 
                System.out.print(" ");
                l++;
            }
            for (int i = 2; i < 10; i++) {
                // jeg har ikke lyst til at se på 15.265485 tal så jeg runder dem op/ned 15.00
                System.out.print("     " + (roundUP(b.getDouble() * b.getZoneR() * i * d)));
            }
            System.out.println(" ");
        }
    }

    /**
     * Modtag nogle penge (i kroner) fra en kunde.
     */
    // hvis man vil købe billetetn uden rejse kort 
    public void indsætPenge(double beløb) {
        balance = balance + beløb;
        String temp = ((new SimpleDateFormat("dd/MM-yyyy HH:mm:ss").format(new Date())) + " Brugeren har kastet ind " + beløb + " kr");
        logbog(temp); // logbog
    }

    /**
     * Giver balancen (beløbet maskinen har modtaget til den næste billet).
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Udskriv en billet. Opdater total og nedskriv balancen med billetprisen
     */
    public void udskrivBillet(int antalZoner, int thisID, long l) {
        // kan kun købe til zone 2-9
        if (antalZoner < 2 || antalZoner > 10 || thisID == 0) {
            System.out.println("Fejl Antal Zoner");
            return;
        }

        BilletTyper tempB = null;
        String RKJN = "Nej"; // til logbog
        double nyPris = 0;
        boolean billetFindes = false;
        double Saldo = balance;
        String[] info = {""};

        for (BilletTyper b : billeter) {
            if (thisID == b.getID()) {
                tempB = b;
                nyPris = b.getDouble()*antalZoner*b.getZoneR();
                billetFindes = true;
            }
        }
        if (!billetFindes) {
            System.out.println("Billeten findes ikke");
            return;
        }
        if (l >= 1000000) {
            RKJN = "Ja";
            nyPris = nyPris * 0.85; // rejse kort rabbet
            nyPris = roundUP(nyPris); // kun en decimal tal
            try {
                BufferedReader myFile = new BufferedReader(new FileReader("RejsekortListe.txt"));
                String tempStr = "";
                while ((tempStr = myFile.readLine()) != null) {
                    info = tempStr.split("¤");
                    String tempL = l + "";
                    if (tempL.equals(info[3])) {
                        if (balance > 0) {
                            returpenge();
                        }
                        Saldo = Double.parseDouble(info[9]);
                        System.out.println("Dit rejsekort Saldo er på"+Saldo);
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        if (Saldo < (nyPris)) {
            System.out.println("Du mangler at indbetale nogle penge");
            return;
        }
        // alle test er ok !! 
        System.out.println("##########B##T#########");
        System.out.println("# BlueJ Trafikselskab #");
        System.out.println("#                     #");
        System.out.println("#        Billet       #");
        System.out.println("#                     #");
        System.out.println("#        Zone " + antalZoner + "       #");

        System.out.print("#" + tempB.getString() + " ");
        space(nyPris);
        System.out.println("#");

        System.out.println("#                     #");
        System.out.println("##########B##T#########");
        Saldo = Saldo - nyPris;
        System.out.println("Din Nye saldo er " + Saldo);
        // hvis man bruger rejse kort så bliver saldo opdateret 
        if (l > 1000000) {

            try {
                FileOutputStream myFile = new FileOutputStream("RejsekortListe.txt");
                info[9] = Saldo + "";
                String temp = "";
                for (int i = 0; i <= 9; i++) {
                    temp = temp + info[i] + "¤";
                }
                myFile.write(temp.getBytes());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Error");
            }
        } else {
            balance = Saldo;
        }// log bog 
        String S = (new SimpleDateFormat("dd/MM-yyyy HH:mm:ss").format(new Date())) + " Brugeren Har Købt";
        S = S + tempB.getString() + " Pris=" + nyPris + " RejseKort" + RKJN;
        logbog(S);
        tempB.addAntal(tempB.getAntalS() + nyPris);
    }

    public void space(double tal) {
        if (tal < 10) {
            System.out.print(tal + " kr ");
        } else if (tal < 100) {
            System.out.print(tal + " kr");
        } else {
            System.out.print(+tal + "kr");
        }
    }

    public double returpenge() {
        double returbeløb = balance;
        balance = 0;
        String logS = (new SimpleDateFormat("dd/MM-yyyy HH:mm:ss").format(new Date())) + " Brugeren fik " + returbeløb + " kr tilbage";
        logbog(logS);
        System.out.println("Du får " + returbeløb + " kr retur");
        return returbeløb;
    }

    void montørLogin(String adgangskode) {
        if ("1234".equals(adgangskode)) {
            montørtilstand = true;
            System.out.println("Montørtilstand aktiveret");
            System.out.println("Du kan nu angive billetpris");
        } else {
            montørtilstand = false;
            System.out.println("Montørtilstand deaktiveret");
        }
    }

    public double getTotal() {
        if (montørtilstand) {
            double penge = 0;
            for (BilletTyper b : billeter) {
                penge = penge + b.getAntalS();
            }
            return penge;
        } else {
            System.out.println("Du har ikke retihedertil at se det");
            return 0;
        }
    }

    public void setBilletpris(double billetPris, int BilletTyppe) {
        BilletTyper nybillet = null;
        
        for (BilletTyper b : billeter) {
            if ((b.getID()) == BilletTyppe) {
                nybillet = b;
                break;
            }
        }
        nybillet.changePris(billetPris);
    }

    public void changeZoneR(double ZoneRbt, int BilletType) {
        BilletTyper billet = null;
        for (BilletTyper b : billeter) {
            if (b.getID() == BilletType) {
                billet = b;
                break;
            }
        }
        billet.changeZoner(ZoneRbt);
    }

    public boolean erMontør() {
        return montørtilstand;
    }

    public void Udskriv() {
        if (montørtilstand) {
            try {
                BufferedReader myFile = new BufferedReader(new FileReader("LogBog.txt"));
                String line;
                while ((line = myFile.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (Exception e) {
                System.out.println("Der er ikke noget logBog");
            }

        }
    }

    private double roundUP(double d) {
        int temp = (int) d;
        double rest = d - temp;
        if (rest < 0.25) {
            rest = 0;
        } else if (rest < 0.75) {
            rest = 0.5;
        } else {
            rest = 1;
        }
        return (rest + temp);
    }

    private void logbog(String s) {
        try {
            Writer myFile = new BufferedWriter(new FileWriter("LogBog.txt", true));
            myFile.append(s + "\n");
            myFile.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
