public class TwoDimansionalArray {
  public static int[][] array = new int[3][4];

  public static void main(String args0[]) {
    teilaufgabeA();
    divider();
    teilaufgabeB();
    divider();
    teilaufgabeC();
  }

  //--------------------------------------------------
  //      Teilaufgaben
  //--------------------------------------------------

  public static void teilaufgabeA() {
    fillArrayRandom(array);
    for(int i = 0; i < array.length; i++) {
      for(int j = 0; j < array[i].length; j++) {
        print(array[i][j]);
      }
      System.out.println();
    }
  }

  public static void teilaufgabeB() {
    fillArrayRandom(array);
    for(int i = 0; i < array.length; i++) {
      int sumHori = 0;
      for(int j = 0; j < array[i].length; j++) {
        sumHori += array[i][j];
        print(array[i][j]);
      }
      System.out.println("\t" + sumHori);
    }
  }

  public static void teilaufgabeC() {
    fillArrayRandom(array);
    int[] sumVert = new int[array[0].length];
    for(int i = 0; i < array.length; i++) {
      int sumHori = 0;
      for(int j = 0; j < array[i].length; j++) {
        sumVert[j] += array[i][j];
        sumHori += array[i][j];
        print(array[i][j]);
      }
      System.out.println("\t" + sumHori);
    }
    System.out.println();
    for(int i = 0; i < sumVert.length; i++) {
      print(sumVert[i]);
    }
  }

  //--------------------------------------------------
  //      Helper Methods
  //--------------------------------------------------

  public static void fillArrayRandom(int[][] currentArray) {
    for(int i = 0; i < currentArray.length; i++) {
      for(int j = 0; j < currentArray[i].length; j++) {
        currentArray[i][j] = (int)(Math.random() * 10);
      }
    }
  }

  public static void print(int i) {
    if(i < 10) System.out.print(i + "  ");
    else System.out.print(i + " ");
  }

  public static void divider() {
    System.out.println("\n------------------------------------------\n");
  }
}
