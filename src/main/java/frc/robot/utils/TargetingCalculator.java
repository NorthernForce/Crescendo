package frc.robot.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import edu.wpi.first.math.interpolation.*;

public class TargetingCalculator {
    private InterpolatingDoubleTreeMap treeMap;
    private File file;
    private CSVReader csvReader;
    private CSVWriter csvWriter;
    public TargetingCalculator(String filePath){
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
            try {
                csvWriter = new CSVWriter(new FileWriter(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            treeMap = new InterpolatingDoubleTreeMap();
            try {
                while(csvReader.readNext() != null){
                    String[] nextLine = csvReader.readNext();
                    treeMap.put(Double.parseDouble(nextLine[0]), Double.parseDouble(nextLine[1]));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            
        }
    }

    /**
     * @param distance the distance from the speaker
     * @return the corresponding speed to run the shooters at
     */
    public double getSpeed(double distance){
        return treeMap.get(distance);
    }

    /**
     * Adds data to the tree map and csv file
     * @param distance the distance from the speaker
     * @param speed the speed that the shooters were running at
     */
    public void addData(double distance, double speed){
        csvWriter.writeNext(new String[]{Double.toString(distance) + "," + Double.toString(speed)});
        treeMap.put(distance, speed);
        try {
            csvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
