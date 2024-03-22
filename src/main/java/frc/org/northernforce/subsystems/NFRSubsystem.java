package org.northernforce.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * This is a simple abstract class that all northern force subsystems must inherit from.
 */
public abstract class NFRSubsystem extends SubsystemBase
{
    /**
     * This is a very simple configuration for this abstract subsystem that subsystem configurations must inherit from.
     */
    public static abstract class NFRSubsystemConfiguration
    {
        protected final String name;
        /**
         * Creates a new nfr subsystem configuration.
         * @param name a unique name that identifies this subsystem.
         */
        public NFRSubsystemConfiguration(String name)
        {
            this.name = name;
        }
    }
    /**
     * Initializes the abstract components of NFR Subsystem with a configuration.
     * @param config a class that inherits from the basic NFR subsystem config.
     */
    public NFRSubsystem(NFRSubsystemConfiguration config)
    {
        super();
        setName(config.name);
    }
}