package frc.robot.utils;

public class testPoly {
    public static void main(String[] args) {
        double[][] test = {{1,2,3},{4,5,6},{7,2,9}};
        double result = PolyReg.determinantOfMatrix(test);
        System.out.println(result);
        
    }
}
