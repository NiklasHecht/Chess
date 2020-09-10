import java.util.Random;

public class ArrayCalc {

  static int[] numbers = new int[10];

  public static void main(String args[]) {
    generateArray();

    logArray();
    logSum();
    logAverage();
    logMax();
  }

  // ------- ------- -------  ------
  // ------     Log Data      ------
  // ------- ------- -------  ------

  private static void logArray() {
    log("a", getArrayString());
  }

  private static void logSum() {
    log("b", "Summe: " + getSum());
  }

  private static void logAverage() {
    log("c", "Mittelwert: " + getAverage());
  }

  private static void logMax() {
    log("d", "Die größte Zahl lautet: " + getMax());
  }

  // ------- ------- -------  ------
  // ------    Calc Data      ------
  // ------- ------- -------  ------

  private static void generateArray() {
    System.out.println("Array wird mit zehn Zufallszahlen gefüllt...\n");

    for(int i = 0; i < numbers.length; i++) {
      numbers[i] = new Random().nextInt(100);
    }
  }

  private static String getArrayString() {
    String arrayString = "";
    for(int i = 0; i < numbers.length; i++) {
      arrayString += " " + numbers[i];
    }
    return arrayString;
  }

  private static int getSum() {
    int sum = 0;
    for(int i = 0; i < numbers.length; i++) {
      sum += numbers[i];
    }
    return sum;
  }

  private static int getAverage() {
    return getSum() / numbers.length;
  }

  private static int getMax() {
    int max = Integer.MIN_VALUE;
    for(int i = 0; i < numbers.length; i++) {
      if(numbers[i] > max) max = numbers[i];
    }
    return max;
  }

  // ------- ------- -------  ------
  // ------   Helper Methods  ------
  // ------- ------- -------  ------

  private static void log(String teilaufgabe, String text) {
    System.out.println("\n-------- Teilaufgabe " + teilaufgabe + ") --------");
    System.out.println(text);
  }
}
