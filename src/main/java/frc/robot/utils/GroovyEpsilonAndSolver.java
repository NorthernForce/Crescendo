package frc.robot.utils;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

public class GroovyEpsilonAndSolver {
    public static double epsilon(String func, int indexStart, int indexEndOrN, double[] x, double[] y) {
        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(binding);
        double result = 0;
        String[] rewrittenFuncs = GroovyRewriteFunction.rewrite(func, x, y);
        for(int i = indexStart-1; i < indexEndOrN-1; i++)
        {
            try {
                result+=Double.parseDouble(shell.evaluate(rewrittenFuncs[i]).toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public static Object GroovySolver(String equation)
    {
        Binding binding = new Binding();
        GroovyShell shell = new GroovyShell(binding);
        return shell.evaluate(equation);
    }
}