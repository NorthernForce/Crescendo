package org.northernforce.subsystems.arm;

import org.northernforce.subsystems.NFRSubsystem;

import edu.wpi.first.math.geometry.Transform3d;

/**
 * This is a basic arm joint subsystem interface that defines a few methods. All arm joints must inherit from this.
 */
public abstract class NFRArmJoint extends NFRSubsystem
{
    /**
     * Simple arm joint configuration that is just NFRSubsystemConfiguration.
     */
    public static abstract class NFRArmJointConfiguration extends NFRSubsystemConfiguration
    {
        /**
         * Constructs a new configuration for an arm joint.
         * @param name the unique name of the subsystem.
         */
        public NFRArmJointConfiguration(String name)
        {
            super(name);
        }
    }
    /**
     * Creates a new arm joint config.
     * @param config a configuration containing parameters for the subsystem.
     */
    public NFRArmJoint(NFRArmJointConfiguration config)
    {
        super(config);
    }
    /**
     * Gets the transform from the origin of the joint to the end of the joint.
     * @return x, y, z, as well as roll, pitch, yaw
     */
    public abstract Transform3d getEndState();
}