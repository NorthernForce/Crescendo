package frc.robot.subsystems;

import java.util.Optional;

import org.littletonrobotics.junction.Logger;
import org.northernforce.motors.MotorEncoderMismatchException;
import org.northernforce.motors.NFRSparkMax;
import org.northernforce.subsystems.arm.NFRRotatingArmJoint;

import frc.robot.constants.CrabbyConstants;
import frc.robot.dashboard.CrabbyDashboard;
import frc.robot.utils.LoggableHardware;
public class WristJoint extends NFRRotatingArmJoint implements LoggableHardware
{
    public CrabbyDashboard dashboard;
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
    @Override
    public void startLogging(double period) {
    }
    @Override
    public void logOutputs(String name) {
        Logger.recordOutput(name + "/Angle", getRotation());
        Logger.recordOutput(name + "/TargetAngle", CrabbyConstants.WristConstants.ampRotation.getDegrees());
        
        Logger.recordOutput(name + "/TargetCloseShotAngle", CrabbyConstants.WristConstants.closeShotRotation.getDegrees());        
    }
}
