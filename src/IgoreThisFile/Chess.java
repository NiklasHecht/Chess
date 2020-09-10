import java.util.Scanner;

public class Chess {

  static int whiteWins = 0;
  static int blackWins = 0;

  static Scanner sc = new Scanner(System.in);
  String[][] board = new String[][]
      {
          // White = lowercase
          // Black = uppercase
    {"RK", "KT", "BI", "QU", "KI", "BI", "KT", "RK"},
    {"PW", "PW", "PW", "PW", "PW", "PW", "PW", "PW"},
    {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "},
    {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "},
    {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "},
    {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "},
    {"pw", "pw", "pw", "pw", "pw", "pw", "pw", "pw"},
    {"rk", "kt", "bi", "qu", "ki", "bi", "kt", "rk"}
//          {"  ", "  ", "  ", "  ", "KI", "  ", "  ", "  "},
//          {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "},
//          {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "},
//          {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "},
//          {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "},
//          {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "},
//          {"  ", "  ", "  ", "  ", "  ", "  ", "  ", "  "},
//          {"  ", "  ", "  ", "  ", "ki", "  ", "  ", "rk"}
      };

  boolean playerOne = true;
  boolean[][] gotMoved = new boolean[][] {
      {false, false, false, false, false, false, false, false},
      {false, false, false, false, false, false, false, false},
      {true, true, true, true, true, true, true, true},
      {true, true, true, true, true, true, true, true},
      {true, true, true, true, true, true, true, true},
      {true, true, true, true, true, true, true, true},
      {false, false, false, false, false, false, false, false},
      {false, false, false, false, false, false, false, false}
  };

  public void start() {
    boolean won = won();
    while(!won) {
      logBoard();
      move();
      won = won();
    }
    if(playerOne) {
      System.out.println("Black won!");
      blackWins++;
    }
    else {
      System.out.println("White won!");
      whiteWins++;
    }
  }

  public void move() {
    String[] input;
    try {
      String i = sc.nextLine();
      input = i.split(" ");
    }catch(Exception e) {
      move();
      return;
    }
    String figure = getFigureAt(input[0]);
    if(figure == null || figure.equals("  ")) { // Empty field selected or invalid field selected
      System.out.println("Empty field selected or invalid field selected");
      move();
      return;
    }
    if(playerOne && isUpperCase(figure) || !playerOne && !isUpperCase(figure)) { // Wrong players figure selected
      System.out.println("Wrong players figure selected");
      move();
      return;
    }
    if(input.length >= 2) moveFigure(input);
    else move();
  }

  public void moveFigure(String[] pos) {
    int[][] possibleMoves = getMovesFor(pos[0]);
    int[] oldCoords = getCoordsFor(pos[0]);
    int[] newCoords = getCoordsFor(pos[1]);
    boolean moved = false;
    for(int i = 0; i < possibleMoves.length; i++) {
      if(possibleMoves[i][0] == newCoords[0] && possibleMoves[i][1] == newCoords[1]) {
        if(playerOne && newCoords[0] == 0 && board[oldCoords[0]][oldCoords[1]].equals("pw")) {
          board[newCoords[0]][newCoords[1]] = "qu";
        } else if(!playerOne && newCoords[0] == 7 && board[oldCoords[0]][oldCoords[1]].equals("PW")) {
          board[newCoords[0]][newCoords[1]] = "QU";
        } else if(board[oldCoords[0]][oldCoords[1]].toLowerCase().equals("ki") && (oldCoords[1] - newCoords[1]) == 2) {
          board[oldCoords[0]][oldCoords[1] - 1] = board[oldCoords[0]][0];
          board[oldCoords[0]][0] = "  ";
          gotMoved[oldCoords[0]][0] = true;
          board[newCoords[0]][newCoords[1]] = board[oldCoords[0]][oldCoords[1]];
        } else if(board[oldCoords[0]][oldCoords[1]].toLowerCase().equals("ki") && (oldCoords[1] - newCoords[1]) == -2) {
          board[oldCoords[0]][oldCoords[1] + 1] = board[oldCoords[0]][7];
          board[oldCoords[0]][7] = "  ";
          gotMoved[oldCoords[0]][7] = true;
          board[newCoords[0]][newCoords[1]] = board[oldCoords[0]][oldCoords[1]];
        } else {
          board[newCoords[0]][newCoords[1]] = board[oldCoords[0]][oldCoords[1]];
        }
        board[oldCoords[0]][oldCoords[1]] = "  ";
        gotMoved[oldCoords[0]][oldCoords[1]] = true;
        moved = true;
        playerOne = !playerOne;
        break;
      }
    }
    if(!moved) {
      System.out.println("Invalid move selected");
      move();
    }
  }

  public boolean isUpperCase(String str) {
    return !str.toLowerCase().equals(str);
  }

  public int[][] add(int[] coords, int[][] array) {
    int[][] result = new int[array.length + 1][2];
    for(int i = 0; i < array.length; i++) {
      result[i] = array[i];
    }
    result[array.length] = coords;
    return result;
  }

  public int[][] getMovesFor(String pos) {
    String figure = getFigureAt(pos).toLowerCase();
    int[] coords = getCoordsFor(pos);
    int i = coords[0];
    int j = coords[1];
    int[][] possibleMoves = new int[][] {};
    switch (figure) {
      case "rk":
        for(int j2 = j + 1; j2 < 8; j2++) { //horizontal positive
          String figure2 = board[i][j2];
          if(!figure2.equals("  ")) {
            if((playerOne && isUpperCase(figure2)) || (!playerOne && !isUpperCase(figure2))) {
              possibleMoves = add(new int[] {i, j2}, possibleMoves);
            }
            break;
          }
          else {
            possibleMoves = add(new int[] {i, j2}, possibleMoves);
          }
        }
        for(int j2 = j - 1; j2 >= 0; j2--) { //horizontal negative
          String figure2 = board[i][j2];
          if(!figure2.equals("  ")) {
            if((playerOne && isUpperCase(figure2)) || (!playerOne && !isUpperCase(figure2))) {
              possibleMoves = add(new int[] {i, j2}, possibleMoves);
            }
            break;
          }
          else {
            possibleMoves = add(new int[] {i, j2}, possibleMoves);
          }
        }
        for(int i2 = i + 1; i2 < 8; i2++) { //vertical positive
          String figure2 = board[i2][j];
          if(!figure2.equals("  ")) {
            if((playerOne && isUpperCase(figure2)) || (!playerOne && !isUpperCase(figure2))) {
              possibleMoves = add(new int[] {i2, j}, possibleMoves);
            }
            break;
          }
          else {
            possibleMoves = add(new int[] {i2, j}, possibleMoves);
          }
        }
        for(int i2 = i - 1; i2 >= 0; i2--) { //vertical negative
          String figure2 = board[i2][j];
          if(!figure2.equals("  ")) {
            if((playerOne && isUpperCase(figure2)) || (!playerOne && !isUpperCase(figure2))) {
              possibleMoves = add(new int[] {i2, j}, possibleMoves);
            }
            break;
          }
          else {
            possibleMoves = add(new int[] {i2, j}, possibleMoves);
          }
        }
        break;
      case "kt":
        if(i + 2 < 8 && j + 1 < 8 && (board[i + 2][j + 1].equals("  ") || playerOne && isUpperCase(board[i + 2][j + 1]) || !playerOne && !isUpperCase(board[i + 2][j + 1])))  possibleMoves = add(new int[] {i + 2, j + 1}, possibleMoves);
        if(i - 2 >= 0 && j + 1 < 8 && (board[i - 2][j + 1].equals("  ") || playerOne && isUpperCase(board[i - 2][j + 1]) || !playerOne && !isUpperCase(board[i - 2][j + 1])))  possibleMoves = add(new int[] {i - 2, j + 1}, possibleMoves);
        if(i + 2 < 8 && j - 1 >= 0 && (board[i + 2][j - 1].equals("  ") || playerOne && isUpperCase(board[i + 2][j - 1]) || !playerOne && !isUpperCase(board[i + 2][j - 1])))  possibleMoves = add(new int[] {i + 2, j - 1}, possibleMoves);
        if(i - 2 >= 0 && j - 1 >= 0 && (board[i - 2][j - 1].equals("  ") || playerOne && isUpperCase(board[i - 2][j - 1]) || !playerOne && !isUpperCase(board[i - 2][j - 1])))  possibleMoves = add(new int[] {i - 2, j - 1}, possibleMoves);
        if(i + 1 < 8 && j + 2 < 8 && (board[i + 1][j + 2].equals("  ") || playerOne && isUpperCase(board[i + 1][j + 2]) || !playerOne && !isUpperCase(board[i + 1][j + 2])))  possibleMoves = add(new int[] {i + 1, j + 2}, possibleMoves);
        if(i - 1 >= 0 && j + 2 < 8 && (board[i - 1][j + 2].equals("  ") || playerOne && isUpperCase(board[i - 1][j + 2]) || !playerOne && !isUpperCase(board[i - 1][j + 2])))  possibleMoves = add(new int[] {i - 1, j + 2}, possibleMoves);
        if(i + 1 < 8 && j - 2 >= 0 && (board[i + 1][j - 2].equals("  ") || playerOne && isUpperCase(board[i + 1][j - 2]) || !playerOne && !isUpperCase(board[i + 1][j - 2])))  possibleMoves = add(new int[] {i + 1, j - 2}, possibleMoves);
        if(i - 1 >= 0 && j - 2 >= 0 && (board[i - 1][j - 2].equals("  ") || playerOne && isUpperCase(board[i - 1][j - 2]) || !playerOne && !isUpperCase(board[i - 1][j - 2])))  possibleMoves = add(new int[] {i - 1, j - 2}, possibleMoves);
        break;
      case "bi":
        for(int i2 = i + 1; i2 < 8 && j + (i2 - i) < 8; i2++) { //positive positive
          String figure2 = board[i2][j + (i2 - i)];
          if(!figure2.equals("  ")) {
            if((playerOne && isUpperCase(figure2)) || (!playerOne && !isUpperCase(figure2))) {
              possibleMoves = add(new int[] {i2, j + (i2 - i)}, possibleMoves);
            }
            break;
          }
          else {
            possibleMoves = add(new int[] {i2, j + (i2 - i)}, possibleMoves);
          }
        }
        for(int i2 = i + 1; i2 < 8 && j - (i2 - i) >= 0; i2++) { //positive negative
          String figure2 = board[i2][j - (i2 - i)];
          if(!figure2.equals("  ")) {
            if((playerOne && isUpperCase(figure2)) || (!playerOne && !isUpperCase(figure2))) {
              possibleMoves = add(new int[] {i2, j - (i2 - i)}, possibleMoves);
            }
            break;
          }
          else {
            possibleMoves = add(new int[] {i2, j - (i2 - i)}, possibleMoves);
          }
        }
        for(int i2 = i - 1; i2 >= 0 && j + (i - i2) < 8; i2--) { //negative positive
          String figure2 = board[i2][j + (i - i2)];
          if(!figure2.equals("  ")) {
            if((playerOne && isUpperCase(figure2)) || (!playerOne && !isUpperCase(figure2))) {
              possibleMoves = add(new int[] {i2, j + (i - i2)}, possibleMoves);
            }
            break;
          }
          else {
            possibleMoves = add(new int[] {i2, j + (i - i2)}, possibleMoves);
          }
        }
        for(int i2 = i - 1; i2 >= 0 && j - (i - i2) >= 0; i2--) { //negative negative
          String figure2 = board[i2][j - (i - i2)];
          if(!figure2.equals("  ")) {
            if((playerOne && isUpperCase(figure2)) || (!playerOne && !isUpperCase(figure2))) {
              possibleMoves = add(new int[] {i2, j - (i - i2)}, possibleMoves);
            }
            break;
          }
          else {
            possibleMoves = add(new int[] {i2, j - (i - i2)}, possibleMoves);
          }
        }
        break;
      case "qu":
        for(int i2 = i + 1; i2 < 8 && j + (i2 - i) < 8; i2++) { //positive positive
          String figure2 = board[i2][j + (i2 - i)];
          if(!figure2.equals("  ")) {
            if((playerOne && isUpperCase(figure2)) || (!playerOne && !isUpperCase(figure2))) {
              possibleMoves = add(new int[] {i2, j + (i2 - i)}, possibleMoves);
            }
            break;
          }
          else {
            possibleMoves = add(new int[] {i2, j + (i2 - i)}, possibleMoves);
          }
        }
        for(int i2 = i + 1; i2 < 8 && j - (i2 - i) >= 0; i2++) { //positive negative
          String figure2 = board[i2][j - (i2 - i)];
          if(!figure2.equals("  ")) {
            if((playerOne && isUpperCase(figure2)) || (!playerOne && !isUpperCase(figure2))) {
              possibleMoves = add(new int[] {i2, j - (i2 - i)}, possibleMoves);
            }
            break;
          }
          else {
            possibleMoves = add(new int[] {i2, j - (i2 - i)}, possibleMoves);
          }
        }
        for(int i2 = i - 1; i2 >= 0 && j + (i - i2) < 8; i2--) { //negative positive
          String figure2 = board[i2][j + (i - i2)];
          if(!figure2.equals("  ")) {
            if((playerOne && isUpperCase(figure2)) || (!playerOne && !isUpperCase(figure2))) {
              possibleMoves = add(new int[] {i2, j + (i - i2)}, possibleMoves);
            }
            break;
          }
          else {
            possibleMoves = add(new int[] {i2, j + (i - i2)}, possibleMoves);
          }
        }
        for(int i2 = i - 1; i2 >= 0 && j - (i - i2) >= 0; i2--) { //negative negative
          String figure2 = board[i2][j - (i - i2)];
          if(!figure2.equals("  ")) {
            if((playerOne && isUpperCase(figure2)) || (!playerOne && !isUpperCase(figure2))) {
              possibleMoves = add(new int[] {i2, j - (i - i2)}, possibleMoves);
            }
            break;
          }
          else {
            possibleMoves = add(new int[] {i2, j - (i - i2)}, possibleMoves);
          }
        }
        for(int j2 = j + 1; j2 < 8; j2++) { //horizontal positive
          String figure2 = board[i][j2];
          if(!figure2.equals("  ")) {
            if((playerOne && isUpperCase(figure2)) || (!playerOne && !isUpperCase(figure2))) {
              possibleMoves = add(new int[] {i, j2}, possibleMoves);
            }
            break;
          }
          else {
            possibleMoves = add(new int[] {i, j2}, possibleMoves);
          }
        }
        for(int j2 = j - 1; j2 >= 0; j2--) { //horizontal negative
          String figure2 = board[i][j2];
          if(!figure2.equals("  ")) {
            if((playerOne && isUpperCase(figure2)) || (!playerOne && !isUpperCase(figure2))) {
              possibleMoves = add(new int[] {i, j2}, possibleMoves);
            }
            break;
          }
          else {
            possibleMoves = add(new int[] {i, j2}, possibleMoves);
          }
        }
        for(int i2 = i + 1; i2 < 8; i2++) { //vertical positive
          String figure2 = board[i2][j];
          if(!figure2.equals("  ")) {
            if((playerOne && isUpperCase(figure2)) || (!playerOne && !isUpperCase(figure2))) {
              possibleMoves = add(new int[] {i2, j}, possibleMoves);
            }
            break;
          }
          else {
            possibleMoves = add(new int[] {i2, j}, possibleMoves);
          }
        }
        for(int i2 = i - 1; i2 >= 0; i2--) { //vertical negative
          String figure2 = board[i2][j];
          if(!figure2.equals("  ")) {
            if((playerOne && isUpperCase(figure2)) || (!playerOne && !isUpperCase(figure2))) {
              possibleMoves = add(new int[] {i2, j}, possibleMoves);
            }
            break;
          }
          else {
            possibleMoves = add(new int[] {i2, j}, possibleMoves);
          }
        }
        break;
      case "ki":
        if(i + 1 < 8 && (board[i + 1][j].equals("  ") || playerOne && isUpperCase(board[i + 1][j]) || !playerOne && !isUpperCase(board[i + 1][j]))) possibleMoves = add(new int[]{i + 1, j}, possibleMoves);
        if(i - 1 >= 0 && (board[i - 1][j].equals("  ") || playerOne && isUpperCase(board[i - 1][j]) || !playerOne && !isUpperCase(board[i - 1][j]))) possibleMoves = add(new int[]{i - 1, j}, possibleMoves);
        if(j + 1 < 8 && (board[i][j + 1].equals("  ") || playerOne && isUpperCase(board[i][j + 1]) || !playerOne && !isUpperCase(board[i][j + 1]))) possibleMoves = add(new int[]{i, j + 1}, possibleMoves);
        if(j - 1 >= 0 && (board[i][j - 1].equals("  ") || playerOne && isUpperCase(board[i][j - 1]) || !playerOne && !isUpperCase(board[i][j - 1]))) possibleMoves = add(new int[]{i, j - 1}, possibleMoves);
        if(i + 1 < 8 && j + 1 < 8 && (board[i + 1][j + 1].equals("  ") || playerOne && isUpperCase(board[i + 1][j + 1]) || !playerOne && !isUpperCase(board[i + 1][j + 1]))) possibleMoves = add(new int[]{i + 1, j + 1}, possibleMoves);
        if(i - 1 >= 0 && j - 1 >= 0 && (board[i - 1][j - 1].equals("  ") || playerOne && isUpperCase(board[i - 1][j - 1]) || !playerOne && !isUpperCase(board[i - 1][j - 1]))) possibleMoves = add(new int[]{i - 1, j - 1}, possibleMoves);
        if(i + 1 < 8 && j - 1 >= 0 && (board[i + 1][j - 1].equals("  ") || playerOne && isUpperCase(board[i + 1][j - 1]) || !playerOne && !isUpperCase(board[i + 1][j - 1]))) possibleMoves = add(new int[]{i + 1, j - 1}, possibleMoves);
        if(i - 1 >= 0 && j + 1 < 8 && (board[i - 1][j + 1].equals("  ") || playerOne && isUpperCase(board[i - 1][j + 1]) || !playerOne && !isUpperCase(board[i - 1][j + 1]))) possibleMoves = add(new int[]{i - 1, j + 1}, possibleMoves);
        if(!gotMoved[i][j]) {
          if(!gotMoved[i][7] && board[i][6].equals("  ") && board[i][5].equals("  ")) possibleMoves = add(new int[]{i, j + 2}, possibleMoves);
          if(!gotMoved[i][0] && board[i][3].equals("  ") && board[i][2].equals("  ") && board[i][1].equals("  ")) possibleMoves = add(new int[]{i, j - 2}, possibleMoves);
        }
        break;
      case "pw":
        if(playerOne) {
          if(i - 1 >= 0){
            if(board[i - 1][j].equals("  ")) possibleMoves = add(new int[]{i - 1, j}, possibleMoves);
            if(j + 1 < 8 && isUpperCase(board[i - 1][j + 1])) possibleMoves = add(new int[]{i - 1, j + 1}, possibleMoves);
            if(j - 1 >= 0 && isUpperCase(board[i - 1][j - 1])) possibleMoves = add(new int[]{i - 1, j - 1}, possibleMoves);
          }
          if(i == 6 && (board[i - 2][j].equals("  "))) possibleMoves = add(new int[]{i - 2, j}, possibleMoves);
        }
        else {
          if(i + 1 < 8){
            if(board[i + 1][j].equals("  ")) possibleMoves = add(new int[]{i + 1, j}, possibleMoves);
            if(j + 1 < 8 && !isUpperCase(board[i + 1][j + 1])) possibleMoves = add(new int[]{i + 1, j + 1}, possibleMoves);
            if(j - 1 >= 0 && !isUpperCase(board[i + 1][j - 1])) possibleMoves = add(new int[]{i + 1, j - 1}, possibleMoves);
          }
          if(i == 1 && (board[i + 2][j].equals("  "))) possibleMoves = add(new int[]{i + 2, j}, possibleMoves);

        }
        break;
    }

    return possibleMoves;
  }

  public int[][] getMovesFor(int i, int j) {
    switch(j) {
      case 0:
        return getMovesFor("A" + i);
      case 1:
        return getMovesFor("B" + i);
      case 2:
        return getMovesFor("C" + i);
      case 3:
        return getMovesFor("D" + i);
      case 4:
        return getMovesFor("E" + i);
      case 5:
        return getMovesFor("F" + i);
      case 6:
        return getMovesFor("G" + i);
      case 7:
        return getMovesFor("H" + i);
      default:
        return new int[][]{};
    }
  }

  public int[] getCoordsFor(String pos) {
    String[] coords = pos.split("");
    int i = 7 - (Integer.parseInt(coords[1]) - 1);
    int j;
    switch(coords[0].toUpperCase()) {
      case "A":
        j = 0;
        break;
      case "B":
        j = 1;
        break;
      case "C":
        j = 2;
        break;
      case "D":
        j = 3;
        break;
      case "E":
        j = 4;
        break;
      case "F":
        j = 5;
        break;
      case "G":
        j = 6;
        break;
      case "H":
        j = 7;
        break;
      default:
        return null;
    }
    return new int[] {i, j};
  }

  public String getFigureAt(String pos) {
    int[] coords = getCoordsFor(pos);
    return board[coords[0]][coords[1]];
  }

  public boolean isChecked() {
    int[][] allCurrentMoves = new int[][] {};
    for(int i = 0; i < board.length; i++) {
      for(int j = 0; j < board[i].length; j++) {
        String figure = board[i][j];
        if(playerOne) {
          if(!figure.equals("  ") && isUpperCase(figure)) {
            int[][] figuresMoves = getMovesFor(7 - i, j);
            for(int k = 0; k < figuresMoves.length; k++) {
              allCurrentMoves = add(figuresMoves[k], allCurrentMoves);
            }
          }
        } else {
          if(!figure.equals("  ") && !isUpperCase(figure)) {
            int[][] figuresMoves = getMovesFor(7 - i, j);
            for(int k = 0; k < figuresMoves.length; k++) {
              allCurrentMoves = add(figuresMoves[k], allCurrentMoves);
            }
          }
        }
      }
    }
    for(int i = 0; i < allCurrentMoves.length; i++) {
      System.out.print("[" + (7 - allCurrentMoves[i][0]) + ", " + allCurrentMoves[i][1] + "], ");
    }
    int[][] kings = getKingsPosition();
    for(int i = 0; i < allCurrentMoves.length; i++) {
      for(int k = 0; k < kings.length; k++) {
        if(allCurrentMoves[i][0] == kings[k][0] && allCurrentMoves[i][1] == kings[k][1]) {
          return true;
        }
      }
    }
    return false;
  }

  public int[][] getKingsPosition() {
    int[][] result = new int[][] {};
    for(int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if(board[i][j].toLowerCase().equals("ki")) result = add(new int[]{i, j}, result);
      }
    }
    return result;
  }

  public boolean won() {
    if(playerOne) {
      for(int i = 0; i < board.length; i++) {
        for(int j = 0; j < board[i].length; j++) {
          if(board[i][j].equals("ki")) return false;
        }
      }
    } else {
      for(int i = 0; i < board.length; i++) {
        for(int j = 0; j < board[i].length; j++) {
          if(board[i][j].equals("KI")) return false;
        }
      }
    }
    return true;
  }

  public void logBoard() {
    System.out.print("   | A  | B  | C  | D  | E  | F  | G  | H  |    ");
    if(!playerOne) System.out.print("--> ");
    System.out.println("Black (Wins: " + blackWins + ")");
    System.out.println("-----------------------------------------------");
    for(int i = 0; i < board[1].length; i++) {
      System.out.print(" " + (9 - (i + 1)) + " |");
      for(int j = 0; j < board.length; j++) {
        System.out.print(" " + board[i][j] + " |");
      }
      System.out.println(" " + (9 - (i + 1)) + "\n-----------------------------------------------");
    }
    System.out.print("   | A  | B  | C  | D  | E  | F  | G  | H  |    ");
    if(playerOne) System.out.print("--> ");
    System.out.println("White (Wins: " + whiteWins + ")");
  }

  public static void main(String[] args0) {
    boolean b = true;
    while(b) {
      System.out.println("\nPress Enter to start the game - Type anything else + Enter to end the Game");
      String input = Chess.sc.nextLine();
      b = input.equals("");
      if(b){
        Chess game = new Chess();
        game.start();
      }
    }
  }
}
