package automat;

public class BenytBilletautomat {

    public static void main(String[] arg) {
        Billetautomat automat = new Billetautomat();
        java.util.Scanner tastatur = new java.util.Scanner(System.in, "windows-1252");
        System.out.println("BenytBilletautomat version 3");
        System.out.println();
        long kortnr = 0;
        int valg = 0;
        while (true) {
            printMenue(automat);

            valg = GetNumber(tastatur);
            if (valg >= 1000000) {
                kortnr = valg;
                valg = 7;
            }
            switch (valg) {
                case 0:
                    break;
                case 1:
                    System.out.print("Skriv beløb: ");
                    double beløb = GetDouble(tastatur);
                    automat.indsætPenge(beløb);
                    break;
                case 2:
                    long thisRejseKort = 0;
                    System.out.println("KøbBillet Med rejseKort(j/n)");
                    String købInfo = tastatur.nextLine();
                    if (købInfo.equals("j")) {
                        System.out.println("Indtast din rejseKortNR");
                        thisRejseKort = GetNumber(tastatur);
                    }
                    System.out.println("Vælg Type (tryk ind deres id)");
                    if (thisRejseKort > 1000000) {
                        automat.getBilletpris(0.85);
                    } else {
                        automat.getBilletpris(1);
                    }
                    int billetID = GetNumber(tastatur);
                    System.out.println("Skriv antal Zoner");
                    int købantalZoner = GetNumber(tastatur);
                    automat.udskrivBillet(købantalZoner, billetID, thisRejseKort);
                    break;
                case 3:
                    double beløbRetur = automat.returpenge();
                    System.out.println("Du fik " + beløbRetur + " retur retur");
                    break;
                case 4:
                    automat.getBilletpris(1);
                    break;
                case 5:
                    System.out.println("indtest dit navn");
                    String RKnavn = tastatur.nextLine();
                    System.out.println("indtest password");
                    boolean RKOK = true;
                    int myPass = 0;
                    while (RKOK) {
                        myPass = GetNumber(tastatur);
                        if (myPass > 0) {
                            RKOK = false;
                        }
                    }
                    RejseKort newRK = new RejseKort(RKnavn, myPass);
                    newRK.getRejsekortnr();
                    break;
                case 7:
                    RejsekortMenue(kortnr, tastatur);
                    break;
                case 10:
                    System.out.print("Skriv kode: ");
                    String kode = tastatur.next();
                    automat.montørLogin(kode);
                    break;
                case 11:
                    if (automat.erMontør()) {
                        System.out.println("Skriv billetType(id)");
                        automat.getBilletpris(1);
                        int id = GetNumber(tastatur);
                        System.out.print("Skriv beløb: ");
                        double nyPris = GetDouble(tastatur);
                        automat.setBilletpris(nyPris, id);
                    } else {
                        System.out.println("Afvist - log ind først");
                    }
                    break;
                case 12:
                    if (automat.erMontør()) {
                        System.out.println("Skriv billetType(id)");
                        automat.getBilletpris(1);
                        int id = GetNumber(tastatur);
                        System.out.print("Skriv ZoneR: ");
                        double nyPris = GetDouble(tastatur);
                    } else {
                        System.out.println("Afvist - log ind først");
                    }

                    break;
                case 13:
                    automat.Udskriv();
                    break;
                case 14:
                    if (automat.erMontør()) {
                        automat.AddBillet();
                    }
                case 15:
                    if (automat.erMontør()) {
                        automat.montørLogin("");
                        break;
                    }

                default:
                    System.out.println("Ugyldigt valg, prøv igen");
                    break;

            }
        }
    }

    public static int GetNumber(java.util.Scanner tastatur) {
        int number = 0;
        try {
            number = tastatur.nextInt();
        } catch (Exception e) {
            System.out.println("du skal skrive et tal");
            System.out.println(e.getMessage());
            number = 0;
        }
        tastatur.nextLine();
        return number;
    }

    public static double GetDouble(java.util.Scanner tastatur) {
        double number = 0;
        try {
            number = tastatur.nextDouble();
        } catch (Exception e) {
            System.out.println("du skal skrive et tal");
            System.out.println(e.getMessage());
            number = 0;
        }
        tastatur.nextLine();
        return number;
    }

    public static void RejsekortMenue(long d, java.util.Scanner tastatur) {
        try {
            RejseKort RK = new RejseKort(d);
            boolean user = true;
            int myValg = 0;
            int Pass;
            System.out.println("Password");
            Pass = GetNumber(tastatur);
            if (!(RK.confPass(Pass))) {
                user = false;
                System.out.println("Rpøv Igen");
            }
            while (user) {
                RK.printManual();
                myValg = GetNumber(tastatur);
                switch (myValg) {
                    case 1:
                        System.out.println("din Saldo er på " + RK.getSaldo() + " kr");

                        break;
                    case 2:
                        System.out.println("Skriv beløb(min 50 kr)");
                        int thisBeløb = GetNumber(tastatur);
                        double nyBeløb = (RK.getSaldo()) + thisBeløb;
                        if (nyBeløb > 50 || nyBeløb % 50 == 0) {
                            RK.Money(nyBeløb);
                        } else {
                            System.out.println("Error");
                        }
                        break;
                    case 3:
                        System.out.println("Skriv din gamle Password");
                        int gPass = GetNumber(tastatur);
                        System.out.println("Skriv en ny Password");
                        int nyPass = GetNumber(tastatur);
                        RK.changePass(gPass, nyPass);
                        break;
                    case 4:
                        System.out.println("Du er logget UD");
                        user = false;
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Din rejse Kort findes ikke i databassen");
        }
    }

    static void printMenue(Billetautomat automat) {
        System.out.println("-----------------------------------------------");
        System.out.println("Se listen neden under");
        System.out.println("Balancen er på " + automat.getBalance() + " kroner");
        System.out.println();
        System.out.println("Tast 1 for at indbetale penge");
        System.out.println("Tast 2 for at købe Billeten");
        System.out.println("Tast 3 for at få returpengene");
        System.out.println("Tast 4 for at se Priser");
        System.out.println("Tast 5 for at får et gratis rejsekort");
        System.out.println();
        System.out.println("Tast 10 for at logge ind som montør");
        if (automat.erMontør()) {
            System.out.println("Tast 11 for at sætte billet pris(montør)");
            System.out.println("Tast 12 for at sætte zone rabbat (montør)");
            System.out.println("Tast 13 for at se log bog(montør)");
            System.out.println("Tast 14 for at tilfør");
            System.out.println("Tast 15 for at loge ud ");

        }
    }
}
