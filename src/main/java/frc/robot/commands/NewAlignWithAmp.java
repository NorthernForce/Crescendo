package frc.robot.commands;

import java.util.Optional;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class NewAlignWithAmp extends SequentialCommandGroup {
    public NewAlignWithAmp(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, PIDController controller,
        DoubleSupplier xSupplier, DoubleSupplier ySupplier, Supplier<Rotation2d> thetaSupplier, boolean optimize, boolean fieldRelative,
        Supplier<Optional<Double>> yawSupplier, PIDController translational) {
        addCommands(
            new SideDrive(drive, setStateCommands, controller, xSupplier, ySupplier, thetaSupplier, optimize, fieldRelative,
                yawSupplier, translational)
        );
    }
}
