package frc.robot.commands;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;

public class LastStep extends Command
{
    protected final NFRSwerveDrive drive;
    protected final NFRSwerveModuleSetState[] setStateCommands;
    protected final PIDController controller, translational;
    protected final DoubleSupplier ySupplier, yawSupplier;
    protected final Supplier<Rotation2d> thetaSupplier;
    protected final boolean optimize, fieldRelative;
    /**
     * Creates a new TurnToTarget. This allows the robot to be driven in both directions with optional field relative driving, while aligning with
     * a target.
     * @param drive the swerve drive subsystem
     * @param setStateCommands the commands to set the state of each swerve module
     * @param controller the pid controller to calculate closed-loop feedback
     * @param xSupplier the x supplier (field relative optional)
     * @param ySupplier the y supplier (field relative optional)
     * @param thetaSupplier the theta supplier to be used in the absense of a target
     * @param targetSupplier the supplier for a target (ie an apriltag to turn to face)
     * @param optimize whether to optimize each swerve module (cut to the quickest possible state)
     * @param fieldRelative whether the translational control will be relative to the field or the robot
     */
    public LastStep(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, PIDController controller,
         DoubleSupplier ySupplier, Supplier<Rotation2d> thetaSupplier, boolean optimize, boolean fieldRelative,
        DoubleSupplier yawSupplier, PIDController translational)
    {
        this.drive = drive;
        this.setStateCommands = setStateCommands;
        this.controller = controller;
        this.ySupplier = ySupplier;
        this.thetaSupplier = thetaSupplier;
        this.optimize = optimize;
        this.fieldRelative = fieldRelative;
        this.yawSupplier = yawSupplier;
        addRequirements(drive);
        controller.enableContinuousInput(-Math.PI, Math.PI);
        this.translational = translational;
    }
    @Override
    public void initialize()
    {
        controller.reset();
        translational.reset();
        for (var command : setStateCommands)
        {
            command.schedule();
        }
    }
    @Override
    public void execute()
    {
        controller.setSetpoint(thetaSupplier.get().getRadians());
        ChassisSpeeds speeds = new ChassisSpeeds(translational.calculate(DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red ? -yawSupplier.getAsDouble() : yawSupplier.getAsDouble()), ySupplier.getAsDouble(), controller.calculate(
            MathUtil.angleModulus(drive.getRotation().getRadians())));
        if (controller.atSetpoint())
        {
            speeds.omegaRadiansPerSecond = 0;
        }
        if (fieldRelative)
            speeds = ChassisSpeeds.fromFieldRelativeSpeeds(speeds, drive.getRotation());
        SwerveModuleState[] states = drive.toModuleStates(speeds);
        for (int i = 0; i < states.length; i++)
        {
            setStateCommands[i].setTargetState(optimize ? SwerveModuleState.optimize(states[i],
                drive.getModules()[i].getRotation()) : states[i]);
        }
    }
    @Override
    public boolean isFinished()
    {
        return false;
    }
    @Override
    public void end(boolean interrupted)
    {
        for (var command : setStateCommands)
        {
            command.cancel();
        }
    }
}