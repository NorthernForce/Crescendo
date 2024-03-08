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

import edu.wpi.first.wpilibj.RobotBase;

public class ExponentialRegressive implements TargetingCalculator{
    private File file;
    private CSVReader csvReader;
    private CSVWriter csvWriter;
    private ArrayList<Point> points;
    private String equation;
    public ExponentialRegressive(String filePath){
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
    }

    /**
     * @param distance the distance from the speaker
     * @return the corresponding value at that index in the tree map
     */
    @Override
    public double getValueForDistance(double distance){
        return Double.parseDouble(GroovyEpsilonAndSolver.GroovySolver(equation).toString());
    }

    /**
     * Adds data to the tree map and csv file
     * @param distance the distance from the speaker
     * @param value the value to add to the csv file
     */
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
        equation = findRegressiveExponential(points);
    }
    public static String findRegressiveExponential(ArrayList<Point> points)
    {
        double[] x = new double[points.size()];
        double[] y = new double[points.size()];
        for(int i = 0; i < points.size(); i++)
        {
            x[i] = points.get(i).x;
            y[i] = points.get(i).y;
        }
        int n = points.size();
        double a = Math.exp((GroovyEpsilonAndSolver.epsilon("Math.pow(x,2)*y",1, n+1,x,y) * (GroovyEpsilonAndSolver.epsilon("y*Math.log(y)",1, n+1,x,y)) - 
            (GroovyEpsilonAndSolver.epsilon("x*y",1, n+1,x,y))*(GroovyEpsilonAndSolver.epsilon("x*y*Math.log(y)",1, n+1,x,y))) / 
            ((GroovyEpsilonAndSolver.epsilon("y",1, n+1,x,y))*(GroovyEpsilonAndSolver.epsilon("Math.pow(x,2)*y",1, n+1,x,y)) - 
            (Math.pow(GroovyEpsilonAndSolver.epsilon("x*y",1, n+1,x,y),2))));
        double b = ((GroovyEpsilonAndSolver.epsilon("y",1, n+1,x,y))*(GroovyEpsilonAndSolver.epsilon("x*y*Math.log(y)",1, n+1,x,y))-
            (GroovyEpsilonAndSolver.epsilon("x*y",1, n+1,x,y))*(GroovyEpsilonAndSolver.epsilon("y*Math.log(y)",1, n+1,x,y)))/
            ((GroovyEpsilonAndSolver.epsilon("y",1, n+1,x,y))*(GroovyEpsilonAndSolver.epsilon("Math.pow(x,2)*y",1, n+1,x,y))-
            (Math.pow(GroovyEpsilonAndSolver.epsilon("x*y",1, n+1,x,y),2)));
        String finalEquation = (a+"*Math.pow(Math.exp(1),(" + b + "*");
        return finalEquation;
        
    }
}
