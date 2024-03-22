package org.northernforce.commands;

import org.northernforce.subsystems.arm.NFRSimpleMotorClaw;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * Opens a claw. Goes until the claw is open at a set speed.
 */
public class NFRSimpleMotorClawOpen extends Command
{
    protected final NFRSimpleMotorClaw claw;
    protected final double speed;
    protected final boolean useClosedLoop;
    protected final int pidSlot;
    /**
     * Creates a new NFRSimpleMotorClawOpen.
     * @param claw the claw subsystem.
     * @param speed the speed between [-1, 1]
     */
    public NFRSimpleMotorClawOpen(NFRSimpleMotorClaw claw, double speed)
    {
        addRequirements(claw);
        this.claw = claw;
        this.speed = speed;
        this.useClosedLoop = false;
        this.pidSlot = -1;
    }
    /**
     * Creates a new NFRSimpleMotorClawOpen. Uses closed-loop control.
     * @param claw the claw subsystem.
     * @param speed the speed as relative to motor.
     * @param pidSlot the pid slot for closed-loop control.
     */
    public NFRSimpleMotorClawOpen(NFRSimpleMotorClaw claw, double speed, int pidSlot)
    {
        addRequirements(claw);
        this.claw = claw;
        this.speed = speed;
        this.useClosedLoop = true;
        this.pidSlot = pidSlot;
    }
    /**
     * If using closed-loop conrol, sets the speed of the closed-loop feedback.
     */
    @Override
    public void initialize()
    {
        if (useClosedLoop)
        {
            claw.setClosedLoop(speed, pidSlot);
        }
    }
    /**
     * If not using closed-loop conrol, sets the power to the motor.
     */
    @Override
    public void execute()
    {
        claw.setOpenLoop(speed);
    }
    /**
     * Checks whether the claw is open.
     * @return whether the claw is open.
     */
    @Override
    public boolean isFinished()
    {
        return claw.isClosed();
    }
    /**
     * Stops the claw.
     * @param interrupted whether the command was terminated before isFinished returned true.
     */
    @Override
    public void end(boolean interrupted)
    {
        claw.setOpenLoop(0);
    }
}