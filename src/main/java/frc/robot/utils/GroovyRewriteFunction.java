package frc.robot.utils;

import java.util.ArrayList;
public class GroovyRewriteFunction {
    public static String[] rewrite(String function, double[] x, double[] y) {
        String[] result = new String[x.length];
        String[] arrayFunc = new String[function.length()];
        for(int i = 0; i < function.length(); i++) {
            arrayFunc[i] = function.substring(i, i+1);
        }
        ArrayList<Integer> xIndex = new ArrayList<Integer>();
        ArrayList<Integer> yIndex = new ArrayList<Integer>();
        
        for(int i = 0; i < x.length; i++) {
            for(int j = 0; j < function.length(); j++) {
                if(arrayFunc[j].equals("x")||xIndex.contains(j)) {
                    arrayFunc[j] = String.valueOf(x[i]);
                    if(!xIndex.contains(j))
                    {
                        xIndex.add(j);
                    }
                }
                if(arrayFunc[j].equals("y")||yIndex.contains(j)) {
                    arrayFunc[j] = String.valueOf(y[i]);
                    if(!yIndex.contains(j))
                    {
                        yIndex.add(j);
                    }
                }
            }
            result[i] = String.join("", arrayFunc);
            //System.out.println("Question: " + result[i] + "\nAnswer: " + shell.evaluate(result[i]));
        }
        return result;
    }
}
