package frc.robot.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.opencv.core.Point;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.util.function.Supplier;
import edu.wpi.first.wpilibj.RobotBase;

public class PolyReg implements TargetingCalculator {
    private File file;
    private static double coeffs[];
    private CSVReader csvReader;
    private CSVWriter csvWriter;
    private static ArrayList<Point> points;
    private double[] xPoints;
    private double[] yPoints;
    private static double b;
    private static double a;

    public PolyReg(String filePath, ArrayList<Point> pointss) {
        xPoints = new double[pointss.size()];
        yPoints = new double[pointss.size()];
        points = pointss;
        // if (!RobotBase.isSimulation()) {
        //     file = new File(filePath);
        //     if (!file.exists()) {
        //         try {
        //             file.createNewFile();
        //         } catch (IOException e) {
        //             e.printStackTrace();
        //         }
        //     }
        //     try {
        //         csvReader = new CSVReader(new FileReader(file));
        //         csvWriter = new CSVWriter(new FileWriter(file));
        //         while (csvReader.readNext() != null) {
        //             String[] nextLine = csvReader.readNext();
        //             addData(Double.parseDouble(nextLine[0]), Double.parseDouble(nextLine[1]));
        //             points.add(new Point(Double.parseDouble(nextLine[0]), Double.parseDouble(nextLine[1])));
        //         }
        //     } catch (FileNotFoundException e) {
        //         e.printStackTrace();
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     } catch (NumberFormatException e) {
        //         e.printStackTrace();
        //     }
        // }
        for (int i = 0; i < points.size(); i++) {
            xPoints[i] = points.get(i).x;
            yPoints[i] = points.get(i).y;
            System.out.println("(" + xPoints[i] + ", " + yPoints[i] + ")");
        }
        coeffs = new double[points.size()];
        coeffs = findSkelequation(yPoints, xPoints, false);
    }

    @FunctionalInterface
    public interface VariableOperator {
        double apply(double x);
    }

