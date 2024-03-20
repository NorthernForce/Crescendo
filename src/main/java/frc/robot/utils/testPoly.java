package frc.robot.utils;

public class testPoly {
    public static void main(String[] args) {
        double[][] test = {{5,4,8,6,9},{6,8,2,4,-5},{9,5,7,3,6},{33,75,4322,472,-475},{-43,-65,437,-2222,1289}};
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
