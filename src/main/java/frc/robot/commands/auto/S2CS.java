package frc.robot.commands.auto;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import org.northernforce.commands.NFRSwerveDriveStop;
import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;

import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.CloseShot;
import frc.robot.commands.NFRSwerveDriveFollowPath;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.WristJoint;
import frc.robot.utils.AutonomousRoutine;

public class S2CS extends SequentialCommandGroup
{
    protected static final PathPlannerPath[] paths = new PathPlannerPath[] {
        PathPlannerPath.fromPathFile("S2.CS.G3"),
        PathPlannerPath.fromPathFile("S2.CS.G2"),
        PathPlannerPath.fromPathFile("S2.CS.G6"),
        PathPlannerPath.fromPathFile("S2.CS.return"),
        PathPlannerPath.fromPathFile("S2.CS.attempt")
    };
    /**
     * Creates a new S2CS
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @param shouldFlipPath whether to flip the routine based on alliance
     */
    public S2CS(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, BooleanSupplier shouldFlipPaths)
    {
        addCommands(
            new NFRSwerveDriveFollowPath(drive, setStateCommands, paths[0], poseSupplier, controller,
                () -> Rotation2d.fromDegrees(0), 0.1, shouldFlipPaths, true),
            new NFRSwerveDriveFollowPath(drive, setStateCommands, paths[1], poseSupplier, controller,
                () -> Rotation2d.fromDegrees(0), 0.1, shouldFlipPaths, true),
            new NFRSwerveDriveFollowPath(drive, setStateCommands, paths[2], poseSupplier, controller,
                () -> Rotation2d.fromDegrees(0), 0.1, shouldFlipPaths, true),
            new NFRSwerveDriveFollowPath(drive, setStateCommands, paths[3], poseSupplier, controller,
                () -> Rotation2d.fromDegrees(0), 0.1, shouldFlipPaths, true),
            new NFRSwerveDriveFollowPath(drive, setStateCommands, paths[4], poseSupplier, controller,
                () -> Rotation2d.fromDegrees(0), 0.1, shouldFlipPaths, true),
            new NFRSwerveDriveStop(drive, setStateCommands, true)
        );
    }
    /**
     * Creates a new S1LV1
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @param shouldFlipPath whether to flip the routine based on alliance
     * @param shooter the shooter subsystem
     * @param wristJoint the wrist subsystem
     * @param intake the intake subsystem
     */
    public S2CS(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, BooleanSupplier shouldFlipPath, Shooter shooter, WristJoint wristJoint, Indexer intake)
    {
        addCommands(
            new CloseShot(shooter, wristJoint, intake),
            new NFRSwerveDriveFollowPath(drive, setStateCommands, paths[0], poseSupplier, controller,
                () -> Rotation2d.fromDegrees(0), 0.1, shouldFlipPath, false),
            new NFRSwerveDriveFollowPath(drive, setStateCommands, paths[1], poseSupplier, controller,
                () -> Rotation2d.fromDegrees(0), 0.1, shouldFlipPath, false),
            new NFRSwerveDriveFollowPath(drive, setStateCommands, paths[2], poseSupplier, controller,
                () -> Rotation2d.fromDegrees(0), 0.1, shouldFlipPath, false),
            new NFRSwerveDriveFollowPath(drive, setStateCommands, paths[3], poseSupplier, controller,
                () -> Rotation2d.fromDegrees(0), 0.1, shouldFlipPath, false),
            new NFRSwerveDriveFollowPath(drive, setStateCommands, paths[4], poseSupplier, controller,
                () -> Rotation2d.fromDegrees(0), 0.1, shouldFlipPath, false),
            new NFRSwerveDriveStop(drive, setStateCommands, true)
        );
    }
    /**
     * Gets the AutonomousRoutine struct for S2CS
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @param shouldFlipPath whether to flip the routine based on alliance
     * @return an AutonomousRoutine for S2CS
     */
    public static AutonomousRoutine getRoutine(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands,
        Supplier<Pose2d> poseSupplier, PPHolonomicDriveController controller, BooleanSupplier shouldFlipPath)
    {
        return new AutonomousRoutine(S2CS.class.getSimpleName(),
            () -> shouldFlipPath.getAsBoolean() ? paths[0].flipPath().getPreviewStartingHolonomicPose() : paths[0].getPreviewStartingHolonomicPose(),
            new S2CS(drive, setStateCommands, poseSupplier, controller, shouldFlipPath));
    }
    /**
     * Gets the AutonomousRoutine struct for S2CS
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @param shouldFlipPath whether to flip the routine based on alliance
     * @param shooter the robot's shooter
     * @param wrist the robot's wrist
     * @param intake the robot's intake
     * @return an AutonomousRoutine for S2CS
     */
    public static AutonomousRoutine getRoutine(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands,
        Supplier<Pose2d> poseSupplier, PPHolonomicDriveController controller, BooleanSupplier shouldFlipPath, Shooter shooter,
        WristJoint wrist, Indexer intake)
    {
        return new AutonomousRoutine(S2CS.class.getSimpleName(),
            () -> shouldFlipPath.getAsBoolean() ? paths[0].flipPath().getPreviewStartingHolonomicPose() : paths[0].getPreviewStartingHolonomicPose(),
            new S2CS(drive, setStateCommands, poseSupplier, controller, shouldFlipPath, shooter, wrist, intake));
    }
}
