public class HelperMethods {
  public static String numberToPosition(int n) {
    int h = n % 10;
    String ending;
    switch(h) {
      case 1:
        ending = "st";
        break;
      case 2:
        ending = "nd";
        break;
      case 3:
        ending = "rd";
        break;
      default:
        ending = "th";
    }
    return n + ending;
  }

  public static double roundTwoDecimals(double i) {
    return Math.round(i * 100)/100.0;
  }
}
