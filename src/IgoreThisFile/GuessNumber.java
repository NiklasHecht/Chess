import java.util.Random;
import java.util.Scanner;

public class GuessNumber {
  static Scanner sc = new Scanner(System.in);
  static int randomNumber = new Random().nextInt(1000);
  static boolean gameWon = false;

  static String info = "GUESS THE CORRECT\nYou gotta guess a random Number\nfrom 1 to 1,000\nBut you only got 10 Trys\nAfter missing those you lost";
  static String winMessage = "Congrats!\nYou mastered your guessing skills";
  static String lossMessage = "Damn! Sorry but you lost\n" + "The correct number would have been " + randomNumber;


  public static void main(String args[]) {
    log("" + randomNumber);
    startGame();
  }

  private static void startGame() {
    log(info);
    startLoop();
    logResult();
  }

  private static void logResult() {
    if(gameWon) log(winMessage);
    else log(lossMessage);
  }

  private static void startLoop() {
    int i;
    for(i = 1; i <= 10; i++) {
      System.out.print("\nYour " + HelperMethods.numberToPosition(i) + " Try:\t");
      int n = guess();
      if(n == 0) {
        gameWon = true;
        break;
      }
      else logLowHigh(n);
    }
  }

  private static int guess() {
    int n;
    try{
      n = Integer.parseInt(sc.next()) - randomNumber;
    } catch(Exception e) {
      System.out.print("Please provide a valid Number:\t");
      n = guess();
    }
    return n;
  }

  private static void logLowHigh(int n) {
    String string = "high";
    if(n < 0) string = "low";
    System.out.println("Your Number was to " + string);
  }

  private static void log(String string) {
    System.out.println("\n");
    printStars();
    System.out.println(string);
    printStars();
  }

  private static void printStars() {
    System.out.println("****************************************");
  }
}
