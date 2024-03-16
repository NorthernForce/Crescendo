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

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.WristJoint;
import frc.robot.utils.AutonomousRoutine;

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
            // "S1.CS.V2",
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
     * @param controller the controller for following the path
     * @param intake the intake subsystem to be used
     * @param intakeSpeed speed at which to run the intake
     * @return an list of AutonomousRoutines
     */
    public static List<AutonomousRoutine> getRoutines(SwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands,
        Supplier<Pose2d> poseSupplier, Consumer<Pose2d> resetPose, HolonomicPathFollowerConfig config, BooleanSupplier shouldFlipPath, Intake intake,
        double intakeSpeed, Shooter shooter, WristJoint wrist)
    {
        NamedCommands.registerCommand("intake", new RunIntake(intake, intakeSpeed));
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