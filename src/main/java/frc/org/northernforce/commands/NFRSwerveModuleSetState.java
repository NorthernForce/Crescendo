package org.northernforce.commands;

import org.northernforce.subsystems.drive.swerve.NFRSwerveModule;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * The command responsible for setting the state of a swerve module.
 */
public class NFRSwerveModuleSetState extends Command
{
    protected final NFRSwerveModule module;
    protected final int velocityPidSlot, positionalPidSlot;
    protected final boolean useTrapezoidalPositioning, useVelocityClosedLoop, usePositionalClosedLoop;
    protected final PIDController pidController;
    protected SwerveModuleState state;
    /**
     * Creates a new NFRSwerveModuleSetState.
     * @param module the module instance.
     * @param velocityPidSlot the velocity pid slot for closed-loop control
     * @param positionalPidSlot the positional pid slot for closed-loop control
     * @param useTrapezoidalPositioning whether to use advanced trapezoidal positioning (ex. motion magic) for control
     * (not yet recommended).
     */
    public NFRSwerveModuleSetState(NFRSwerveModule module, int velocityPidSlot, int positionalPidSlot,
        boolean useTrapezoidalPositioning)
    {
        addRequirements(module);
        this.module = module;
        this.velocityPidSlot = velocityPidSlot;
        this.positionalPidSlot = positionalPidSlot;
        this.useTrapezoidalPositioning = useTrapezoidalPositioning;
        useVelocityClosedLoop = true;
        usePositionalClosedLoop = true;
        pidController = null;
        state = null;
    }
    /**
     * Creates a new NFRSwerveModuleSetState.
     * @param module the module instance.
     * @param velocityPidSlot the velocity pid slot for closed-loop control
     * @param pidController the pid controller for the turning.
     */
    public NFRSwerveModuleSetState(NFRSwerveModule module, int velocityPidSlot, PIDController pidController)
    {
        addRequirements(module);
        this.module = module;
        this.velocityPidSlot = velocityPidSlot;
        this.positionalPidSlot = -1;
        this.useTrapezoidalPositioning = false;
        useVelocityClosedLoop = true;
        usePositionalClosedLoop = false;
        this.pidController = pidController;
        state = null;
    }
    /**
     * Creates a new NFRSwerveModuleSetState.
     * @param module the module instance.
     * @param pidController the pid controller for the turning.
     */
    public NFRSwerveModuleSetState(NFRSwerveModule module, PIDController pidController)
    {
        addRequirements(module);
        this.module = module;
        this.velocityPidSlot = -1;
        this.positionalPidSlot = -1;
        this.useTrapezoidalPositioning = false;
        useVelocityClosedLoop = false;
        usePositionalClosedLoop = false;
        this.pidController = pidController;
        state = null;
    }
    /**
     * Creates a new NFRSwerveModuleSetState.
     * @param module the module instance.
     * @param positionalPidSlot the positional pid slot for closed-loop control
     * @param useTrapezoidalPositioning whether to use advanced trapezoidal positioning (ex. motion magic) for control
     * (not yet recommended).
     */
    public NFRSwerveModuleSetState(NFRSwerveModule module, int positionalPidSlot,
        boolean useTrapezoidalPositioning)
    {
        addRequirements(module);
        this.module = module;
        this.velocityPidSlot = -1;
        this.positionalPidSlot = positionalPidSlot;
        this.useTrapezoidalPositioning = useTrapezoidalPositioning;
        useVelocityClosedLoop = false;
        usePositionalClosedLoop = true;
        pidController = null;
        state = null;
    }
    /**
     * Stets the target state of the module.
     * @param state the target speed and rotation.
     */
    public void setTargetState(SwerveModuleState state)
    {
        this.state = state;
        if (useVelocityClosedLoop)
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
     * Updates feedback as needed.
     */
    @Override
    public void execute()
    {
        if (!usePositionalClosedLoop && state != null)
        {
            module.setTurnSpeed(pidController.calculate(module.getRotation().getRotations()));
        }
    }
    /**
     * Gets the target state.
     * @return the target state (speed and rotation).
     */
    public SwerveModuleState getTargetState()
    {
        return state;
    }
}
