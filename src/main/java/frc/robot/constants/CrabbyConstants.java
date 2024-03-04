package frc.robot.constants;

import org.northernforce.subsystems.arm.NFRRotatingArmJoint.NFRRotatingArmJointConfiguration;

import org.northernforce.subsystems.drive.NFRSwerveDrive.NFRSwerveDriveConfiguration;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import frc.robot.subsystems.OrangePi.OrangePiConfiguration;
import frc.robot.subsystems.Xavier.XavierConfiguration;

public class CrabbyConstants {
    public static final TalonFXConfiguration defaultTalonConfiguration = new TalonFXConfiguration(); // TODO: if necessary,
        // add some common configurations to this
    public static class DriveConstants {
        public static final Translation2d[] offsets = new Translation2d[] {
            new Translation2d(0.225425, 0.307975),
            new Translation2d(0.225425, -0.307975),
            new Translation2d(-0.225425, 0.307975),
            new Translation2d(-0.225425, -0.307975)
        };
        public static final NFRSwerveDriveConfiguration config = new NFRSwerveDriveConfiguration("drive");
        public static final PIDController turnToTargetController = new PIDController(1, 0, 0);
    }
    public static class IntakeConstants
    {
        public static final double intakeSpeed = -1;
        public static final double intakePurgeSpeed = 1;
    }
    public static class WristConstants
    {
        public static final NFRRotatingArmJointConfiguration wristConfig = 
            new NFRRotatingArmJointConfiguration("wristConfig")
                .withUseLimits(true)
                .withUseIntegratedLimits(true)
                .withLimits(Rotation2d.fromDegrees(22), Rotation2d.fromDegrees(56));
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
        public static final double kV = 0.0119; // Multiplied by setpoint (velocity)
        public static final double kS = 0.0; // Added to setpoint (constant)
        public static final double kP = 0.02; // Multiplied by error (velocity)
        public static final double kI = 0.0; // Accumulation of error
        public static final double kD = 0.0; // Accumulation of rate of change 
        public static final Slot0Configs slot0Config = new Slot0Configs()
                .withKV(kV)
                .withKS(kS)
                .withKP(kP)
                .withKI(kI)
                .withKD(kD);
        public static final TalonFXConfiguration shooterMotorConfiguration = defaultTalonConfiguration.withSlot0(slot0Config)
            .withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Coast));
        public static final double closeShotSpeed = 30;
        public static final double ampBottomSpeed = 15;
        public static final double ampTopSpeed = 9;
        public static final double tolerance = 1; // RPS
        public static final double clearanceTime = 0.1; // Time in seconds for shooter to start ramping down after note is passed into shooter
    }
    public static class XavierConstants
    {
        public static final XavierConfiguration config = new XavierConfiguration("xavier", "note_detection");
    }
    public static class OrangePiConstants
    {
        public static final OrangePiConfiguration config = new OrangePiConfiguration("orange pi", "xavier");
        public static final double cameraHeight = Units.inchesToMeters(26);
        public static final Rotation2d cameraPitch = Rotation2d.fromDegrees(25);
    }
}
