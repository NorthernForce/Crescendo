package frc.robot.commands;

import java.util.Optional;
import java.util.function.Supplier;

import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.OrangePi.TargetDetection;

public class TurnToTargetDynamic extends Command
{
    protected final Supplier<Optional<TargetDetection>> targetSupplier;
    protected final Timer cacheTimer;
    protected final SwerveDrive drive;
    protected boolean hasReset = true;
    protected Rotation2d cache = new Rotation2d();
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
    public TurnToTargetDynamic(SwerveDrive drive, Supplier<Optional<TargetDetection>> targetSupplier)
    {
        this.drive = drive;
        this.targetSupplier = targetSupplier;
        this.cacheTimer = new Timer();
    }
    @Override
    public void initialize()
    {
        hasReset = true;
        cacheTimer.restart();
        PPHolonomicDriveController.setRotationTargetOverride(() -> {
            var detection = targetSupplier.get();
            if (detection.isPresent())
            {
                hasReset = false;
                cacheTimer.restart();
                cache = drive.getRotation().minus(Rotation2d.fromRadians(detection.get().yaw()));
            }
            if (!hasReset && !cacheTimer.hasElapsed(0.75))
            {
                return Optional.of(cache);
            }
            return Optional.empty();
        });
    }
    @Override
    public boolean isFinished()
    {
        return false;
    }
    @Override
    public void end(boolean interrupted)
    {
        PPHolonomicDriveController.setRotationTargetOverride(null);
    }
}