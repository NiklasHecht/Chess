class Bank {
  static Bankverwaltung verwaltung = new Bankverwaltung();
  public static void main(String args[]) {
    Kunde kunde1 = new Kunde("Mike Oxlong", 10);
    verwaltung.calcZinsen(kunde1, 7);
    verwaltung.calcZinsen(kunde1, 3);
//    verwaltung.einzahlung(kunde1, 100);
  }
}

class Bankverwaltung {
  final int ZINSSATZ_NORMAL = 5;
  final int ZINSSATZ_SHORT = 3;
  final int ZINSSATZ_LONG = 4;

  public void einzahlung(Kunde kunde, double betrag) {
    double alterKontostand = kunde.getKontostand();
    double zwischnenbertag = alterKontostand + betrag;
    double zinsbetrag = zwischnenbertag * ZINSSATZ_NORMAL / 100;
    double neuerKontostand = zwischnenbertag + zinsbetrag;
    String name = kunde.getName();
    kunde.setKontostand(neuerKontostand);

    System.out.println("\nName:\t\t\t\t\t\t\t\t\t" + name);
    System.out.println("Alter Kontostand:\t\t\t" + alterKontostand + "€");
    System.out.println("Einzahlung:\t\t\t\t\t\t" + betrag + "€");
    System.out.println("Zwischenbetrag:\t\t\t\t" + zwischnenbertag + "€");
    System.out.println("Zinsbetrag:\t\t\t\t\t\t" + zinsbetrag + "€");
    System.out.println("Neuer Kontostand:\t\t\t" + kunde.getKontostand() + "€");
  }

  public void calcZinsen(Kunde kunde, int laufzeit) {
    int zinssatz;
    if(laufzeit < 5) {
      zinssatz = ZINSSATZ_SHORT;
    } else {
      zinssatz = ZINSSATZ_LONG;
    }
    System.out.println("\nKunde: " + kunde.getName() + "\t\t\tKapital: " + kunde.getKontostand() + "€\t\tZinssatz: " + zinssatz + "%");
    System.out.println("Jahr\t\t\t\t\t\t Zinsbetrag\t\t\t\tKontostand");
    for(int i = 0; i < laufzeit; i++) {
      double kontoStand = kunde.getKontostand();
      kunde.setKontostand(kontoStand + kontoStand * zinssatz / 100);
      System.out.println("\t" + (i+1) + "\t\t\t\t\t\t\t\t" + HelperMethods.roundTwoDecimals(kunde.getKontostand() - kontoStand) + "€\t\t\t\t\t\t\t" + HelperMethods.roundTwoDecimals(kunde.getKontostand()) + "€");
    }
  }
}

class Kunde {
  private String name;
  private double kontostand;

  public Kunde(String name, double kontostand) {
    this.setName(name);
    this.setKontostand(kontostand);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getKontostand() {
    return kontostand;
  }

  public void setKontostand(double kontostand) {
    this.kontostand = kontostand;
  }
}