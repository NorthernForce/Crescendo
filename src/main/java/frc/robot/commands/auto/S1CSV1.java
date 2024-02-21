package frc.robot.commands.auto;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;

import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.NFRSwerveDriveFollowPath;
import frc.robot.utils.AutonomousRoutine;

public class S1CSV1 extends SequentialCommandGroup
{
    protected static final PathPlannerPath[] paths = new PathPlannerPath[] {
        PathPlannerPath.fromPathFile("S1.CS.V1.1"),
        PathPlannerPath.fromPathFile("S1.CS.V1.2"),
        PathPlannerPath.fromPathFile("S1.CS.V1.3"),
        PathPlannerPath.fromPathFile("S1.CS.V1.4")
    };
    /**
     * Creates a new S1CSV1
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @param shouldFlipPath whether to flip the routine based on alliance
     */
    public S1CSV1(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, BooleanSupplier shouldFlipPath)
    {
        addCommands(
            new NFRSwerveDriveFollowPath(drive, setStateCommands, paths[0], poseSupplier, controller,
                () -> Rotation2d.fromDegrees(0), 0.1, shouldFlipPath),
            new NFRSwerveDriveFollowPath(drive, setStateCommands, paths[1], poseSupplier, controller,
                () -> Rotation2d.fromDegrees(0), 0.1, shouldFlipPath),
            new NFRSwerveDriveFollowPath(drive, setStateCommands, paths[2], poseSupplier, controller,
                () -> Rotation2d.fromDegrees(0), 0.1, shouldFlipPath),
            new NFRSwerveDriveFollowPath(drive, setStateCommands, paths[3], poseSupplier, controller,
                () -> Rotation2d.fromDegrees(0), 0.1, shouldFlipPath)
        );
    }
    /**
     * Gets the AutonomousRoutine struct for S1CSV1
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @param shouldFlipPath whether to flip the routine based on alliance
     * @return an AutonomousRoutine for S1CSV1
     */
    public static AutonomousRoutine getRoutine(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands,
        Supplier<Pose2d> poseSupplier, PPHolonomicDriveController controller, BooleanSupplier shouldFlipPath)
    {
        return new AutonomousRoutine(S1CSV1.class.getSimpleName(),
            () -> shouldFlipPath.getAsBoolean() ? paths[0].flipPath().getPreviewStartingHolonomicPose() : paths[0].getPreviewStartingHolonomicPose(),
            new S1CSV1(drive, setStateCommands, poseSupplier, controller, shouldFlipPath));
    }
}
