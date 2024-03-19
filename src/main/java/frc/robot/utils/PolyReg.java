package frc.robot.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.Point;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.util.function.Supplier;
import edu.wpi.first.wpilibj.RobotBase;

public class PolyReg implements TargetingCalculator{
    private File file;
    private double coeffs[];
    private CSVReader csvReader;
    private CSVWriter csvWriter;
    private static ArrayList<Point> points;
    private static double b;
    private static double a;
    public PolyReg(String filePath){
        
        points = new ArrayList<Point>();
        if (!RobotBase.isSimulation())
        {
            file = new File(filePath);
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
            try {
                csvReader = new CSVReader(new FileReader(file));
                csvWriter = new CSVWriter(new FileWriter(file));
                while(csvReader.readNext() != null){
                    String[] nextLine = csvReader.readNext();
                    addData(Double.parseDouble(nextLine[0]), Double.parseDouble(nextLine[1]));
                    points.add(new Point(Double.parseDouble(nextLine[0]), Double.parseDouble(nextLine[1])));
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        coeffs = new double[points.size()];
        findSkelequation();
    }
    @FunctionalInterface
    public interface VariableOperator 
    {
        double apply(double x);
    }

    public double[] findSkelequation() // XD XD XD LOLOLOLOLOLOLOLOLOLOL
    {
        double[][] origMatrices = new double[points.size()][points.size()];
        for(int row = 0; row < origMatrices.length; row++)
        {
            for(int col = 0; col < origMatrices[0].length; row++)
            {
                origMatrices[col][row] = Math.pow(coeffs[row],col);   
            }
        }

        return origMatrices[0]; // TODO CHANGE THIS!!!
    }
    // public double[][] findInverseMatrices(double[][] current)
    // {
    //     double[][] result = new double[current.length][current[0].length];
    //     double multAmount = 1.0 / ((Supplier<Double>) () -> 
    //     {
            
    //     }).get();

    // }
    // public double[][] matriceOfMinors(double[][] current)
    // {

    // }
    public static double determinantOfMatrix(double[][] current)
    {
        double[][] result = new double[current.length][current[0].length];
        double[][] tempLittleMatrixes = new double[current.length-1][current[0].length-1];

        double tempResult = 0;
        int cx = 0;
        int cy = 0;
        int colDown = 0;
        int rowDown = 0;
        int changeX = 0;
        int changed = 0;
        int pos = 0;
        for(int mx = 0; mx < result[0].length; mx++)
        {
            rowDown = 0;
            colDown = 0;
            for(int y = 1; y < result.length; y++)
            {

                
                for(int x = 0; x < result[0].length; x++)
                {
                    if(x != mx)
                    {
                        tempLittleMatrixes[colDown][rowDown] = current[y][x];
                        rowDown++;
                        changed++;
                        if(rowDown == tempLittleMatrixes[0].length)
                        {
                            rowDown = 0;
                            colDown++;
                        }
                        
                    }
                    else
                    {
                        changeX = 1;
                    }
                }   

            }
            tempResult+=current[0][mx]*(pos%2 == 0 ? 1 : -1)*(tempLittleMatrixes.length == 2 ? (tempLittleMatrixes[0][0]*tempLittleMatrixes[1][1]-tempLittleMatrixes[0][1]*tempLittleMatrixes[1][0]) : determinantOfMatrix(tempLittleMatrixes));
            pos++;
        }
        double noBottomFeeder =tempResult;
        return noBottomFeeder;
    }

    public VariableOperator evalSkelequation(double x)
    {
        VariableOperator operator = (operands) -> {
            double result = 0;
            double index = 0;
            double[] theCoeffs = coeffs;
            for (double coeff : coeffs) {
                result += coeff * Math.pow(x,index); // Example operation (sum)
                index++;
            }
            return result;
        };
        return operator;
    }

    @Override
    public double getValueForDistance(double distance) {
        return 0;
    }
    @Override
    public void addData(double distance, double value){
        if (!RobotBase.isSimulation())
        {
            csvWriter.writeNext(new String[]{Double.toString(distance), Double.toString(value)});
            points.add(new Point((distance), (value)));
            try {
                csvWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
