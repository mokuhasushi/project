package project.unsw.gloriaromanus;

public class Utils {
    public static double clamp (double a, double b, double c) {
        return Math.min(Math.max(a,b),c);
    }
    public static int clamp (int a, int b, int c) {
        return Math.min(Math.max(a,b),c);
    }
}
