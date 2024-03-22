package org.northernforce.subsystems.drive;

import org.northernforce.motors.MotorEncoderMismatchException;
import org.northernforce.motors.NFRMotorController;
import org.northernforce.gyros.NFRGyro;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.DifferentialDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;

/**
 * This is a generic tank drive that supports closed loop and open loop control. Uses common NFR interfaces such as
 * NFRGyro and NFRMotorController. Inherits from NFRDrive.
 */
public class NFRTankDrive extends NFRDrive
{
    /**
     * This holds configurations for simulation and real driving of the robot.
     */
    public static class NFRTankDriveConfiguration extends NFRDriveConfiguration
    {
        protected double trackWidth, gearRatio, wheelRadius, moi, mass, maxSpeed;
        protected DCMotor gearbox;
        /**
         * Creates a new NFRTankDriveConfiguration.
         * @param name the unique identifier for the subsystem, eg. "drive"
         */
        public NFRTankDriveConfiguration(String name)
        {
            super(name);
            this.trackWidth = 0;
            this.gearRatio = 0;
            this.wheelRadius = 0;
            this.moi = 0;
            this.mass = 0;
            this.maxSpeed = 0;
            this.gearbox = null;
        }
        /**
         * Creates a new NFRTankDriveConfiguration.
         * @param name the unique identifier for the subsystem, eg. "drive"
         * @param trackWidth the width of the chassis in meters
         * @param gearRatio the gear ratio between the motor and the wheel.
         * @param wheelRadius the radius of the wheel in meters
         * @param moi the moment of inertia of the drive train. Can be left as zero if simulation is not used.
         * @param mass the mass of the robot. Can be left as zero is simulation is not used.
         * @param maxSpeed the max speed of the robot. Best estimation is used for closed loop control. It is in
         * meters/s.
         * @param gearbox the gearbox that the controller is controlling. Only necessary if simulation is used.
         */
        public NFRTankDriveConfiguration(String name, double trackWidth, double gearRatio, double wheelRadius, double moi,
            double mass, double maxSpeed, DCMotor gearbox)
        {
            super(name);
            this.trackWidth = trackWidth;
            this.gearRatio = gearRatio;
            this.wheelRadius = wheelRadius;
            this.gearbox = gearbox;
            this.moi = moi;
            this.mass = mass;
            this.maxSpeed = maxSpeed;
        }
        /**
         * With track width.
         * @param trackWidth the width of the track in meters.
         * @return this
         */
        public NFRTankDriveConfiguration withTrackWidth(double trackWidth)
        {
            this.trackWidth = trackWidth;
            return this;
        }
        /**
         * With gear ratio.
         * @param gearRatio the gear ratio between the drive encoder and a rotation of the wheel.
         * @return this
         */
        public NFRTankDriveConfiguration withGearRatio(double gearRatio)
        {
            this.gearRatio = gearRatio;
            return this;
        }
        /**
         * With wheel radius.
         * @param wheelRadius the radius of the wheel
         * @return this
         */
        public NFRTankDriveConfiguration withWheelRadius(double wheelRadius)
        {
            this.wheelRadius = wheelRadius;
            return this;
        }
        /**
         * With moment of inertia.
         * @param moi the moment of inertia. ONLY USED IN SIMULATION.
         * @return this
         */
        public NFRTankDriveConfiguration withMOI(double moi)
        {
            this.moi = moi;
            return this;
        }
        /**
         * With mass.
         * @param mass the mass of the robot in kg. ONLY USED IN SIMULATION.
         * @return this
         */
        public NFRTankDriveConfiguration withMass(double mass)
        {
            this.mass = mass;
            return this;
        }
        /**
         * With max speed.
         * @param maxSpeed the max speed of the robot used for closed loop control (in m/s). Used for closed loop control.
         * @return this
         */
        public NFRTankDriveConfiguration withMaxSpeed(double maxSpeed)
        {
            this.maxSpeed = maxSpeed;
            return this;
        }
        /**
         * With gearbox.
         * @param gearbox the gearbox of each drive side. ONLY USED IN SIMULATION.
         * @return this
         */
        public NFRTankDriveConfiguration withGearbox(DCMotor gearbox)
        {
            this.gearbox = gearbox;
            return this;
        }
    }
    protected final DifferentialDriveKinematics kinematics;
    protected final NFRMotorController leftSide, rightSide;
    protected final DifferentialDrive robotDrive;
    protected final DifferentialDrivePoseEstimator estimator;
    protected final DifferentialDriveOdometry odometry;
    protected final NFRGyro gyro;
    protected final DifferentialDrivetrainSim simulator;
    protected final NFRTankDriveConfiguration config;
    protected final Notifier notifier;
    /**
     * Creates a new NFR Tank Drive.
     * @param config the class containing the configuration parameters.
     * @param leftSide the NFRMotorController for the left side
     * @param rightSide the NFRMotorController for the right side
     * @param gyro the gyroscope
     */
    public NFRTankDrive(NFRTankDriveConfiguration config, NFRMotorController leftSide, NFRMotorController rightSide,
        NFRGyro gyro)
    {
        super(config);
        this.config = config;
        kinematics = new DifferentialDriveKinematics(config.trackWidth);
        this.leftSide = leftSide;
        this.rightSide = rightSide;
        robotDrive = new DifferentialDrive(leftSide, rightSide);
        try
        {
            leftSide.setSelectedEncoder(leftSide.getIntegratedEncoder());
            rightSide.setSelectedEncoder(rightSide.getIntegratedEncoder());
        }
        catch (MotorEncoderMismatchException e)
        {
            e.printStackTrace();
        }
        leftSide.getSelectedEncoder().setConversionFactor((Math.PI * config.wheelRadius * 2) / (config.gearRatio));
        rightSide.getSelectedEncoder().setConversionFactor((Math.PI * config.wheelRadius * 2) / (config.gearRatio));
        this.gyro = gyro;
        estimator = new DifferentialDrivePoseEstimator(kinematics, gyro.getGyroYaw(),
            leftSide.getSelectedEncoder().getPosition(), rightSide.getSelectedEncoder().getPosition(),
            new Pose2d());
        odometry = new DifferentialDriveOdometry(gyro.getGyroYaw(),
            leftSide.getSelectedEncoder().getPosition(), rightSide.getSelectedEncoder().getPosition());
        if (RobotBase.isSimulation())
        {
            simulator = new DifferentialDrivetrainSim(
                config.gearbox, config.gearRatio, config.moi, config.mass,
                config.wheelRadius, config.trackWidth,
                VecBuilder.fill(0.001, 0.001, 0.001, 0.1, 0.1, 0.005, 0.005)
            );
        }
        else
        {
            simulator = null;
        }
        notifier = new Notifier(this::updateOdometry);
        notifier.startPeriodic(0.02);
    }
    /**
     * Gets the estimated pose that is a combination of odometry and vision estimates
     * @return pose consisting of x, y, and theta. Relative to wpilib origin (blue corner).
     */
    @Override
    public Pose2d getEstimatedPose()
    {
        return estimator.getEstimatedPosition();
    }
    /**
     * Resets the pose to a new pose. This is meant for at the start of a match. Not meant for vision readings.
     * @param newPose pose consisting of x, y, and theta. Relative to wpilib origin (blue corner).
     */
    @Override
    public void resetPose(Pose2d newPose)
    {
        estimator.resetPosition(
            gyro.getGyroYaw(),
            leftSide.getSelectedEncoder().getPosition(),
            rightSide.getSelectedEncoder().getPosition(),
            newPose
        );
    }
    /**
     * Adds a vision estimate that is factored into the pose estimation.
     * @param timestamp timestamp of the pose. Seconds.
     * @param pose pose consisting of x, y, and theta. Relative to wpilib origin (blue corner).
     */
    @Override
    public void addVisionEstimate(double timestamp, Pose2d pose)
    {
        estimator.addVisionMeasurement(pose, timestamp);
    }
    /**
     * Gets the current chassis speeds as recored by the encoder
     * @return chassis speeds which holds vx, vy, and vtheta.
     */
    @Override
    public ChassisSpeeds getChassisSpeeds()
    {
        return kinematics.toChassisSpeeds(new DifferentialDriveWheelSpeeds(
            leftSide.getSelectedEncoder().getVelocity(),
            rightSide.getSelectedEncoder().getVelocity()
        ));
    }
    /**
     * This is the periodic function. In it, the estimator is fed the current information from the encoders and
     * gyroscope.
     */
    @Override
    public void periodic()
    {
    }
    /**
     * Updates the odometry and pose estimator with module positions.
     */
    public void updateOdometry()
    {
        estimator.updateWithTime(
            System.currentTimeMillis() / 1000,
            gyro.getGyroYaw(),
            leftSide.getSelectedEncoder().getPosition(),
            rightSide.getSelectedEncoder().getPosition()
        );
        odometry.update(
            gyro.getGyroYaw(),
            leftSide.getSelectedEncoder().getPosition(),
            rightSide.getSelectedEncoder().getPosition()
        );
    }
    /**
     * Gets the odometry
     * @return the odometry
     */
    public DifferentialDriveOdometry getOdometry()
    {
        return odometry;
    }
    /**
     * This is the simulation periodic function. The simulator is fed the inputs from the motors, and the simulation
     * returns new encoder values, and a new gyroscope heading.
     */
    @Override
    public void simulationPeriodic()
    {
        simulator.setInputs(
            leftSide.get() * RobotController.getBatteryVoltage(),
            rightSide.get() * RobotController.getBatteryVoltage()
        );
        simulator.update(0.02);
        leftSide.getSelectedEncoder().addSimulationPosition(simulator.getLeftVelocityMetersPerSecond() * 0.02);
        rightSide.getSelectedEncoder().addSimulationPosition(simulator.getRightVelocityMetersPerSecond() * 0.02);
        gyro.addSimulationYaw(simulator.getHeading());
    }
    /**
     * Converts chassis speeds to wheel speeds based on kinematics.
     * @param speeds the speeds of the chassis
     * @return the speeds of the wheels
     */
    public DifferentialDriveWheelSpeeds toWheelSpeeds(ChassisSpeeds speeds)
    {
        return kinematics.toWheelSpeeds(speeds);
    }
    /**
     * Scales the speeds from [-1, 1] to [-maxSpeed, maxSpeed]
     * @param speeds the speeds of the wheels
     * @return the speeds of the wheels * maxSpeed
     */
    public DifferentialDriveWheelSpeeds scaleSpeeds(DifferentialDriveWheelSpeeds speeds)
    {
        return new DifferentialDriveWheelSpeeds(
            speeds.leftMetersPerSecond * config.maxSpeed,
            speeds.rightMetersPerSecond * config.maxSpeed
        );
    }
    /**
     * Drives using open loop control.
     * @param speeds the speeds of the drivetrain. Range: [-1, 1]
     */
    public void driveOpenLoop(DifferentialDriveWheelSpeeds speeds)
    {
        leftSide.set(speeds.leftMetersPerSecond);
        rightSide.set(speeds.rightMetersPerSecond);
        robotDrive.feed();
    }
    /**
     * Drives using closed loop control
     * @param speeds the speeds of the drivetrain. Range: [-maxSpeed, maxSpeed]
     * @param pidSlot the pid slot of the velocity closed loop
     */
    public void driveClosedLoop(DifferentialDriveWheelSpeeds speeds, int pidSlot)
    {
        leftSide.setVelocity(pidSlot, speeds.leftMetersPerSecond);
        rightSide.setVelocity(pidSlot, speeds.rightMetersPerSecond);
        robotDrive.feed();
    }
    /**
     * The returns the speeds of the wheels on both side.
     * @return the speeds of the wheels on both sides in m/s.
     */
    public DifferentialDriveWheelSpeeds getWheelSpeeds()
    {
        return new DifferentialDriveWheelSpeeds(leftSide.getSelectedEncoder().getVelocity(),
            rightSide.getSelectedEncoder().getVelocity());
    }
    /**
     * Gets the controller that the left side uses.
     * @return the controller that the left side uses.
     */
    public NFRMotorController getLeftSide()
    {
        return leftSide;
    }
    /**
     * Gets the controller that the right side uses.
     * @return the controller that the right side uses.
     */
    public NFRMotorController getRightSide()
    {
        return rightSide;
    }
    /**
     * Gets the pose estimated by the odometry, not affected by vision measurements.
     * @return odometry.getPoseMeters()
     */
    public Pose2d getOdometryPose()
    {
        return odometry.getPoseMeters();
    }
}