package frc.robot.commands;

import java.util.Optional;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.OrangePi.TargetDetection;

public class TurnToTarget extends Command
{
    protected final NFRSwerveDrive drive;
    protected final NFRSwerveModuleSetState[] setStateCommands;
    protected final ProfiledPIDController controller;
    protected final DoubleSupplier xSupplier, ySupplier, thetaSupplier;
    protected final Supplier<Optional<TargetDetection>> targetSupplier;
    /**
     * Creates a new TurnToTarget. This allows the robot to be driven in both directions with optional field relative driving, while aligning with
     * a target.
     * @param drive the swerve drive subsystem
     * @param setStateCommands the commands to set the state of each swerve module
     * @param controller the profiled pid controller to calculate closed-loop feedback
     * @param xSupplier the x supplier (field relative optional)
     * @param ySupplier
     * @param thetaSupplier
     * @param targetSupplier
     */
    public TurnToTarget(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, ProfiledPIDController controller,
        DoubleSupplier xSupplier, DoubleSupplier ySupplier, DoubleSupplier thetaSupplier, Supplier<Optional<TargetDetection>> targetSupplier)
    {
        this.drive = drive;
        this.setStateCommands = setStateCommands;
        this.controller = controller;
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
        this.thetaSupplier = thetaSupplier;
        this.targetSupplier = targetSupplier;
        addRequirements(drive);
    }
    @Override
    public void initialize()
    {
        controller.reset(0);
        for (var command : setStateCommands)
        {
            command.initialize();
        }
        controller.setGoal(0);
    }
    @Override
    public void execute()
    {
        var detection = targetSupplier.get();
        if (detection.isPresent())
        {
            ChassisSpeeds speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
                new ChassisSpeeds(xSupplier.getAsDouble(), ySupplier.getAsDouble(), controller.calculate(detection.get().yaw())),
                drive.getRotation()
            );
            SwerveModuleState[] states = drive.toModuleStates(speeds);
            for (int i = 0; i < setStateCommands.length; i++)
            {
                setStateCommands[i].setTargetState(states[i]);
            }
        }
        else
        {
            ChassisSpeeds speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
                new ChassisSpeeds(xSupplier.getAsDouble(), ySupplier.getAsDouble(), thetaSupplier.getAsDouble()),
                drive.getRotation()
            );
            SwerveModuleState[] states = drive.toModuleStates(speeds);
            for (int i = 0; i < setStateCommands.length; i++)
            {
                setStateCommands[i].setTargetState(states[i]);
            }
        }
        for (var command : setStateCommands)
        {
            command.execute();
        }
    }
    @Override
    public boolean isFinished()
    {
        return targetSupplier.get().isPresent() && controller.atGoal();
    }
    @Override
    public void end(boolean interrupted)
    {
        for (var command : setStateCommands)
        {
            command.end(interrupted);
        }
    }
}