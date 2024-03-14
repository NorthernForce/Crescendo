package frc.robot.utils;

import java.util.function.DoubleBinaryOperator;


public class EpsilonAndSolver {
    public static double epsilon(DoubleBinaryOperator func, int indexStart, int indexEndOrN, double[] x, double[] y) {
        double result = 0;
        //String[] rewrittenFuncs = GroovyRewriteFunction.rewrite(func, x, y);
        for(int i = indexStart-1; i < indexEndOrN; i++)
        {
            
            //MemoryCalculator.calcStorage();
            try {
                result+= func.applyAsDouble(x[i], y[i]);//Double.parseDouble(shell.evaluate(rewrittenFuncs[i]).toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}