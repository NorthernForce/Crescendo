package org.northernforce.commands;

import org.northernforce.subsystems.arm.NFRArmMotorExtensionJoint;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * Extends an extension joint controlled by a motor.
 */
public class NFRArmMotorExtensionJointExtend extends Command
{
    protected final NFRArmMotorExtensionJoint arm;
    protected final double speed;
    protected final boolean useClosedLoop;
    protected final int pidSlot;
    /**
     * Creates a new NFRArmMotorExtensionJointExtend.
     * @param arm the arm subsystem.
     * @param speed the speed to travel until at the forward limit.
     */
    public NFRArmMotorExtensionJointExtend(NFRArmMotorExtensionJoint arm, double speed)
    {
        addRequirements(arm);
        this.arm = arm;
        this.speed = speed;
        this.useClosedLoop = false;
        this.pidSlot = -1;
    }
    /**
     * Creates a new NFRArmMotorExtensionJointExtend. Uses closed-loop control.
     * @param arm the arm subsystem.
     * @param speed the speed to travel until at the forward limit.
     * @param pidSlot the pid slot of the velocity closed-loop control.
     */
    public NFRArmMotorExtensionJointExtend(NFRArmMotorExtensionJoint arm, double speed, int pidSlot)
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
     * Returns whether the arm is extended.
     * @return whether the arm is extended.
     */
    @Override
    public boolean isFinished()
    {
        return arm.isExtended();
    }
    /**
     * Stops the extension.
     * @param interrupted whether the arm reached its setpoint before stopping.
     */
    @Override
    public void end(boolean interrupted)
    {
        arm.setOpenLoop(0);
    }
}