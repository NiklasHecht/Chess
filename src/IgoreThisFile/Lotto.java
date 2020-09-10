import java.util.Arrays;
import java.util.Scanner;

public class Lotto {
  static Scanner sc = new Scanner(System.in);
  static int[] lotteryNumbers = new int[6];
  static int[] guess = new int[6];
  static int matchingNumbers = 0;
  static int gamesPlayed = 0;
  static int[] correctOnesCount = new int[6];

  public static void main(String args[]) {
    setUpFirstGame();
    newGame();
  }

  private static void startGame() {
    createLotteryNumbers();
    enterNumbers();
    calculate();
    result();
  }

  private static void resetVariables() {
    Arrays.fill(guess, -1);
    Arrays.fill(lotteryNumbers, -1);
    matchingNumbers = 0;
  }

  private static void setUpFirstGame() {
    resetVariables();
    Arrays.fill(correctOnesCount, 0);
  }

  private static void newGame() {
    gamesPlayed++;
    System.out.println("Good luck in your " + HelperMethods.numberToPosition(gamesPlayed) + " game");
    resetVariables();
    startGame();
  }

  private static void result() {
    if(matchingNumbers > 0) System.out.println("Congrats you got " + matchingNumbers + " correct guesses!");
    else System.out.println("Damn! You ain't got luck today. Better luck next time! :)");
    System.out.println("\nWanna try again? (y/n)");

    boolean validInput = false;

    while(!validInput) {
      String input = sc.next().trim();
      if(input.equals("y")) {
        newGame();
        validInput = true;
      } else if(input.equals("n")) {
        logStats();
        System.out.println("Bye, have a great Day!");
        validInput = true;
      } else {
        System.out.println("Please provide a valid input \"y\" or \"n\"");
      }
    }
  }

  private static void logStats() {
    for(int i = 0; i < correctOnesCount.length; i++) {
      int n = correctOnesCount[i];
      if(n > 0) {
        System.out.println("You got " + (i + 1) + " outta " + gamesPlayed + "times " + n + " correct guesses.");
      }
    }
  }

  private static void calculate() {
    for(int i = 0; i < guess.length; i++) {
      for(int j = 0; j < lotteryNumbers.length; j++) {
        if(guess[i] == lotteryNumbers[j]) matchingNumbers++;
      }
    }
    if(matchingNumbers > 0) correctOnesCount[matchingNumbers - 1]++;
  }

  private static void createLotteryNumbers() {
    for (int i = 0; i < lotteryNumbers.length; i++) {
      lotteryNumbers[i] = getUniqueLottozahl(lotteryNumbers);
      System.out.print(lotteryNumbers[i] + ", ");
      System.out.println();
    }
  }

  private static void enterNumbers() {
    int n = -1;
    for (int i = 1; i <= guess.length; i++) {
      boolean isValid = false;
      while(!isValid) {
        System.out.println("Enter Number: " + i);
        n = sc.nextInt();
        if(checkNumber(guess, n)) {
          break;
        }
        else invalidGuessInput(i);
      }
      guess[i-1] = n;
    }
  }

  private static void invalidGuessInput(int i) {
    String guessString = "guesses are ";
    if(i <= 2) guessString = "guess is ";
    System.out.print("Please provide a valid input. An unique number between \"1\" and \"49\". Your current " + guessString);
    boolean enteredLoop = false;
    for(int j = 0; j < i-1; j++) {
      enteredLoop = true;
      System.out.print(guess[j]);
      if(j != i-2) System.out.print(", ");
    }
    if(!enteredLoop) System.out.print("none");
    System.out.println(".");
  }

  private static int getUniqueLottozahl(int[] arr) {
    int n = (int) (Math.random() * 48) + 1;
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == n) {
        return getUniqueLottozahl(arr);
      }
    }
    return n;
  }

  private static boolean checkNumber(int[] arr, int n) {
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == n || n > 49 || n < 1) {
        return false;
      }
    }
    return true;
  }
}