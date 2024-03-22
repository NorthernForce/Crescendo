// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.northernforce.subsystems.arm;

import org.northernforce.motors.NFRMotorController;

import edu.wpi.first.math.geometry.Transform3d;

/** The subsystem to control a roller intake */
public class NFRRollerIntake extends NFRArmJoint {

    /** The config class for a {@link NFRRollerIntake}*/
    public static class NFRRollerIntakeConfiguration extends NFRArmJointConfiguration {
        protected double speedCoefficient = 1;

        /**
         * Creates a config for a {@link NFRRollerIntake}
         * @param name The name for the subsystem
         * @param speedCoefficient The Speed coefficient (usually either -1 or 1)
         */
        public NFRRollerIntakeConfiguration(String name, double speedCoefficient) {
            super(name);
            this.speedCoefficient = speedCoefficient;
        }

        /**
         * Creates a config for a {@link NFRRollerIntake}
         * @param name The name for the subsystem
         */
        public NFRRollerIntakeConfiguration(String name) {
            super(name);
        }

        /**
         * Sets the speed coefficient
         * @param speedCoefficient
         * @return This for chaining
         */
        public NFRRollerIntakeConfiguration withSpeedCoefficient(double speedCoefficient) {
            this.speedCoefficient = speedCoefficient;
            return this;
        }
    }
    
    protected final NFRMotorController controller;
    protected final NFRRollerIntakeConfiguration config;

    /**
     * Creates a NFRRollerIntake
     * @param config The config class
     * @param controller The motor controller for the intake
     */
    public NFRRollerIntake(NFRRollerIntakeConfiguration config, NFRMotorController controller) {
        super(config);
        this.config = config;
        this.controller = controller;
    }

    /**
     * Runs the roller intake at speed
     * @param speed The speed to run the roller intake at
     */
    public void setSpeed(double speed) {
        controller.set(speed * config.speedCoefficient);
    }

    public Transform3d getEndState() {
        return new Transform3d();
    }
}
