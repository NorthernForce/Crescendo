package frc.robot.subsystems;

import java.util.Optional;

import org.northernforce.commands.NFRRotatingArmJointSetAngle;
import org.northernforce.motors.MotorEncoderMismatchException;
import org.northernforce.motors.NFRSparkMax;
import org.northernforce.subsystems.arm.NFRRotatingArmJoint;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.commands.NFRWristContinuousAngle;
import frc.robot.constants.CrabbyConstants;
import frc.robot.dashboard.CrabbyDashboard;
public class WristJoint extends NFRRotatingArmJoint
{
    public CrabbyDashboard dashboard;
    private Rotation2d targetAngle = Rotation2d.fromDegrees(0);
    public WristJoint(NFRSparkMax wristController, NFRRotatingArmJointConfiguration wristConfig, CrabbyDashboard dashboard)
    {
        super(wristConfig, wristController, Optional.empty());
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

        dashboard.wristGauge.setSupplier(() -> getRotation().getDegrees());
    }
    /**
     * Does not change anything except for target variable
     * @param rotation
     */
    public void setTargetAngle(Rotation2d rotation)
    {
        targetAngle = rotation;
    }
    public Rotation2d getTargetAngle()
    {
        return targetAngle;
    }
}
