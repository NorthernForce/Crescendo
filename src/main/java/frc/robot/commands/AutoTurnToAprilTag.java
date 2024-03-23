package frc.robot.commands;

import java.util.Optional;
import java.util.function.Supplier;

import org.northernforce.subsystems.drive.NFRSwerveDrive;

import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;

public class AutoTurnToAprilTag extends Command {
    protected final NFRSwerveDrive drive;
    protected final Supplier<Optional<Rotation2d>> targetRotation;
    protected final Rotation2d tolerance;
    public AutoTurnToAprilTag(NFRSwerveDrive drive, Supplier<Optional<Rotation2d>> targetRotation, Rotation2d tolerance)
    {
        this.drive = drive;
        this.targetRotation = targetRotation;
        this.tolerance = tolerance;
    }
    @Override
    public void initialize()
    {
        PPHolonomicDriveController.setRotationTargetOverride(() -> {
            var targ = targetRotation.get();
            if (targ.isPresent()) {
                return Optional.of(drive.getRotation().minus(targ.get()));
            }
            else return Optional.empty();
        });
    }
    @Override
    public void end(boolean interrupted)
    {
        PPHolonomicDriveController.setRotationTargetOverride(null);
    }
    @Override
    public boolean isFinished()
    {
        var targ = targetRotation.get();
        return targ.isPresent() ? Math.abs(targ.get().getDegrees()) < tolerance.getDegrees() : false;
    }
}
