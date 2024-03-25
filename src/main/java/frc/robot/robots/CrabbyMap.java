package frc.robot.robots;

import org.northernforce.motors.NFRSparkMax;
import org.northernforce.motors.NFRTalonFX;

import com.revrobotics.REVPhysicsSim;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.system.plant.DCMotor;
import frc.robot.constants.CrabbyConstants;
import frc.robot.gyros.NFRPigeon2;
import frc.robot.logging.ComponentLogger;
import frc.robot.sensors.NFRBeamBreak;
import frc.robot.subsystems.swerve.SwerveModule;
import frc.robot.utils.LoggableHardware;
import frc.robot.utils.SwerveModuleHelpers;

public class CrabbyMap implements LoggableHardware {
    public CrabbyContainer crabiliciouslyCrabtasticalCrabby;
    public CrabbyMap(CrabbyContainer crabby)
    {
        crabiliciouslyCrabtasticalCrabby = crabby;
    }
    public final SwerveModule[] modules = new SwerveModule[] {
        SwerveModuleHelpers.createMk4iL2("Front Left", 1, 5, 9, false, "drive"),
        SwerveModuleHelpers.createMk4iL2("Front Right", 2, 6, 10, true, "drive"),
        SwerveModuleHelpers.createMk4iL2("Back Left", 3, 7, 11, false, "drive"),
        SwerveModuleHelpers.createMk4iL2("Back Right", 4, 8, 12, true, "drive"),
    };

    public final NFRPigeon2 gyro = new NFRPigeon2(1);
    public final NFRSparkMax intakeMotor = new NFRSparkMax(MotorType.kBrushless, 14);
    public final NFRBeamBreak indexerBeamBreak = new NFRBeamBreak(7);
    public final NFRSparkMax indexerMotor = new NFRSparkMax(MotorType.kBrushless, 16); //TODO get the id
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
    public final NFRTalonFX shooterMotorTop = new NFRTalonFX(CrabbyConstants.ShooterConstants.topShooterConfiguration, 22);
    public final NFRTalonFX shooterMotorBottom = new NFRTalonFX(CrabbyConstants.ShooterConstants.bottomShooterConfiguration, 23);
    public final NFRSparkMax wristSparkMax = new NFRSparkMax(MotorType.kBrushless, 17);
    public final ComponentLogger talonComp = new ComponentLogger(shooterMotorBottom, shooterMotorTop);
    {
        REVPhysicsSim.getInstance().addSparkMax(wristSparkMax, DCMotor.getNEO(1));
        wristSparkMax.setSmartCurrentLimit(40);
        wristSparkMax.setIdleMode(IdleMode.kBrake);
    }
    public final NFRSparkMax climberMotor = new NFRSparkMax(MotorType.kBrushless, 15);
    {
        REVPhysicsSim.getInstance().addSparkMax(climberMotor, DCMotor.getNEO(1));
        // climberMotor.restoreFactoryDefaults();
        climberMotor.setSmartCurrentLimit(40);
        climberMotor.setIdleMode(IdleMode.kBrake);
        // climberMotor.burnFlash();
    }

    public final ComponentLogger xboxLogger = new ComponentLogger(crabiliciouslyCrabtasticalCrabby);
    public final ComponentLogger sparkComp = new ComponentLogger(wristSparkMax, climberMotor, indexerMotor, intakeMotor);
    public final ComponentLogger allComps = new ComponentLogger(talonComp, sparkComp, xboxLogger);
    @Override
    public void startLogging(double period) {
        allComps.startLogging(period);
    }
    @Override
    public void logOutputs(String name) {
        allComps.logOutputs(name);
    }
}
