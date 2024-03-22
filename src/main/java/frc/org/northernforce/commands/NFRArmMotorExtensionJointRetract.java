package org.northernforce.commands;

import org.northernforce.subsystems.arm.NFRArmMotorExtensionJoint;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * Retracts an extension joint controlled by a motor.
 */
public class NFRArmMotorExtensionJointRetract extends Command
{
    protected final NFRArmMotorExtensionJoint arm;
    protected final double speed;
    protected final boolean useClosedLoop;
    protected final int pidSlot;
    /**
     * Creates a new NFRArmMotorExtensionJointRetract.
     * @param arm the arm subsystem.
     * @param speed the speed to travel until at the forward limit.
     */
    public NFRArmMotorExtensionJointRetract(NFRArmMotorExtensionJoint arm, double speed)
    {
        addRequirements(arm);
        this.arm = arm;
        this.speed = speed;
        this.useClosedLoop = false;
        this.pidSlot = -1;
    }
    /**
     * Creates a new NFRArmMotorExtensionJointRetract. Uses closed-loop control.
     * @param arm the arm subsystem.
     * @param speed the speed to travel until at the forward limit.
     * @param pidSlot the pid slot of the velocity closed-loop control.
     */
    public NFRArmMotorExtensionJointRetract(NFRArmMotorExtensionJoint arm, double speed, int pidSlot)
    {
        addRequirements(arm);
        this.arm = arm;
        this.speed = speed;
        this.useClosedLoop = true;
        this.pidSlot = pidSlot;
    }
    /**
     * If using closed loop, sets the speed.
     */
    @Override
    public void initialize()
    {
        if (useClosedLoop)
        {
            arm.setClosedLoop(speed, pidSlot);
        }
    }
    /**
     * If using open loop, sets the speed.
     */
    @Override
    public void execute()
    {
        if (!useClosedLoop)
        {
            arm.setOpenLoop(speed);
        }
    }
    /**
     * Returns whether the arm is retracted.
     * @return whether the arm is retracted.
     */
    @Override
    public boolean isFinished()
    {
        return arm.isRetracted();
    }
    /**
     * Stops the retraction.
     * @param interrupted whether the arm reached its setpoint before stopping.
     */
    @Override
    public void end(boolean interrupted)
    {
        arm.setOpenLoop(0);
    }
}