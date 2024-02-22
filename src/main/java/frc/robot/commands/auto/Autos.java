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
        PPHolonomicDriveController controller, boolean ignoreCommands)
    {
        return S1LV1.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red, ignoreCommands);
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
        PPHolonomicDriveController controller, boolean ignoreCommands)
    {
        return S2CV1.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red, ignoreCommands);
    }

    /**
     * Gets the AutonomousRoutine struct for S2CV2
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S2CV2
     */
    public static AutonomousRoutine getS2CV2(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, boolean ignoreCommands)
    {
        return S2CV2.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red, ignoreCommands);
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
        PPHolonomicDriveController controller, boolean ignoreCommands)
    {
        return S1LV2.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red, ignoreCommands);
    }
    public static AutonomousRoutine getS2T(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, boolean ignoreCommands)
    {
        return S2T.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red, ignoreCommands);
    }
    /**
     * Gets the AutonomousRoutine struct for S1CSV2
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S1CSV2
     */
    public static AutonomousRoutine getS1CSV2(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, boolean ignoreCommands)
    {
        return S1CSV2.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red, ignoreCommands);
    }
    /**
     * Gets the AutonomousRoutine struct for S2LSV1
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S2LSV1
     */
    public static AutonomousRoutine getS2LSV1(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, boolean ignoreCommands)
    {
        return S2LSV1.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red, ignoreCommands);
    }
    /**
     * Gets the AutonomousRoutine struct for S3LSV1
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S3LSV1
     */
    public static AutonomousRoutine getS3LSV1(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, boolean ignoreCommands)
    {
        return S3LSV1.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red, ignoreCommands);
    }
    /* 
     * Gets the AutonomousRoutine struct for S1LS
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S1LS
     */
    public static AutonomousRoutine getS1LS(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, boolean ignoreCommands)
    {
        return S1LS.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red, ignoreCommands);
    }
    /**
     * Gets the AutonomousRoutine struct for S3CV1
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S3CV1
     */
    public static AutonomousRoutine getS3CV1(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, boolean ignoreCommands)
    {
        return S3CV1.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red, ignoreCommands);
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
        PPHolonomicDriveController controller, boolean ignoreCommands)
    {
        return S2CS.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red, ignoreCommands);
    }
    /**
     * Gets the AutonomousRoutine struct for S2SV1
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S2SV1
     */
    public static AutonomousRoutine getS2SV1(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, boolean ignoreCommands)
    {
        return S2SV1.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red, ignoreCommands);
    }
        /**
     * Gets the AutonomousRoutine struct for S2SV2
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S2SV2
     */
    public static AutonomousRoutine getS2SV2(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, boolean ignoreCommands)
    {
        return S2SV2.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red, ignoreCommands);
    }
    /**
     * Gets the AutonomousRoutine struct for S3CS
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S3CS
     */
    public static AutonomousRoutine getS3CS(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, boolean ignoreCommands)
    {
        return S3CS.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red, ignoreCommands);
    }
    /**
     * Gets the AutonomousRoutine struct for S3SV1(G7)
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S3SV1G7
     */
    public static AutonomousRoutine getS3SV1(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, boolean ignoreCommands)
    {
        return S3SV1.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red, ignoreCommands);
    }
    /**
     * Gets the AutonomousRoutine struct for S3SV2(G8)
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S3SV2G8
     */
    public static AutonomousRoutine getS3SV2(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, boolean ignoreCommands)
    {
        return S3SV2.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red, ignoreCommands);
    }

    /**
     * @return an AutonomousRoutine for S3CV2
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an AutonomousRoutine for S3CV2
     */
    public static AutonomousRoutine getS3CV2(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands, Supplier<Pose2d> poseSupplier,
        PPHolonomicDriveController controller, boolean ignoreCommands)
    {
        return S3CV2.getRoutine(drive, setStateCommands, poseSupplier, controller,
            () -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red, ignoreCommands);
    }
    /**
     * Gets the list of autonomous routines.
     * Includes S1.CS.V1, S1.CS.V2
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
            getS1CSV2(drive, setStateCommands, poseSupplier, controller, true),
            getS1LS(drive, setStateCommands, poseSupplier, controller, true),
            getS1LV1(drive, setStateCommands, poseSupplier, controller, true), 
            getS1LV2(drive, setStateCommands, poseSupplier, controller, true),
            getS2CS(drive, setStateCommands, poseSupplier, controller, true),
            getS2CV1(drive, setStateCommands, poseSupplier, controller, true),
            getS2CV2(drive, setStateCommands, poseSupplier, controller, true),
            getS2LSV1(drive, setStateCommands, poseSupplier, controller, true),
            getS3LSV1(drive, setStateCommands, poseSupplier, controller, true),
            getS2SV1(drive, setStateCommands, poseSupplier, controller, true),
            getS2SV2(drive, setStateCommands, poseSupplier, controller, true),
            getS2T(drive, setStateCommands, poseSupplier, controller, true),
            getS3CS(drive, setStateCommands, poseSupplier, controller, true),
            getS3SV1(drive, setStateCommands, poseSupplier, controller, true),
            getS3SV2(drive, setStateCommands, poseSupplier, controller, true),
            getS3CV1(drive, setStateCommands, poseSupplier, controller, true),
            getS3CV2(drive, setStateCommands, poseSupplier, controller, true)
        );
    }
    /**
     * Gets the list of autonomous routines.
     * Includes S1.CS.V1, S1.CS.V2
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @param intake the intake subsystem to be used
     * @param indexer the indexer subsystem to be used
     * @param intakeSpeed speed at which to run the intake
     * @param indexerSpeed speed at which to run the indexer
     * @return an list of AutonomousRoutines
     */
    public static List<AutonomousRoutine> getRoutines(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands,
        Supplier<Pose2d> poseSupplier, PPHolonomicDriveController controller, Intake intake, Indexer indexer, double intakeSpeed, double indexerSpeed)
    {
        NamedCommands.registerCommand("intake", new RunFullIntake(indexer, intake, intakeSpeed, indexerSpeed));
        return List.of(
            getS1CSV1(drive, setStateCommands, poseSupplier, controller, false),
            getS1CSV2(drive, setStateCommands, poseSupplier, controller, false),
            getS1LS(drive, setStateCommands, poseSupplier, controller, false),
            getS1LV1(drive, setStateCommands, poseSupplier, controller, false), 
            getS1LV2(drive, setStateCommands, poseSupplier, controller, false),
            getS2CS(drive, setStateCommands, poseSupplier, controller, false),
            getS2CV1(drive, setStateCommands, poseSupplier, controller, false),
            getS2CV2(drive, setStateCommands, poseSupplier, controller, false),
            getS2T(drive, setStateCommands, poseSupplier, controller, false),
            getS2LSV1(drive, setStateCommands, poseSupplier, controller, false),
            getS3LSV1(drive, setStateCommands, poseSupplier, controller, false),
            getS2SV1(drive, setStateCommands, poseSupplier, controller, false),
            getS2SV2(drive, setStateCommands, poseSupplier, controller, false),
            getS3CS(drive, setStateCommands, poseSupplier, controller, false),
            getS3SV1(drive, setStateCommands, poseSupplier, controller, false),
            getS3SV2(drive, setStateCommands, poseSupplier, controller, false),
            getS3CV1(drive, setStateCommands, poseSupplier, controller, false),
            getS3CV2(drive, setStateCommands, poseSupplier, controller, false),
            getS2T(drive, setStateCommands, poseSupplier, controller, false)
        );
    }
}
