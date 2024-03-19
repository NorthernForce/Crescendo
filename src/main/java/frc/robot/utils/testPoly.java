package frc.robot.utils;

public class testPoly {
    public static void main(String[] args) {
        double[][] test = {{6,1,1},{4,-2,5},{2,8,7}};
        double result = (double)PolyReg.determinantOfMatrix(test);
        System.out.println(result);
        
    }
}
