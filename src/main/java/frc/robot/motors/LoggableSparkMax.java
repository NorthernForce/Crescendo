package frc.robot.motors;

import org.littletonrobotics.junction.Logger;
import org.northernforce.motors.NFRSparkMax;

import frc.robot.utils.LoggableHardware;

public class LoggableSparkMax extends NFRSparkMax implements LoggableHardware {
    /**
     * Creates a new NFR Spark Max.
     * 
     * @param type      The Rev Motor Type. (Brushed or Brushless)
     * @param primaryID The primary id of the motor controller
     */
    public LoggableSparkMax(MotorType type, int primaryID) {
        super(type, primaryID);
    }
    /**
     * Creates a new NFR Spark Max.
     * 
     * @param type        The Rev Motor Type. (Brushed or Brushless)
     * @param primaryId   The primary id of the motor controller
     * @param followerIds the ids of the follower controllers
     */
    public LoggableSparkMax(MotorType type, int primaryID, int... followers) {
        super(type, primaryID, followers);
    }
    @Override
    public void startLogging(double period) {
    }
    @Override
    public void logOutputs(String name) {
        Logger.recordOutput(name + "/Velocity", getSelectedEncoder().getVelocity());
        Logger.recordOutput(name + "/Position", getSelectedEncoder().getPosition());
        Logger.recordOutput(name + "/IntegratedVelocity", getIntegratedEncoder().getVelocity());
        Logger.recordOutput(name + "/IntegratedPosition", getIntegratedEncoder().getPosition());
        if (getAbsoluteEncoder().isPresent())
        {
            Logger.recordOutput(name + "/AbsolutePosition", getAbsoluteEncoder().get().getAbsolutePosition());
            Logger.recordOutput(name + "/AbsoluteOffset", getAbsoluteEncoder().get().getAbsoluteOffset());
            Logger.recordOutput(name + "/AbsoluteVelocity", getAbsoluteEncoder().get().getAbsoluteVelocity());
        }
        Logger.recordOutput(name + "/Temperature", getMotorTemperature());
        Logger.recordOutput(name + "/OutputCurrent", getOutputCurrent());
        Logger.recordOutput(name + "/ForwardSoftLimit", getFault(FaultID.kSoftLimitFwd));
        Logger.recordOutput(name + "/ReverseSoftLimit", getFault(FaultID.kSoftLimitRev));
        Logger.recordOutput(name + "/SensorFault", getFault(FaultID.kSensorFault));
    }
}
