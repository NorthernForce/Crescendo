package org.northernforce.commands;

import java.util.function.DoubleSupplier;

import org.northernforce.subsystems.arm.NFRRotatingArmJoint;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * Controls a rotating arm joint with a single input. Optionally uses closed-loop control.
 */
public class NFRRotatingArmJointWithJoystick extends Command
{
    protected final NFRRotatingArmJoint arm;
    protected final DoubleSupplier supplier;
    protected final boolean useClosedLoop;
    protected final int pidSlot;
    /**
     * Creates a new NFRRotatingArmJointWithJoystick
     * @param arm the arm subsystem
     * @param supplier the supplier for speed between [-1, 1]
     */
    public NFRRotatingArmJointWithJoystick(NFRRotatingArmJoint arm, DoubleSupplier supplier)
    {
        addRequirements(arm);
        this.arm = arm;
        this.supplier = supplier;
        this.useClosedLoop = false;
        this.pidSlot = -1;
    }
    /**
     * Creates a new NFRRotatingArmJointWithJoystick
     * @param arm the arm subsystem
     * @param supplier the supplier for speed between [-1, 1]
     * @param pidSlot the pid slot for velocity closed-loop control
     */
    public NFRRotatingArmJointWithJoystick(NFRRotatingArmJoint arm, DoubleSupplier supplier, int pidSlot)
    {
        addRequirements(arm);
        this.arm = arm;
        this.supplier = supplier;
        this.useClosedLoop = true;
        this.pidSlot = pidSlot;
    }
    /**
     * Sets the speed of the arm joint.
     */
    @Override
    public void execute()
    {
        if (useClosedLoop)
        {
            arm.setClosedLoop(supplier.getAsDouble(), pidSlot);
        }
        else
        {
            arm.setOpenLoop(supplier.getAsDouble());
        }
    }
}