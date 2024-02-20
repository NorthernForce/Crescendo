package frc.robot.subsystems;

import java.util.Optional;

import org.northernforce.encoders.NFRAbsoluteEncoder;
import org.northernforce.encoders.NFRCANCoder;
import org.northernforce.motors.MotorEncoderMismatchException;
import org.northernforce.motors.NFRSparkMax;
import org.northernforce.subsystems.arm.NFRRotatingArmJoint;
import org.northernforce.subsystems.arm.NFRRotatingArmJoint.NFRRotatingArmJointConfiguration;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class WristJoint extends SubsystemBase
{
    private NFRRotatingArmJoint wristJoint;
    private int m_pin;
    
    public WristJoint()
    {
        this(14);
    }
    public WristJoint(int neoNum)
    {
        System.out.println("Made WristJoint object");
        NFRSparkMax wristController = new NFRSparkMax(MotorType.kBrushless, neoNum);
        NFRRotatingArmJointConfiguration wristConfig = new NFRRotatingArmJointConfiguration("wristConfig");
        try {
            wristController.setSelectedEncoder(wristController.getAbsoluteEncoder().get());
        } catch (MotorEncoderMismatchException e) {
            e.printStackTrace();
        }
        wristJoint = new NFRRotatingArmJoint(wristConfig, wristController, Optional.empty());

        m_pin = neoNum;

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
        double ampAngle = 90; //for now, I will just use 90 degrees to test el motoro until I get data from ONE PARTICULAR CLAREDITH DYNAMIC DUO. *Ahem* Clare and Meredith...
        System.out.println("Fetching the angle using a formula from distance. Angle = " + ampAngle);
        return ampAngle; 
    }
    public NFRRotatingArmJoint getMotor()
    {
        System.out.println("Fetching arm");
        return wristJoint;
    }
    public double getGearRatio()
    {
        double gearRatio = 4; //TODO properly tune gear ratio
        System.out.println("Fetching gear ratio (" + gearRatio + ")");
        return gearRatio; 
    }
     
}
