package frc.robot.utils;

import java.io.BufferedReader;  
import java.io.FileReader;  
import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.Point;  
public class CSVFileReader  
{  
    public static ArrayList<Point> readFile(String filePath)   
    {  
        String line = "";  
        String splitBy = "\r\n";  
        String splitCoords = ",";
        ArrayList<Point> angleToDistance = new ArrayList<Point>();;
        try   
        {
            BufferedReader br = new BufferedReader(new FileReader(filePath));  
            while ((line = br.readLine()) != null) 
            {  
                String[] pointCoords = line.split(splitBy); 
                String[] coords = String.join("",pointCoords).split(splitCoords);
                int index = 0;
                double x = 0;
                double y = 0;
                Point currentPoint;
                for(String i : coords)
                {
                    //System.out.println(i + ".");
                    if(index%2 == 0)
                    {
                        x = Double.parseDouble(i);
                    } 
                    else 
                    {
                        y = Double.parseDouble(i);
                        currentPoint = new Point(x,y);
                        angleToDistance.add(currentPoint);
                    }
                    index++;
                }
            }  
            br.close();
        }   
        catch (IOException e)   
        {  
            e.printStackTrace();  
        }
        ///System.out.println(angleToDistance);
        return angleToDistance;
    }  
}  