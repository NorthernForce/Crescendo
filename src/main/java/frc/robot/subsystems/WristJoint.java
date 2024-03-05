package frc.robot.subsystems;

import java.util.Optional;
import org.northernforce.motors.MotorEncoderMismatchException;
import org.northernforce.motors.NFRSparkMax;
import org.northernforce.subsystems.arm.NFRRotatingArmJoint;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.constants.CrabbyConstants;
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
        wristController.getAbsoluteEncoder().get().setConversionFactor(CrabbyConstants.WristConstants.wristEncoderRatio);
        wristController.getPIDController().setP(CrabbyConstants.WristConstants.kP);
        wristController.getPIDController().setI(CrabbyConstants.WristConstants.kI);
        wristController.getPIDController().setD(CrabbyConstants.WristConstants.kD);
        wristController.getPIDController().setSmartMotionMaxVelocity(CrabbyConstants.WristConstants.maxVelocity, 0);
        wristController.getPIDController().setSmartMotionMaxAccel(CrabbyConstants.WristConstants.maxAccel, 0);
        wristController.getPIDController().setSmartMotionAllowedClosedLoopError(CrabbyConstants.WristConstants.allowedClosedLoopError, 0);
        wristController.burnFlash();
    }
    public Rotation2d getSpeakerAngle(boolean useAbsolutePositioning, double distance)
    {
        if(useAbsolutePositioning)
        {
            //TODO use formula to generate an exact angle from distance
        } else 
        {
            //TODO use distance parameters to approximate angle needed (ex. if 0 < distance < 5, ampAngle = 55)
        }
        Rotation2d ampAngle = Rotation2d.fromDegrees(45); //for now, I will just use random degrees to test el motoro until I get data from ONE PARTICULAR CLAREDITH DYNAMIC DUO. *Ahem* Clare and Meredith...
        System.out.println("Fetching the angle using a formula from distance. Angle = " + ampAngle);
        return ampAngle; 
    }
}
