package frc.robot.commands.auto;

import java.util.function.BooleanSupplier;
import java.util.function.IntFunction;
import java.util.function.Supplier;

import org.northernforce.commands.NFRSwerveDriveStop;
import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;

import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.NFRSwerveDriveFollowPath;
import frc.robot.utils.AutonomousRoutine;

public class S2SV2 extends SequentialCommandGroup
{
    protected static final PathPlannerPath[] paths = new PathPlannerPath[] {
        PathPlannerPath.fromPathFile("S2.S.1.V2G8"),
        PathPlannerPath.fromPathFile("S3.S.2.piece2.V2G8"),
        PathPlannerPath.fromPathFile("S3.S.3.centerlineprep.V2G8"),
        PathPlannerPath.fromPathFile("S3.S.4.centerline.V2G8"),
    };
    /**
     * Creates a new S2SV2
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @param shouldFlipPath whether to flip the routine based on alliance
     */
    public S2SV2(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, BooleanSupplier shouldFlipPath, boolean ignoreCommands)
    {
        IntFunction<NFRSwerveDriveFollowPath> path = idx -> new NFRSwerveDriveFollowPath(
            drive, setStateCommands, paths[0], poseSupplier, controller,
            () -> Rotation2d.fromDegrees(0), 0.1, shouldFlipPath, ignoreCommands);
        addCommands(
            path.apply(0),
            path.apply(1),
            path.apply(2),
            path.apply(3),
            new NFRSwerveDriveStop(drive, setStateCommands, true)
        );
    }
    /**
     * Gets the AutonomousRoutine struct for S2SV2
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @param shouldFlipPath whether to flip the routine based on alliance
     * @return an AutonomousRoutine for S2SV2
     */
    public static AutonomousRoutine getRoutine(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands,
        Supplier<Pose2d> poseSupplier, PPHolonomicDriveController controller, BooleanSupplier shouldFlipPath, boolean ignoreCommands)
    {
        return new AutonomousRoutine(S2SV2.class.getSimpleName(),
            () -> shouldFlipPath.getAsBoolean() ? paths[0].flipPath().getPreviewStartingHolonomicPose() : paths[0].getPreviewStartingHolonomicPose(),
            new S2SV2(drive, setStateCommands, poseSupplier, controller, shouldFlipPath, ignoreCommands));
    }
}
