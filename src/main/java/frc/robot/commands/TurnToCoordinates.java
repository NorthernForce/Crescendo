package frc.robot.commands;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.Command;

public class TurnToCoordinates extends Command
{
    protected final NFRSwerveDrive drive;
    protected final NFRSwerveModuleSetState[] setStateCommands;
    protected final Supplier<Pose2d> poseSupplier;
    protected final Supplier<Translation2d> targetCoordinates;
    protected final PIDController controller;
    protected final DoubleSupplier xSupplier, ySupplier;
    protected final boolean fieldRelative, optimize;
    public TurnToCoordinates(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        Supplier<Translation2d> targetCoordinates, PIDController controller, boolean optimize, boolean fieldRelative,
        DoubleSupplier xSupplier, DoubleSupplier ySupplier)
    {
        addRequirements(drive);
        this.drive = drive;
        this.setStateCommands = setStateCommands;
        this.poseSupplier = poseSupplier;
        this.targetCoordinates = targetCoordinates;
        this.fieldRelative = fieldRelative;
        this.optimize = optimize;
        this.controller = controller;
        this.xSupplier = xSupplier;
        this.ySupplier = ySupplier;
    }
    public TurnToCoordinates(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier, Supplier<Translation2d>
        targetCoordinates, PIDController controller, boolean optimize, boolean fieldRelative)
    {
        this(drive, setStateCommands, poseSupplier, targetCoordinates, controller, optimize, fieldRelative, () -> 0, () -> 0);
    }
    @Override
    public void initialize()
    {
        controller.reset();
        for (var setStateCommand : setStateCommands)
        {
            setStateCommand.schedule();
        }
    }
    @Override
    public void execute()
    {
        ChassisSpeeds speeds = new ChassisSpeeds(xSupplier.getAsDouble(), ySupplier.getAsDouble(),
            controller.calculate(drive.getRotation().getRadians(),
                targetCoordinates.get().minus(poseSupplier.get().getTranslation()).getAngle().getRadians()));
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
    public void end(boolean interrupted)
    {
        for (var setStateCommand : setStateCommands)
        {
            setStateCommand.cancel();
        }
    }
    @Override
    public boolean isFinished()
    {
        return controller.atSetpoint();
    }
}
