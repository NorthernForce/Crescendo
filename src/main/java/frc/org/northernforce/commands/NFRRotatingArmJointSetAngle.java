package org.northernforce.commands;

import java.util.Optional;

import org.northernforce.subsystems.arm.NFRRotatingArmJoint;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * Moves the arm joint to a position.
 */
public class NFRRotatingArmJointSetAngle extends Command
{
    protected final NFRRotatingArmJoint arm;
    protected final boolean useTrapezoidalPositioning;
    protected final int pidSlot;
    protected final Optional<PIDController> pidController;
    protected final Rotation2d targetAngle, tolerance;
    /**
     * Creates a new NFRRotatingArmJointSetAngle.
     * @param arm the arm subsystem
     * @param targetAngle the angle to go to
     * @param tolerance the angular tolerance
     * @param pidSlot the pid slot of the positional closed-loop
     * @param useTrapezoidalPositioning whether to use trapezoidal positioning
     */
    public NFRRotatingArmJointSetAngle(NFRRotatingArmJoint arm, Rotation2d targetAngle, Rotation2d tolerance,
        int pidSlot, boolean useTrapezoidalPositioning)
    {
        addRequirements(arm);
        this.arm = arm;
        this.useTrapezoidalPositioning = useTrapezoidalPositioning;
        this.pidSlot = pidSlot;
        this.pidController = Optional.empty();
        this.targetAngle = targetAngle;
        this.tolerance = tolerance;
    }
    /**
     * Creates a new NFRRotatingArmJointSetAngle.
     * @param arm the arm subsystem
     * @param targetAngle the angle to go to
     * @param tolerance the angular tolerance
     * @param controller the pid controller to use
     */
    public NFRRotatingArmJointSetAngle(NFRRotatingArmJoint arm, Rotation2d targetAngle, Rotation2d tolerance,
        PIDController controller)
    {
        addRequirements(arm);
        this.arm = arm;
        this.useTrapezoidalPositioning = false;
        this.pidSlot = -1;
        this.targetAngle = targetAngle;
        pidController = Optional.of(controller);
        this.tolerance = tolerance;
    }
    /**
     * If using a WPILIB pid controller, resets the controller. Else starts the integrated positional pid.
     */
    @Override
    public void initialize()
    {
        if (pidController.isPresent())
        {
            pidController.get().reset();
            pidController.get().setSetpoint(targetAngle.getRotations());
        }
        else if (useTrapezoidalPositioning)
        {
            arm.getController().setPositionTrapezoidal(pidSlot, targetAngle.getRotations());
        }
        else
        {
            arm.getController().setPosition(pidSlot, targetAngle.getRotations());
        }
    }
    /**
     * If using a WPILIB pid controller, sets the speed of the motor to the output of the feedback.
     */
    @Override
    public void execute()
    {
        if (pidController.isPresent())
        {
            arm.setOpenLoop(pidController.get().calculate(arm.getRotation().getRotations()));
        }
    }
    /**
     * Returns whether within tolerance of the targetAngle
     * @return within tolerance of the targetAngle
     */
    @Override
    public boolean isFinished()
    {
        return Math.abs(arm.getRotation().getRotations() - targetAngle.getRotations()) < tolerance.getRotations();
    }
    /**
     * Stops the arm.
     * @param interrupted whether the command was terminated before isFinished returned true.
     */
    @Override
    public void end(boolean interrupted)
    {
        arm.setOpenLoop(0);
    }
}