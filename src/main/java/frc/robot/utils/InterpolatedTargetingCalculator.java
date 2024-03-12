package frc.robot.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import edu.wpi.first.math.interpolation.*;
import edu.wpi.first.wpilibj.RobotBase;

public class InterpolatedTargetingCalculator implements TargetingCalculator{
    private InterpolatingDoubleTreeMap treeMap;
    private File file;
    private CSVReader csvReader;
    private CSVWriter csvWriter;
    public InterpolatedTargetingCalculator(String filePath){
        treeMap = new InterpolatingDoubleTreeMap();
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
                csvWriter = new CSVWriter(new FileWriter(file, true));
                csvReader.forEach(nextLine -> {
                    treeMap.put(Double.parseDouble(nextLine[0]), Double.parseDouble(nextLine[1]));
                });
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
        try {
            return treeMap.get(distance);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
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
            try {
                csvWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}