package gloriaromanus;

public class Utils {
    public static double clamp (double a, double b, double c) {
        return Math.min(Math.max(a,b),c);
    }
    public static int clamp (int min, int value, int max) {
        return Math.min(Math.max(min,value),max);
    }
}
