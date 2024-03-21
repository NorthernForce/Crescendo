package frc.robot.utils;

import java.util.ArrayList;
import java.util.Arrays;

import org.opencv.core.Point;

public class testPoly {
    public static void main(String[] args) {
        double[][] current = {{0,50,-9},{76,-58,4},{-306,720,5}};
        ArrayList<Point> points = new ArrayList<Point>(Arrays.asList(new Point(0,0), new Point(1.5,1.5), new Point(4,1)));
        PolyReg polyReg = new PolyReg("sussy baka on a stick", points);
        //for(double[] y : polyReg.findInverseMatrices(current)) for(double x : y) {System.out.print(x + " ");} System.out.println();
        
    }
}
