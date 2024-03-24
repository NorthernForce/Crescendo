package frc.robot.constants;

import org.northernforce.subsystems.arm.NFRRotatingArmJoint.NFRRotatingArmJointConfiguration;

import org.northernforce.subsystems.drive.NFRSwerveDrive.NFRSwerveDriveConfiguration;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import frc.robot.subsystems.NFRPhotonCamera.NFRPhotonCameraConfiguration;
import frc.robot.subsystems.Xavier.XavierConfiguration;

public class CrabbyConstants {
    public static final TalonFXConfiguration defaultTalonConfiguration = new TalonFXConfiguration()
        .withCurrentLimits(new CurrentLimitsConfigs()
            .withSupplyCurrentLimit(60)
            .withSupplyCurrentLimitEnable(true)
            .withSupplyCurrentThreshold(60)
            .withSupplyTimeThreshold(0.5)); // TODO: if necessary,
        // add some common configurations to this
    public static class DriveConstants {
        public static final Translation2d[] offsets = new Translation2d[] {
            new Translation2d(0.225425, 0.307975),
            new Translation2d(0.225425, -0.307975),
            new Translation2d(-0.225425, 0.307975),
            new Translation2d(-0.225425, -0.307975)
        };
        public static final NFRSwerveDriveConfiguration config = new NFRSwerveDriveConfiguration("drive");
        public static final PIDController controller = new PIDController(4.2, 0, 0.5);
        public static final HolonomicPathFollowerConfig holonomicConfig = new HolonomicPathFollowerConfig(
            new PIDConstants(5),
            new PIDConstants(2),
            6, offsets[0].getDistance(new Translation2d()), new ReplanningConfig());
        public static final double maxShootSpeed = 0.5;
    }
    public static class IntakeConstants
    {
        public static final double intakeSpeed = -0.7;
        public static final double intakePurgeSpeed = 0.7;
    }
    public static class IndexerConstants
    {
        public static final double indexerSpeed = 0.6;
        public static final double indexerShootSpeed = 0.8;
        public static final double indexerPurgeSpeed = -1; //TODO find indexerPurgeSpeed
    }
    public static class WristConstants
    {
        public static final NFRRotatingArmJointConfiguration wristConfig = 
            new NFRRotatingArmJointConfiguration("wristConfig")
                .withUseLimits(true)
                .withUseIntegratedLimits(true)
                .withLimits(Rotation2d.fromDegrees(22), Rotation2d.fromDegrees(56))
                .withGearRatio(1) // TODO
                .withGearbox(DCMotor.getNEO(1))
                .withLength(1) // TODO
                .withMass(1); // TODO
        public static final Rotation2d ampRotation = Rotation2d.fromDegrees(55);
        public static final Rotation2d closeShotRotation = Rotation2d.fromDegrees(55);
        public static final Rotation2d tolerance = Rotation2d.fromDegrees(1);
        public static final double kP = 2;
        public static final double kI = 0;
        public static final double kD = 0;
        public static final double wristEncoderRatio = 1.0 / 3.0;
        public static final double maxVelocity = 5;
        public static final double maxAccel = 2;
        public static final double allowedClosedLoopError = tolerance.getRotations();
    }
    public static class ShooterConstants
    {
        public static final Slot0Configs topSlot = new Slot0Configs()
                .withKV(0.0102)
                .withKS(0)
                .withKP(0.01)
                .withKI(0)
                .withKD(0);
        public static final Slot0Configs bottomSlot = new Slot0Configs()
                .withKV(0.05)
                .withKS(0)
                .withKP(0.0115)
                .withKI(0)
                .withKD(0);
        public static final TalonFXConfiguration topShooterConfiguration = defaultTalonConfiguration.withSlot0(topSlot)
            .withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Coast));
        public static final TalonFXConfiguration bottomShooterConfiguration = defaultTalonConfiguration.withSlot0(topSlot)
            .withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Coast));
        public static final double closeShotSpeed = 50; // TODO
        public static final double ampBottomSpeed = 14; // TODO
        public static final double ampTopSpeed = 7; // TODO
        public static final double tolerance = 3; // RPS
        public static final double clearanceTime = 0.1; // Time in seconds for shooter to start ramping down after note is passed into shooter
    }
    public static class ClimberConstants
    {
        public static final double climberSpeed = 1;
        public static final double climberReverseSpeed = -1;
        public static final double climberLimit = 480.0; //TODO: Find climber limit
    }
    public static class XavierConstants
    {
        public static final XavierConfiguration config = new XavierConfiguration("xavier", "note_detection");
    }
    public static class OrangePiConstants
    {
        public static final double cameraHeight = Units.inchesToMeters(26);
        public static final Rotation2d cameraPitch = Rotation2d.fromDegrees(22.5);
        public static final NFRPhotonCameraConfiguration config = new NFRPhotonCameraConfiguration("orange pi", "Unnamed", new Transform3d(
            new Translation3d(Units.inchesToMeters(-11.1), 0, cameraHeight),
            new Rotation3d(0, cameraPitch.getRadians(), Math.toRadians(180))
        ));
    }
}
