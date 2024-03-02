package frc.robot.robots;

import org.northernforce.gyros.NFRGyro;
import org.northernforce.motors.NFRSparkMax;
import org.northernforce.motors.NFRTalonFX;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.CANSparkLowLevel.MotorType;

import frc.robot.gyros.NFRPigeon2;
import frc.robot.sensors.NFRBeamBreak;
import frc.robot.subsystems.swerve.SwerveModule;
import frc.robot.utils.SwerveModuleHelpers;

public class CrabbyMap {
    public final SwerveModule[] modules = new SwerveModule[] {
        SwerveModuleHelpers.createMk4iL3("Front Left", 2, 6, 10, false, "drive"),
        SwerveModuleHelpers.createMk4iL3("Front Right", 1, 5, 9, true, "drive"),
        SwerveModuleHelpers.createMk4iL3("Back Left", 4, 8, 12, false, "drive"),
        SwerveModuleHelpers.createMk4iL3("Back Right", 3, 7, 11, true, "drive"),
    };

    public final NFRGyro gyro = new NFRPigeon2(1);
    public final NFRSparkMax intakeMotor = new NFRSparkMax(MotorType.kBrushless, 18);
    public final NFRBeamBreak intakeBeamBreak = new NFRBeamBreak(7);
    {
        intakeMotor.setFollowerOppose(0);
    }

    private final Slot0Configs ShooterSlot0Config = new Slot0Configs()
            .withKV(0.0119)
            .withKS(0.00)
            .withKP(0.02)
            .withKI(0)
            .withKD(0);

    public final NFRTalonFX shooterMotorTop = new NFRTalonFX(new TalonFXConfiguration()
        .withSlot0(ShooterSlot0Config), 22);
    {
        shooterMotorTop.setNeutralMode(NeutralModeValue.Coast);
    }
    public final NFRTalonFX shooterMotorBottom = new NFRTalonFX(new TalonFXConfiguration()
        .withSlot0(ShooterSlot0Config), 23);
    {
        shooterMotorBottom.setNeutralMode(NeutralModeValue.Coast);
    }
}