    public static double[] findSkelequation(double[] yValues, double[] xValues, boolean useManMadePower) // XD XD XD LOLOLOLOLOLOLOLOLOLOL
    {
        if(useManMadePower)
        {
        double[] result = new double[yValues.length];
        double[][] origMatrices = new double[yValues.length][yValues.length];
        for (int col = 0; col < origMatrices.length; col++) {
            for (int row = 0; row < origMatrices[0].length; row++) {
                System.out.print(Math.pow(xValues[row], col) + " ");
                origMatrices[row][col] = Math.pow(xValues[row], col);
            }
            System.out.println();
        }
        origMatrices = findInverseMatrices(origMatrices);
        for (int y = 0; y < origMatrices.length; y++) {
            for (int x = 0; x < origMatrices[0].length; x++) {
                //System.out.print(origMatrices[y][x] + " * " + yValues[x] + " = " + origMatrices[y][x] * yValues[x] + ", ");
                origMatrices[y][x] *= yValues[x];
                System.out.print(origMatrices[y][x] + " ");
            }
            System.out.println();
        }
        int index = 0;
        for (double[] y : origMatrices) {
            for (double x : y) {
                result[index] += x;
                coeffs[index] = result[index];
            }
            index++;
        }
    
        return result;
    } 
    else{
        WeightedObservedPoints obs = new WeightedObservedPoints();
        for (int i = 0; i < xValues.length; i++) {
            obs.add(xValues[i], yValues[i]);
        }

        // Perform polynomial curve fitting
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2); // Change 2 to the degree of polynomial you want
        double[] coefficients = fitter.fit(obs.toList());

        return coefficients;
    }
    }

    public static double[][] findInverseMatrices(double[][] current) {
        // for(double[] y : current)
        // {
        //     for(double x : y)
        //     {
        //         System.out.print(x + " ");
        //     }
        //     System.out.println();
        // }
        System.out.println();
        double[][] original = adjugateMatrix(current);
        double[][] result = new double[current.length][current[0].length];
        double multAmount = 1.0/determinantOfMatrix(current, 1);
        System.out.println(multAmount);
        for (int y = 0; y < result.length; y++) {
            for (int x = 0; x < result[0].length; x++) {
                original[y][x] *= multAmount;
            }
        }
        // for(double[] y : current)
        // {
        //     for(double x : y)
        //     {
        //         System.out.print(x + " ");
        //     }
        //     System.out.println();
        // }
        // System.out.println();
        
        return original;

    }

    public static double[][] adjugateMatrix(double[][] current) {
        double[][] original = matrixOfCofactors(current);
        double[][] result = new double[current.length][current[0].length];
        for (int y = 0; y < current.length; y++) {
            for (int x = 0; x < current[0].length; x++) {
                result[y][x] = original[x][y];
            }
        }
        return result;
    }

    public static double[][] matrixOfCofactors(double[][] current) {
        int pos = 0;
        double[][] result = new double[current.length][current[0].length];
        for (int y = 0; y < current.length; y++) {
            for (int x = 0; x < current[0].length; x++) {
                result[y][x] = determinantOfMatrix(externalMiniOperator(current, x, y), 1) * -1 * (pos % 2 * 2 - 1);
                pos++;
            }
        }
        return result;
    }

    public static double determinantOfMatrix(double[][] current, double multBy) {
        if (current.length > 2) {
            double[][] result = new double[current.length][current[0].length];
            double[][] tempLittleMatrixes = new double[current.length - 1][current[0].length - 1];

            double tempResult = 0;
            int cx = 0;
            int cy = 0;
            int colDown = 0;
            int rowDown = 0;
            int changeX = 0;
            int changed = 0;
            int pos = 0;
            for (int mx = 0; mx < result[0].length; mx++) {
                rowDown = 0;
                colDown = 0;
                for (int y = 1; y < result.length; y++) {

                    for (int x = 0; x < result[0].length; x++) {
                        if (x != mx) {
                            tempLittleMatrixes[colDown][rowDown] = current[y][x];
                            rowDown++;
                            changed++;
                            if (rowDown == tempLittleMatrixes[0].length) {
                                rowDown = 0;
                                colDown++;
                            }

                        } else {
                            changeX = 1;
                        }
                    }

                }
                tempResult += current[0][mx] * -1 * (pos % 2 * 2 - 1) * determinantOfMatrix(tempLittleMatrixes, 1);
                pos++;
            }
            double noBottomFeeder = tempResult;
            return noBottomFeeder;
        } else {
            return (current[0][0] * current[1][1] - current[0][1] * current[1][0]) * multBy;
        }
    }

    public static double[][] externalMiniOperator(double[][] current, int mx, int my) {
        int rowDown = 0;
        int colDown = 0;
        double[][] tempLittleMatrixes = new double[current.length - 1][current[0].length - 1];
        for (int y = 0; y < current.length; y++) {

            for (int x = 0; x < current[0].length; x++) {
                if (x != mx && y != my) {
                    tempLittleMatrixes[colDown][rowDown] = current[y][x];
                    rowDown++;
                    if (rowDown == tempLittleMatrixes[0].length) {
                        rowDown = 0;
                        colDown++;
                    }

                }
            }

        }
        return tempLittleMatrixes;
    }

    public VariableOperator evalSkelequation(double x) {
        VariableOperator operator = (operands) -> {
            double result = 0;
            double index = 0;
            double[] theCoeffs = coeffs;
            for (double coeff : coeffs) {
                result += coeff * Math.pow(x, index); // Example operation (sum)
                index++;
            }
            return result;
        };
        return operator;
    }
    public String getSkelequation()
    {
        String result = "";
        int index = 0;
        for(double i : coeffs)
        {
            result += i + "x^" + index + "+";
            index++;
        }
        return result.substring(0,result.length()-1);
    }

    @Override
    public double getValueForDistance(double distance) {
        return 0;
    }

    @Override
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

}