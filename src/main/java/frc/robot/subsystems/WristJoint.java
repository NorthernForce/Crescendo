package frc.robot.subsystems;

import java.util.Optional;
import org.northernforce.motors.MotorEncoderMismatchException;
import org.northernforce.motors.NFRSparkMax;
import org.northernforce.subsystems.arm.NFRRotatingArmJoint;
public class WristJoint extends NFRRotatingArmJoint
{
    public WristJoint(NFRSparkMax wristController, NFRRotatingArmJointConfiguration wristConfig)
    {
        super(wristConfig, wristController, Optional.empty());
        System.out.println("Made WristJoint object");
        try {
            wristController.setSelectedEncoder(wristController.getAbsoluteEncoder().get());
        } catch (MotorEncoderMismatchException e) {
            e.printStackTrace();
        } 
        wristController.getPIDController().setP(0.25);
        wristController.getPIDController().setI(0);
        wristController.getPIDController().setD(0);       
        

    }
    public double getAmpAngle(boolean useAbsolutePositioning)
    {
        if(useAbsolutePositioning)
        {
            //TODO use formula to generate an exact angle from distance
        } else 
        {
            //TODO use distance parameters to approximate angle needed (ex. if 0 < distance < 5, ampAngle = 55)
        }
        double ampAngle = 90 * (Math.PI/180); //for now, I will just use 90 degrees to test el motoro until I get data from ONE PARTICULAR CLAREDITH DYNAMIC DUO. *Ahem* Clare and Meredith...
        System.out.println("Fetching the angle using a formula from distance. Angle = " + ampAngle);
        return ampAngle; 
    }
}