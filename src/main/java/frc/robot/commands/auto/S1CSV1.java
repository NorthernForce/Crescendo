package frc.robot.commands.auto;

import java.util.function.Supplier;

import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;

import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.NFRSwerveDriveFollowPath;

public class S1CSV1 extends SequentialCommandGroup
{
    protected final NFRSwerveDrive drive;
    public S1CSV1(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier, PPHolonomicDriveController controller)
    {
        this.drive = drive;
        addCommands(
            new NFRSwerveDriveFollowPath(drive, setStateCommands,
                PathPlannerPath.fromPathFile("S1.CS.V1.1"), poseSupplier, controller,
                () -> Rotation2d.fromDegrees(0), 0.1),
            new NFRSwerveDriveFollowPath(drive, setStateCommands,
                PathPlannerPath.fromPathFile("S1.CS.V1.2"), poseSupplier, controller,
                () -> Rotation2d.fromDegrees(0), 0.1),
            new NFRSwerveDriveFollowPath(drive, setStateCommands,
                PathPlannerPath.fromPathFile("S1.CS.V1.3"), poseSupplier, controller,
                () -> Rotation2d.fromDegrees(0), 0.1),
            new NFRSwerveDriveFollowPath(drive, setStateCommands,
                PathPlannerPath.fromPathFile("S1.CS.V1.4"), poseSupplier, controller,
                () -> Rotation2d.fromDegrees(0), 0.1)
        );
    }
}
