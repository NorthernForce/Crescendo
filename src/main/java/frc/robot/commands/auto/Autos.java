package frc.robot.commands.auto;

import java.util.List;
import java.util.function.Supplier;

import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;

import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.utils.AutonomousRoutine;

public class Autos
{
    /**
     * Gets the AutonomousRoutine struct for S1CSV1
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S1CSV1
     */
    public static AutonomousRoutine getS1CSV1(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller)
    {
        return S1CSV1.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red);
    }
    /**
     * Gets the AutonomousRoutine struct for S1LS
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S1LS
     */
    public static AutonomousRoutine getS1LS(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller)
    {
        return S1LS.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red);
    }
    /**
     * Gets the AutonomousRoutine struct for S2CS
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S2CS
     */
    public static AutonomousRoutine getS2CS(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller)
    {
        return S2CS.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red);
    }
    /**
     * Gets the AutonomousRoutine struct for S3SV1G7
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S3SV1G7
     */
    public static AutonomousRoutine getS3SV1G7(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller)
    {
        return S3SV1G7.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red);
    }

    /**
     * Gets the list of autonomous routines.
     * Includes S1.CS.V1
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an list of AutonomousRoutines
     */
    public static List<AutonomousRoutine> getRoutines(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands,
        Supplier<Pose2d> poseSupplier, PPHolonomicDriveController controller)
    {
        return List.of(
            getS1CSV1(drive, setStateCommands, poseSupplier, controller),
            getS1LS(drive, setStateCommands, poseSupplier, controller),
            getS2CS(drive, setStateCommands, poseSupplier, controller),
            getS3SV1G7(drive, setStateCommands, poseSupplier, controller));
    }
}
