package frc.robot.commands;

import java.util.function.DoubleSupplier;

import org.northernforce.subsystems.drive.NFRSwerveDrive;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.SwerveModuleSetState;

public class DriveWithVelocities extends Command {

    private DoubleSupplier Vx;
    private DoubleSupplier Vy;
    private NFRSwerveDrive drive;
    private SwerveModuleSetState[] setStateCommands;
    private boolean useFieldRelative;
    private boolean useOptimization;

    /** Creates a new DriveWithVelocities. 
     * 
    */
    public DriveWithVelocities(NFRSwerveDrive drive, SwerveModuleSetState[] setStateCommands, DoubleSupplier Vx, DoubleSupplier Vy, boolean useFieldRelative, boolean useOptimization) {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(drive);
        this.drive = drive;
        this.setStateCommands = setStateCommands;
        this.useFieldRelative = useFieldRelative;
        this.useOptimization = useOptimization;
        this.Vx = Vx;
        this.Vy = Vy;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        for (SwerveModuleSetState command : setStateCommands)
        {
            command.schedule();
        }
        Shuffleboard.getTab("Debug").addDouble("module 0 velocity", drive.getModules()[0]::getVelocity);
        Shuffleboard.getTab("Debug").addDouble("module 1 velocity", drive.getModules()[1]::getVelocity);
        Shuffleboard.getTab("Debug").addDouble("module 2 velocity", drive.getModules()[2]::getVelocity);
        Shuffleboard.getTab("Debug").addDouble("module 3 velocity", drive.getModules()[3]::getVelocity);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        ChassisSpeeds speeds = new ChassisSpeeds(Vx.getAsDouble(), Vy.getAsDouble(), 0);
        if (useFieldRelative)
            speeds = ChassisSpeeds.fromFieldRelativeSpeeds(speeds, drive.getRotation());
        speeds = ChassisSpeeds.discretize(speeds, 0.02);
        SwerveModuleState[] states = drive.toModuleStates(speeds);
        for (int i = 0; i < states.length; i++)
        {
            setStateCommands[i].setTargetState(useOptimization ? SwerveModuleState.optimize(states[i],
                drive.getModules()[i].getRotation()) : states[i], true);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        for (SwerveModuleSetState command : setStateCommands)
        {
            command.cancel();
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
