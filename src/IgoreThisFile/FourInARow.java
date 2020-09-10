import java.util.Arrays;
import java.util.Scanner;

public class FourInARow {
  Scanner sc = new Scanner(System.in);
  String[][] board;
  boolean playerOne;

  public FourInARow() {
    board = new String[7][6];
    playerOne = true;
    fillBoard();
  }

  public void logBoard() {
    System.out.println("     1   2   3   4   5   6   7");
    for(int j = 0; j < board[1].length; j++) {
      System.out.print(" " + (j + 1) + " |");
      for(int i = 0; i < board.length; i++) {
        System.out.print(" " + board[i][j] + " |");
      }
      System.out.println("\n--------------------------------");
    }
  }

  public void fillBoard() {
    for(int i = 0; i < board.length; i++) {
      Arrays.fill(board[i], ".");
    }
  }

  public void start() {
    boolean won = won();
    while(!won && !full()) {
      logBoard();
      setStone();
      won = won();
    }
    logBoard();
    if(!won) System.out.println("It's a draw!");

    else {
      if (playerOne)
        System.out.println("Player 2 won!");
      else
        System.out.println("Player 1 won!");
    }
  }

  public boolean full() {
    for(int i = 0; i < board.length; i++) {
      for(int j = 0; j < board[i].length; j++) {
        if(board[i][j].equals(".")) return false;
      }
    }
    return true;
  }

  public boolean won() {
    //HORIZONTAL
    for(int i = 0; i < board.length; i++) {
      for(int j = 0; j < board[i].length; j++) {
        if(!board[i][j].equals(".") && checkForWinningSpot(i, j)) return true;
      }
    }
    return false;
  }

  public boolean checkForWinningSpot(int i, int j) {
    String spot = board[i][j];
    int counter = 0;

    //Horizontal
    for(int i2 = i; i2 < board.length; i2++) {
      if(board[i2][j].equals(spot)) counter++;
      else break;
    }
    if(counter == 4) return true;
    else counter = 0;

    //Vertical
    for(int j2 = j; j2 < board[i].length; j2++) {
      if(board[i][j2].equals(spot)) counter++;
      else break;
    }
    if(counter == 4) return true;
    else counter = 0;

    //Diagonal No.1
    for(int i2 = i; i2 < board.length && j + (i2 - i) < board[i].length; i2++) {
      if(board[i2][j + (i2 - i)].equals(spot)) counter++;
      else break;
    }
    if(counter == 4) return true;
    else counter = 0;

    //Diagonal No.2
    for(int i2 = i; i2 < board.length && j - (i2 - i) > 0; i2++) {
      if(board[i2][j - (i2 - i)].equals(spot)) counter++;
      else break;
    }
    if(counter == 4) return true;

    return false;
  }

  public void setStone() {
    int input = sc.nextInt() - 1;
    if(input < 0 || input >= 7) setStone();
    else insertStone(input);
  }

  public void insertStone(int k) {
    for(int i = board[k].length - 1; i >= 0; i--) {
      if(board[k][i].equals(".")){
        if(playerOne) board[k][i] = "X";
        else board[k][i] = "O";
        playerOne = !playerOne;
        break;
      }
    }
  }




  public static void main(String[] args0) {
    FourInARow game = new FourInARow();
    game.start();
  }
}
