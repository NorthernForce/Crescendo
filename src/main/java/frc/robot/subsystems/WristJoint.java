package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.Optional;


import org.northernforce.motors.MotorEncoderMismatchException;
import org.northernforce.motors.NFRSparkMax;
import org.northernforce.subsystems.arm.NFRRotatingArmJoint;
import org.opencv.core.Point;

import edu.wpi.first.math.geometry.Rotation2d;

import frc.robot.constants.CrabbyConstants;
import frc.robot.utils.CSVFileReader;
import frc.robot.utils.GroovyEpsilonAndSolver;

public class WristJoint extends NFRRotatingArmJoint
{
    private String regressiveEquation; 
    public WristJoint(NFRSparkMax wristController, NFRRotatingArmJointConfiguration wristConfig)
    {
        super(wristConfig, wristController, Optional.empty());
        System.out.println("Made WristJoint object");
        try {
            wristController.setSelectedEncoder(wristController.getAbsoluteEncoder().get());
        } catch (MotorEncoderMismatchException e) {
            e.printStackTrace();
        }
        regressiveEquation = findRegressiveExponential(CSVFileReader.readFile("C:\\Users\\First\\Source\\Crescendo\\src\\main\\java\\frc\\robot\\utils\\AnglesToPoints.csv"), 0);
        wristController.getAbsoluteEncoder().get().setConversionFactor(CrabbyConstants.WristConstants.wristEncoderRatio);
        wristController.getPIDController().setP(CrabbyConstants.WristConstants.kP);
        wristController.getPIDController().setI(CrabbyConstants.WristConstants.kI);
        wristController.getPIDController().setD(CrabbyConstants.WristConstants.kD);
        wristController.getPIDController().setSmartMotionMaxVelocity(CrabbyConstants.WristConstants.maxVelocity, 0);
        wristController.getPIDController().setSmartMotionMaxAccel(CrabbyConstants.WristConstants.maxAccel, 0);
        wristController.getPIDController().setSmartMotionAllowedClosedLoopError(CrabbyConstants.WristConstants.allowedClosedLoopError, 0);
        wristController.burnFlash();
    }
    /**
     * 
     * @param positioningType string to represent angling method
     * @param distance double to represent distance from speaker
     * @return ampAngle returns Rotation2d to represent angle to shoot at
     * @exception IllegalArgumentException if position type is invalid 
     */
    public Rotation2d getSpeakerAngle(PositioningType positioningType, double distance)
    {
        Rotation2d ampAngle = new Rotation2d();
        switch(positioningType)
        {
            case Linear : 
                ampAngle = Rotation2d.fromDegrees(Math.atan(81/distance)*(180/Math.PI));
                break;

            case Regressive :
                ampAngle = Rotation2d.fromDegrees(getAngleFromDistance(10)); // TODO depending on if we use this, I will need to fetch distances
                break;

            case InRange :
                ampAngle = distance < 5.0 ? Rotation2d.fromDegrees(55) : 
                distance < 15 ? Rotation2d.fromDegrees(45) : null;
                break;

            default :
                throw new IllegalArgumentException("Positioning type: \"" + positioningType + "\" is invalid");
        }
       
        
        return ampAngle; 
    }
    public enum PositioningType
    {
        Linear,
        Regressive,
        InRange
    }
    public static String findRegressiveExponential(ArrayList<Point> points, double distance)
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
    public double getAngleFromDistance(double distance)
    {
        return Double.parseDouble(GroovyEpsilonAndSolver.GroovySolver(regressiveEquation + "" + distance + "))").toString());
    }
}
