package frc.robot.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.northernforce.commands.NFRSwerveModuleSetState;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.WristJoint;
import frc.robot.subsystems.OrangePi.TargetCamera;
import frc.robot.utils.AutonomousRoutine;
import frc.robot.utils.TargetingCalculator;

public class Autos
{   
    public static List<String> getAllAutos()
    {
        /*
        autos added here must inherit SequentialCommandGroup and have a getRoutine
        method that returns an AutonomousRoutine (refer to other autos as an example)
        */
        return List.of(
            "S1.CS.V1",
            "S1.CS.V2",
            "S1.LS",
            "S1.L.V1",
            "S1.L.V2",
            "S2.CS",
            "S2.C.V1",
            "S2.C.V2",
            "S2.LS.V1",
            "S2.S.V1",
            "S2.S.V2",
            "S2.T",
            "S3.CS",
            "S3.C.V1",
            "S3.C.V2",
            "S3.LS.V1",
            "S3.S.V1",
            "S3.S.V2"
        );
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
    public static List<AutonomousRoutine> getRoutines(SwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands,
        Supplier<Pose2d> poseSupplier, Consumer<Pose2d> resetPose, HolonomicPathFollowerConfig config, BooleanSupplier shouldFlipPath)
    {
        AutoBuilder.configureHolonomic(poseSupplier, resetPose, drive::getChassisSpeeds, speeds -> drive.drive(speeds, setStateCommands, true, false),
            config, shouldFlipPath, drive);
        ArrayList<AutonomousRoutine> autoRoutines = new ArrayList<>();
        getAllAutos().forEach(name -> autoRoutines.add(new AutonomousRoutine(name, Pose2d::new, new SequentialCommandGroup(
            Commands.runOnce(() -> drive.scheduleCommands(setStateCommands)),
            new PathPlannerAuto(name),
            Commands.runOnce(() -> drive.cancelCommands(setStateCommands))
        ))));
        return autoRoutines;
    }
    /**
     * Gets the list of autonomous routines.
     * Includes S1.CS.V1, S1.CS.V2
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param resetPose the function to reset the pose
     * @param config the configuration of the path following
     * @param shouldFlipPath whether to flip the path (ie. red -> true, blue -> false)
     * @param intake the intake subsystem to be used
     * @param intakeSpeed speed at which to run the intake
     * @param shooter the shooter subsystem
     * @param wrist the wrist subsystem
     * @param camera the camera for detecting apriltags
     * @param controller the pid controller for following the apriltag
     * @param tolerance the tolerance for the rotational aspect of the robot (yaw)
     * @param wristTolerance the tolerance for the wrist when auto shooting
     * @param speedTolerance the tolerance for the shooter speed
     * @param speedCalculator the calculator for the speed based on distance
     * @param wristCalculator the calculator for the wrist angle based on distance
     * @param cameraHeight the height of the camera in meters
     * @param cameraPitch the pitch of the camera
     * @param clearanceTime the time after pushing the note out of the indexer to keep shooting
     * @return an list of AutonomousRoutines
     */
    public static List<AutonomousRoutine> getRoutines(SwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands,
        Supplier<Pose2d> poseSupplier, Consumer<Pose2d> resetPose, HolonomicPathFollowerConfig config, BooleanSupplier shouldFlipPath, Intake intake,
        double intakeSpeed, Shooter shooter, WristJoint wrist, TargetCamera camera, PIDController controller, Rotation2d tolerance, Rotation2d wristTolerance,
        double speedTolerance, TargetingCalculator speedCalculator, TargetingCalculator wristCalculator, double cameraHeight, Rotation2d cameraPitch,
        double clearanceTime)
    {
        NamedCommands.registerCommand("intake", new RunIntake(intake, intakeSpeed));
        NamedCommands.registerCommand("autoShot", new AutoShot(drive, setStateCommands, camera, shooter, intake, wrist, poseSupplier, controller,
            true, false, tolerance, wristCalculator, speedCalculator, cameraHeight, cameraPitch, speedTolerance, intakeSpeed, wristTolerance, clearanceTime));
        NamedCommands.registerCommand("closeShot", new CloseShot(shooter, wrist, intake));
        AutoBuilder.configureHolonomic(poseSupplier, resetPose, drive::getChassisSpeeds, speeds -> drive.drive(speeds, setStateCommands, true, false),
            config, shouldFlipPath, drive);
        ArrayList<AutonomousRoutine> autoRoutines = new ArrayList<>();
        getAllAutos().forEach(name -> autoRoutines.add(new AutonomousRoutine(name, Pose2d::new, new SequentialCommandGroup(
            Commands.runOnce(() -> drive.scheduleCommands(setStateCommands)),
            new PathPlannerAuto(name),
            Commands.runOnce(() -> drive.cancelCommands(setStateCommands))
        ))));
        return autoRoutines;
    }
}