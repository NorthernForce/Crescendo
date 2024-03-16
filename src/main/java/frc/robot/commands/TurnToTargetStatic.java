package frc.robot.commands;

import java.util.Optional;
import java.util.function.Supplier;

import org.northernforce.commands.NFRSwerveModuleSetState;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.OrangePi.TargetDetection;

public class TurnToTargetStatic extends Command
{
    protected final SwerveDrive drive;
    protected final NFRSwerveModuleSetState[] setStateCommands;
    protected final PIDController controller;
    protected final Supplier<Optional<TargetDetection>> targetSupplier;
    protected final boolean optimize;
    protected final Timer cacheTimer;
    protected boolean hasReset = true;
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
    public TurnToTargetStatic(SwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, PIDController controller,
        Supplier<Optional<TargetDetection>> targetSupplier, boolean optimize)
    {
        this.drive = drive;
        this.setStateCommands = setStateCommands;
        this.controller = controller;
        this.targetSupplier = targetSupplier;
        this.optimize = optimize;
        this.cacheTimer = new Timer();
        addRequirements(drive);
        controller.enableContinuousInput(-Math.PI, Math.PI);
    }
    @Override
    public void initialize()
    {
        hasReset = true;
        controller.reset();
        cacheTimer.restart();
    }
    @Override
    public void execute()
    {
        var detection = targetSupplier.get();
        if (detection.isPresent())
        {
            hasReset = false;
            cacheTimer.restart();
            controller.setSetpoint(MathUtil.angleModulus(drive.getRotation().minus(Rotation2d.fromRadians(detection.get().yaw())).getRadians()));
        }
        ChassisSpeeds speeds = new ChassisSpeeds(0, 0, 0);
        if (!hasReset && !cacheTimer.hasElapsed(0.75)) {
            speeds = new ChassisSpeeds(0, 0, controller.calculate(
                MathUtil.angleModulus(drive.getRotation().getRadians())));
            if (controller.atSetpoint())
            {
                speeds.omegaRadiansPerSecond = 0;
            }
        }
        drive.drive(speeds, setStateCommands, optimize, false);
    }
    @Override
    public boolean isFinished()
    {
        return false;
    }
}