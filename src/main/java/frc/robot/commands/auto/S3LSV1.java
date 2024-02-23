package frc.robot.commands.auto;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.function.IntFunction;

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

public class S3LSV1 extends SequentialCommandGroup
{
    protected static final PathPlannerPath[] paths = new PathPlannerPath[] {
        PathPlannerPath.fromPathFile("S3.LS.V1.G8"),
        PathPlannerPath.fromPathFile("S3.LS.V1.backout.cut"),
        PathPlannerPath.fromPathFile("S3.LS.V1.G7"),
        PathPlannerPath.fromPathFile("S3.LS.V1.superback.cut"),
        PathPlannerPath.fromPathFile("S3.LS.V1.G6"),
        PathPlannerPath.fromPathFile("S3.LS.V1.guesswhosbackagain")
    };
    /**
     * Creates a new S3LSV1
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @param shouldFlipPath whether to flip the routine based on alliance
     */
    public S3LSV1(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, BooleanSupplier shouldFlipPath, boolean ignoreCommands)
    {
        IntFunction<NFRSwerveDriveFollowPath> path = idx -> new NFRSwerveDriveFollowPath(
            drive, setStateCommands, paths[idx], poseSupplier, controller,
            () -> Rotation2d.fromDegrees(0), 0.1, shouldFlipPath, ignoreCommands);
        
        addCommands(
            path.apply(0),
            path.apply(1),
            path.apply(2),
            path.apply(3),
            path.apply(4),
            path.apply(5),
            path.apply(6),
            new NFRSwerveDriveStop(drive, setStateCommands, true)
        );
    }
    /**
     * Gets the AutonomousRoutine struct for S3LSV1
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @param shouldFlipPath whether to flip the routine based on alliance
     * @return an AutonomousRoutine for S3LSV1
     */
    public static AutonomousRoutine getRoutine(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands,
        Supplier<Pose2d> poseSupplier, PPHolonomicDriveController controller, BooleanSupplier shouldFlipPath, boolean ignoreCommands)
    {
        return new AutonomousRoutine(S3LSV1.class.getSimpleName(),
            () -> shouldFlipPath.getAsBoolean() ? paths[0].flipPath().getPreviewStartingHolonomicPose() : paths[0].getPreviewStartingHolonomicPose(),
            new S3LSV1(drive, setStateCommands, poseSupplier, controller, shouldFlipPath, ignoreCommands));
    }
}
