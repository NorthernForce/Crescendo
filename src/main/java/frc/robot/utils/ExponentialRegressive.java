package frc.robot.utils;

import java.util.function.Supplier;
import java.io.File;
import java.io.FileNotFoundException;
import org.littletonrobotics.junction.Logger;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.Point;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import edu.wpi.first.wpilibj.RobotBase;

public class ExponentialRegressive implements TargetingCalculator, LoggableHardware {
    private File file;
    private CSVReader csvReader;
    private CSVWriter csvWriter;
    private ArrayList<Point> points;
    private static double b;
    private static double a;
    private double[] xPoints;
    private double[] yPoints;

    public ExponentialRegressive(String filePath) {
        points = new ArrayList<Point>();
        if (!RobotBase.isSimulation()) {
            file = new File(filePath);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                csvReader = new CSVReader(new FileReader(file));
                csvWriter = new CSVWriter(new FileWriter(file, true));
                csvReader.forEach(nextLine -> {
                    points.add(new Point(Double.parseDouble(nextLine[0]), Double.parseDouble(nextLine[1])));
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }
        xPoints = ((Supplier<double[]>) () -> {
            double[] result = new double[points.size()];
            for (int i = 0; i < points.size(); i++) {
                result[i] = points.get(i).x;
            }
            return result;
        }).get();
        yPoints = ((Supplier<double[]>) () -> {
            double[] result = new double[points.size()];
            for (int i = 0; i < points.size(); i++) {
                result[i] = points.get(i).y;
            }
            return result;
        }).get();
        findRegressiveExponential(points);
    }

    /**
     * @param distance the distance from the speaker
     * @return the corresponding value at that index in the tree map
     */
    @Override

    public double getValueForDistance(double distance) {
        return getAngleForDistance(distance);
    }

    /**
     * Adds data to the tree map and csv file
     * 
     * @param distance the distance from the speaker
     * @param value    the value to add to the csv file
     */
    public void addData(double distance, double value) {
        if (!RobotBase.isSimulation()) {
            csvWriter.writeNext(new String[] { Double.toString(distance), Double.toString(value) });
            points.add(new Point((distance), (value)));
            try {
                csvWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void findRegressiveExponential(ArrayList<Point> angleToDistances) {
        if (angleToDistances.size() > 0) {
            double[] m_x = new double[angleToDistances.size()];
            double[] m_y = new double[angleToDistances.size()];
            for (int i = 0; i < angleToDistances.size(); i++) {
                m_x[i] = angleToDistances.get(i).x;
                m_y[i] = angleToDistances.get(i).y;
            }
            int n = angleToDistances.size();
            a = Math.exp((EpsilonAndSolver.epsilon((x, y) -> Math.pow(x, 2) * y, 1, n, m_x, m_y)
                    * (EpsilonAndSolver.epsilon((x, y) -> y * Math.log(y), 1, n, m_x, m_y)) -
                    (EpsilonAndSolver.epsilon((x, y) -> x * y, 1, n, m_x, m_y))
                            * (EpsilonAndSolver.epsilon((x, y) -> x * y * Math.log(y), 1, n, m_x, m_y)))
                    /
                    ((EpsilonAndSolver.epsilon((x, y) -> y, 1, n, m_x, m_y))
                            * (EpsilonAndSolver.epsilon((x, y) -> Math.pow(x, 2) * y, 1, n, m_x, m_y)) -
                            (Math.pow(EpsilonAndSolver.epsilon((x, y) -> x * y, 1, n, m_x, m_y), 2))));
            b = ((EpsilonAndSolver.epsilon((x, y) -> y, 1, n, m_x, m_y))
                    * (EpsilonAndSolver.epsilon((x, y) -> x * y * Math.log(y), 1, n, m_x, m_y)) -
                    (EpsilonAndSolver.epsilon((x, y) -> x * y, 1, n, m_x, m_y))
                            * (EpsilonAndSolver.epsilon((x, y) -> y * Math.log(y), 1, n, m_x, m_y)))
                    /
                    ((EpsilonAndSolver.epsilon((x, y) -> y, 1, n, m_x, m_y))
                            * (EpsilonAndSolver.epsilon((x, y) -> Math.pow(x, 2) * y, 1, n, m_x, m_y)) -
                            (Math.pow(EpsilonAndSolver.epsilon((x, y) -> x * y, 1, n, m_x, m_y), 2)));
        }

    }

    public static double getB() {
        return b;
    }

    public static double getA() {
        return a;
    }

    public static double getAngleForDistance(double distance) {
        return a * Math.pow(Math.exp(1), distance * b);
    }

    public static String getFunction() {
        return a + "e ^ (" + b + "x)";
    }

    @Override
    public void startLogging(double period) {

    }

    @Override
    public void logOutputs(String name) {
        Logger.recordOutput(name + "/ExponentialPoints/xPoints", xPoints);
        Logger.recordOutput(name + "/ExponentialPoints/yPoints", yPoints);
        Logger.recordOutput(name + "/ExponentialFunction", getFunction());
    }
}