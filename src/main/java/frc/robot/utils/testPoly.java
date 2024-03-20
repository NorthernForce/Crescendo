package frc.robot.utils;

public class testPoly {
    public static void main(String[] args) {
        double[][] test = {{1,2,3},{4,5,6},{7,2,9}};
        System.out.println(test.length);
        double result = PolyReg.determinantOfMatrix(test,1);
        System.out.println(result + "\n\n");
        for(double[] x : PolyReg.findInverseMatrices(test))
        {
            for(double y : x)
            {
                System.out.print(y + " ");
            }
            System.out.println();
        }
        
    }
}
