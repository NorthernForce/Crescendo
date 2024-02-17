package frc.robot.commands;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.Command;

public class DriveWithVelocities extends Command {

    private DoubleSupplier Vx;
    private DoubleSupplier Vy;
    private NFRSwerveDrive drive;
    private NFRSwerveModuleSetState[] setStateCommands;
    private boolean useFieldRelative;
    private boolean useOptimization;

    /** Creates a new DriveWithVelocities. 
     * 
    */
    public DriveWithVelocities(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, DoubleSupplier Vx, DoubleSupplier Vy, boolean useFieldRelative, boolean useOptimization) {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(drive);
        this.drive = drive;
        this.setStateCommands = setStateCommands;
        this.useFieldRelative = useFieldRelative;
        this.useOptimization = useOptimization;
        this.Vx = Vx;
        this.Vy = Vy;
    }

     public DriveWithVelocities(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, DoubleSupplier velocity, Supplier<Rotation2d> angle, boolean useFieldRelative, boolean useOptimization) {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(drive);
        this.drive = drive;
        this.setStateCommands = setStateCommands;
        this.useFieldRelative = useFieldRelative;
        this.useOptimization = useOptimization;
        this.Vx = () -> velocity.getAsDouble() * Math.cos(angle.get().getRadians());
        this.Vy = () -> velocity.getAsDouble() * Math.sin(angle.get().getRadians());
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
        ChassisSpeeds speeds = new ChassisSpeeds(Vx.getAsDouble(), Vy.getAsDouble(), 0);
        if (useFieldRelative)
            speeds = ChassisSpeeds.fromFieldRelativeSpeeds(speeds, drive.getRotation());
        speeds = ChassisSpeeds.discretize(speeds, 0.02);
        SwerveModuleState[] states = drive.toModuleStates(speeds);
        for (int i = 0; i < states.length; i++) {
            setStateCommands[i].setTargetState(useOptimization ? SwerveModuleState.optimize(states[i],
                drive.getModules()[i].getRotation()) : states[i]);
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
