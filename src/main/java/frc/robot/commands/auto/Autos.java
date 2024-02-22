package frc.robot.commands.auto;

import java.util.List;
import java.util.function.Supplier;

import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;

import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.commands.RunFullIntake;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
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
        PPHolonomicDriveController controller, boolean ignoreCommands)
    {
        return S1CSV1.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red, ignoreCommands);
    }

    /**
     * Gets the AutonomousRoutine struct for S1LV1
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S1LV1
     */
    public static AutonomousRoutine getS1LV1(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller)
    {
        return S1LV1.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red);
    }

    /**
     * Gets the AutonomousRoutine struct for S2CV1
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S1LV1
     */
    public static AutonomousRoutine getS2CV1(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller)
    {
        return S2CV1.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red);
    }

    /**
     * Gets the AutonomousRoutine struct for S1LV1
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S1LV1
     */
    public static AutonomousRoutine getS2CV2(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller)
    {
        return S2CV2.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red);
    }

    /**
     * Gets the AutonomousRoutine struct for S1LV2
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S1LV2
     */
    public static AutonomousRoutine getS1LV2(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller)
    {
        return S1LV2.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red);
    }
    public static AutonomousRoutine getS2T(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier, PPHolonomicDriveController controller)
    {
        return S2T.getRoutine(drive, setStateCommands, poseSupplier, controller,
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
            getS1CSV1(drive, setStateCommands, poseSupplier, controller, true), 
            getS1LV1(drive, setStateCommands, poseSupplier, controller), 
            getS1LV2(drive, setStateCommands, poseSupplier, controller),
            getS2CV1(drive, setStateCommands, poseSupplier, controller),
            getS2CV2(drive, setStateCommands, poseSupplier, controller),
            getS2T(drive, setStateCommands, poseSupplier, controller)
        );
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
        Supplier<Pose2d> poseSupplier, PPHolonomicDriveController controller, Intake intake, Indexer indexer, double speed)
    {
        NamedCommands.registerCommand("intake", new RunFullIntake(indexer, intake, speed));
        return List.of(getS1CSV1(drive, setStateCommands, poseSupplier, controller, false));
    }
}
