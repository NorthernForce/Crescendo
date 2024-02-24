package frc.robot.commands;

import java.util.function.DoubleSupplier;

import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Xavier;

public class FollowNote extends Command {
    private Xavier xavier;
    private NFRSwerveDrive drive;
    private NFRSwerveModuleSetState[] setStateCommands;
    private DoubleSupplier strafeSupplier;
    private PIDController pid;
    private boolean useOptimization;

    /**
     * Creates a new FollowNote command
     * @param xavier the Xavier subsystem
     * @param drive the Drive subsystem
     * @param setStateCommands the array of SetState command you want to use
     * @param strafeSupplier a supplier which returns the direction of strafe (if any)
     * @param useOptimization whether to use swerve optimization (recommended)
     */
    public FollowNote(Xavier xavier, NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, DoubleSupplier strafeSupplier, boolean useOptimization) {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(drive);
        addRequirements(xavier);
        this.xavier = xavier;
        this.drive = drive;
        this.setStateCommands = setStateCommands;
        this.strafeSupplier = strafeSupplier;
        this.pid = new PIDController(.35, 0, 0);
        this.useOptimization = useOptimization;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        for (NFRSwerveModuleSetState command : setStateCommands) {
            command.schedule();
        }
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        ChassisSpeeds speeds = new ChassisSpeeds(0, 0, 0);
        float radian = xavier.getYawRadians();
        if (radian == 0) {
            pid.reset();
        } else {
            speeds = new ChassisSpeeds(.5, strafeSupplier.getAsDouble() * 0.5, pid.calculate(radian));
        }
        speeds = ChassisSpeeds.discretize(speeds, 0.02);
        SwerveModuleState[] states = drive.toModuleStates(speeds);
        for (int i = 0; i < states.length; i++) {
            setStateCommands[i].setTargetState(useOptimization ? SwerveModuleState.optimize(
                states[i], drive.getModules()[i].getRotation()) : states[i]);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        for (NFRSwerveModuleSetState command : setStateCommands) {
            command.cancel();
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}