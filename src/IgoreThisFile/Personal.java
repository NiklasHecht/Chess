class Personal {
  public static void main(String args[]) {
    Mitarbeiter arbeitnehm_1 = new Mitarbeiter("Glück", "Hans", 1972);
    Mitarbeiter arbeitnehm_2 = new Mitarbeiter("Langes", "Haar", 1975);

    arbeitnehm_1.logInfo(39);
    arbeitnehm_2.logInfo(20);
  }
};

class Mitarbeiter {
  final double LOHN = 5.6;

  String name;
  String vorname;
  int geburtsjahr;
  private int personalNummer = -1;
  static int mitarbeiterCount = 0;

  public Mitarbeiter(String name, String vorname, int geburtsjahr) {
    this.name = name;
    this.vorname = vorname;
    this.geburtsjahr = geburtsjahr;
    this.personalNummer = mitarbeiterCount;
    mitarbeiterCount++;
  }

  public String getPersonalNummer() {
    if(personalNummer < 10) {
      return "000" + this.personalNummer;
    } else if(personalNummer < 100) {
      return "00" + this.personalNummer;
    } else if(personalNummer < 1000) {
      return "0" + this.personalNummer;
    }
    return "" + this.personalNummer;
  }

  public int getAlter(int jahr) {
    return jahr - this.geburtsjahr;
  }

  public double getGehalt(int stunden) {
    double gehalt;
    gehalt = this.LOHN * stunden;
    return gehalt;
  }

  public void logInfo(int arbeitsstunden) {
    System.out.println("Personalnummer: " + this.getPersonalNummer());
    System.out.println("Name: " + this.name);
    System.out.println("Vorname: " + this.vorname);
    System.out.println("Alter: " + this.getAlter(2020));
    System.out.println("Gehalt: " + Math.round(this.getGehalt(arbeitsstunden)) + " €");
    System.out.println();
  }
}
