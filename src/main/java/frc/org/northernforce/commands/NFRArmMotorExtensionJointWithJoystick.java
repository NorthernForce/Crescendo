package org.northernforce.commands;

import java.util.function.DoubleSupplier;

import org.northernforce.subsystems.arm.NFRArmMotorExtensionJoint;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * Controls an extension joint by a variable input.
 */
public class NFRArmMotorExtensionJointWithJoystick extends Command
{
    protected final NFRArmMotorExtensionJoint arm;
    protected final DoubleSupplier supplier;
    protected final boolean useClosedLoop;
    protected final int pidSlot;
    /**
     * Creates a new NFRArmMotorExtensionJointWithJoystick.
     * @param arm the arm subsystem.
     * @param supplier the supplier for speed.
     */
    public NFRArmMotorExtensionJointWithJoystick(NFRArmMotorExtensionJoint arm, DoubleSupplier supplier)
    {
        addRequirements(arm);
        this.arm = arm;
        this.supplier = supplier;
        this.useClosedLoop = false;
        this.pidSlot = -1;
    }
    /**
     * Creates a new NFRArmMotorExtensionJointWithJoystick. Uses closed-loop control.
     * @param arm the arm subsystem.
     * @param supplier the supplier for speed.
     * @param pidSlot the pid slot for closed-loop velocity control.
     */
    public NFRArmMotorExtensionJointWithJoystick(NFRArmMotorExtensionJoint arm, DoubleSupplier supplier, int pidSlot)
    {
        addRequirements(arm);
        this.arm = arm;
        this.supplier = supplier;
        this.useClosedLoop = true;
        this.pidSlot = pidSlot;
    }
    /**
     * Sets the speed using the supplier
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
