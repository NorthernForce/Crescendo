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
        wristController.getAbsoluteEncoder().get().setConversionFactor(1.0/3);
        wristController.getPIDController().setP(2);
        wristController.getPIDController().setI(0);
        wristController.getPIDController().setD(0);
        wristController.getPIDController().setSmartMotionMaxVelocity(5, 0);
        wristController.getPIDController().setSmartMotionMaxAccel(2, 0);
        wristController.getPIDController().setSmartMotionAllowedClosedLoopError(0.002, 0);
        wristController.burnFlash();

    }
    /**
     * 
     * @param positioningType string to represent angling method
     * @param distance double to represent distance from speaker
     * @return ampAngle returns Rotation2d to represent angle to shoot at
     * @exception IllegalArgumentException if position type is invalid 
     */
}
