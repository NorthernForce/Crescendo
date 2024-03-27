package frc.robot.commands;

import java.util.Optional;
import java.util.function.Supplier;

import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.Command;

public class TurnToTarget2 extends Command
{
    protected final NFRSwerveDrive drive;
    protected final NFRSwerveModuleSetState[] setStateCommands;
    protected final PIDController controller;
    protected final Supplier<Optional<Rotation2d>> targetSupplier;
    protected final boolean optimize;
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
    public TurnToTarget2(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, PIDController controller,
    Supplier<Optional<Rotation2d>> targetSupplier,
        boolean optimize)
    {
        this.drive = drive;
        this.setStateCommands = setStateCommands;
        this.controller = controller;
        this.targetSupplier = targetSupplier;
        this.optimize = optimize;
        controller.enableContinuousInput(-Math.PI, Math.PI);
    }
    @Override
    public void initialize()
    {
        controller.reset();
    }
    @Override
    public void execute()
    {
        System.out.println("TurnTOTarget2");
        var detection = targetSupplier.get();
        if (detection.isPresent())
        {
            controller.setSetpoint(MathUtil.angleModulus(drive.getRotation().minus(detection.get()).getRadians()));
        }
        ChassisSpeeds speeds = new ChassisSpeeds(0, 0, controller.calculate(
            MathUtil.angleModulus(drive.getRotation().getRadians())) );
        if (controller.atSetpoint())
        {
            speeds.omegaRadiansPerSecond = 0;
        }
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
        return Math.abs(targetSupplier.get().orElse(Rotation2d.fromDegrees(100)).getDegrees()) < 5
        && Math.abs(drive.getChassisSpeeds().omegaRadiansPerSecond) < 0.1;
    }
    @Override
    public void end(boolean interrupted)
    {
        SwerveModuleState[] states = drive.toModuleStates(new ChassisSpeeds());
        for (int i = 0; i < states.length; i++) {
            setStateCommands[i].setTargetState(optimize ? SwerveModuleState.optimize(states[i],
                    drive.getModules()[i].getRotation()) : states[i]);
        }
    }
}