package frc.robot.utils;

import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.swerve.NFRSwerveModule;

import edu.wpi.first.math.kinematics.SwerveModuleState;

public class SwerveModuleSetState extends NFRSwerveModuleSetState {

    /**
     * Creates a new NFRSwerveModuleSetState.
     * @param module the module instance.
     * @param velocityPidSlot the velocity pid slot for closed-loop control
     * @param positionalPidSlot the positional pid slot for closed-loop control
     * @param useTrapezoidalPositioning whether to use advanced trapezoidal positioning (ex. motion magic) for control
     * (not yet recommended).
     */
    public SwerveModuleSetState(NFRSwerveModule module, int velocityPidSlot, int positionalPidSlot,
            boolean useTrapezoidalPositioning) {
        super(module, velocityPidSlot, positionalPidSlot, useTrapezoidalPositioning);
    }
    
    /**
     * Stets the target state of the module.
     * @param state the target speed and rotation.
     * @param useMetersPerSecondVelocity if true will use close loop velocity configuration (also requires velocity pid controller to be set).
     */
    public void setTargetState(SwerveModuleState state, boolean useMetersPerSecondVelocity)
    {
        this.state = state;
        if (useVelocityClosedLoop && useMetersPerSecondVelocity)
        {
            module.setDriveSpeed(state.speedMetersPerSecond, velocityPidSlot);
        }
        else
        {
            module.setDriveSpeed(state.speedMetersPerSecond);
        }
        if (usePositionalClosedLoop)
        {
            module.setTurnPosition(state.angle, positionalPidSlot, useTrapezoidalPositioning);
        }
        else
        {
            pidController.reset();
            pidController.setSetpoint(state.angle.getRotations());
        }
    }

     /**
     * Stets the target state of the module.
     * Dose not give the possibility to use close loop velocity setting.
     * @param state the target speed and rotation.
     */
    @Override
    public void setTargetState(SwerveModuleState state) {
        this.state = state;
        module.setDriveSpeed(state.speedMetersPerSecond);
        if (usePositionalClosedLoop)
        {
            module.setTurnPosition(state.angle, positionalPidSlot, useTrapezoidalPositioning);
        }
        else
        {
            pidController.reset();
            pidController.setSetpoint(state.angle.getRotations());
        }
    }
}
