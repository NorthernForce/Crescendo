package frc.robot.robots;

import com.revrobotics.REVPhysicsSim;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.system.plant.DCMotor;
import frc.robot.constants.CrabbyConstants;
import frc.robot.gyros.NFRPigeon2;
import frc.robot.motors.LoggableSparkMax;
import frc.robot.motors.LoggableTalonFX;
import frc.robot.sensors.NFRBeamBreak;
import frc.robot.subsystems.swerve.SwerveModule;
import frc.robot.utils.LoggableHardware;
import frc.robot.utils.SwerveModuleHelpers;

public class CrabbyMap implements LoggableHardware {
    public final SwerveModule[] modules = new SwerveModule[] {
        SwerveModuleHelpers.createMk4iL2("Front Left", 1, 5, 9, false, "drive"),
        SwerveModuleHelpers.createMk4iL2("Front Right", 2, 6, 10, true, "drive"),
        SwerveModuleHelpers.createMk4iL2("Back Left", 3, 7, 11, false, "drive"),
        SwerveModuleHelpers.createMk4iL2("Back Right", 4, 8, 12, true, "drive"),
    };

    public final NFRPigeon2 gyro = new NFRPigeon2(1);
    public final LoggableSparkMax intakeMotor = new LoggableSparkMax(MotorType.kBrushless, 14);
    public final NFRBeamBreak indexerBeamBreak = new NFRBeamBreak(7);
    public final LoggableSparkMax indexerMotor = new LoggableSparkMax(MotorType.kBrushless, 16); //TODO get the id
    {
        REVPhysicsSim.getInstance().addSparkMax(indexerMotor, DCMotor.getNEO(1));
        // indexerMotor.restoreFactoryDefaults();
        indexerMotor.setSmartCurrentLimit(60);
        indexerMotor.setIdleMode(IdleMode.kBrake);
        // indexerMotor.burnFlash();
    }
    {
        REVPhysicsSim.getInstance().addSparkMax(intakeMotor, DCMotor.getNEO(1));
        // intakeMotor.restoreFactoryDefaults();
        intakeMotor.setSmartCurrentLimit(60);
        intakeMotor.setIdleMode(IdleMode.kBrake);
        // intakeMotor.burnFlash();
    }
    public final LoggableTalonFX shooterMotorTop = new LoggableTalonFX(CrabbyConstants.ShooterConstants.topShooterConfiguration, 22);
    public final LoggableTalonFX shooterMotorBottom = new LoggableTalonFX(CrabbyConstants.ShooterConstants.bottomShooterConfiguration, 23);
    public final LoggableSparkMax wristSparkMax = new LoggableSparkMax(MotorType.kBrushless, 17);
    {
        REVPhysicsSim.getInstance().addSparkMax(wristSparkMax, DCMotor.getNEO(1));
        wristSparkMax.setSmartCurrentLimit(40);
        wristSparkMax.setIdleMode(IdleMode.kBrake);
    }
    public final LoggableSparkMax climberMotor = new LoggableSparkMax(MotorType.kBrushless, 15);
    {
        REVPhysicsSim.getInstance().addSparkMax(climberMotor, DCMotor.getNEO(1));
        // climberMotor.restoreFactoryDefaults();
        climberMotor.setSmartCurrentLimit(40);
        climberMotor.setIdleMode(IdleMode.kBrake);
        // climberMotor.burnFlash();
    }
    @Override
    public void startLogging(double period) {
        shooterMotorBottom.startLogging(period);
        shooterMotorBottom.startLogging(period);
    }
    @Override
    public void logOutputs(String name) {
        climberMotor.logOutputs(name + "/ClimberMotor");
        indexerMotor.logOutputs(name + "/IndexerMotor");
        intakeMotor.logOutputs(name + "/IntakeMotor");
        shooterMotorTop.logOutputs(name + "/TopShooterMotor");
        shooterMotorBottom.logOutputs(name + "/BottomShooterMotor");
        wristSparkMax.logOutputs(name + "/WristMotor");
    }
}
