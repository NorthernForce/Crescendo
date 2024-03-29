package frc.robot.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import org.northernforce.commands.NFRRotatingArmJointSetAngle;
import org.northernforce.commands.NFRSwerveDriveStop;
import org.northernforce.commands.NFRSwerveModuleSetState;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.constants.CrabbyConstants;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.NFRPhotonCamera;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.subsystems.WristJoint;
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
            "S1.SIMPLE",
            "S2.SIMPLE",
            "S3.SIMPLE",
            // "S1.G1_G2",
            "S2.G2_G1",
            // "S2.G2_G3",
            // "S3.G3_G2",
            // "S1.CS.V1",
            // "S1.CS.V2",
            "S1.CS.V3",
            "S1.CS.LS",
            // "S1.CS.V2",
            // "S1.LS",
            // "S1.L.V1",
            // "S1.L.V2",
            // "S2.CS",
            // "S2.C.V1",
            // "S2.C.V2",
            // "S2.LS.V1",
            // "S2.S.V1",
            // "S2.S.V2",
            "S2.CST",
            // "S2.T",
            "S2.CSC",
            "S3.chaos",
            "S3.CS",
            "S3.BERT"
            // "S3.brokenchaos",
            // "S3.C.V2",
            // "S3.LS.V1",
            // "S3.S.V1",
            // "S3.S.V2"
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
        Shooter shooter, WristJoint wrist, Indexer indexer, NFRPhotonCamera camera, DoubleSupplier lastRecordedDistance, TargetingCalculator topCalculator,
        TargetingCalculator bottomCalculator, TargetingCalculator wristCalculator)
    {
        NamedCommands.registerCommand("intake", new RunIndexerAndIntake(indexer, intake,
        CrabbyConstants.IndexerConstants.indexerSpeed,
        CrabbyConstants.IntakeConstants.intakeSpeed)
        .andThen(new PurgeIndexer(indexer, intake,
                0.7,
                -0.7).withTimeout(0.175)
                .andThen(new RunIndexerAndIntake(indexer, intake,
                        CrabbyConstants.IndexerConstants.indexerSpeed,
                        CrabbyConstants.IntakeConstants.intakeSpeed))));
        NamedCommands.registerCommand("dumbtake", new RunIndexerAndIntake(indexer, intake,
        CrabbyConstants.IndexerConstants.indexerSpeed,
        CrabbyConstants.IntakeConstants.intakeSpeed).withTimeout(6.5));
        NamedCommands.registerCommand("closeShot", new CloseShot(shooter, wrist, indexer, intake));
        NamedCommands.registerCommand("prepAutoShot", new AutoPrepShot(drive, camera, shooter, wrist,
            () -> topCalculator.getValueForDistance(lastRecordedDistance.getAsDouble()),
            () -> bottomCalculator.getValueForDistance(lastRecordedDistance.getAsDouble()),
            () -> Rotation2d.fromRadians(wristCalculator.getValueForDistance(lastRecordedDistance.getAsDouble()))));
        NamedCommands.registerCommand("autoShot", new AutoShot(drive, setStateCommands, shooter, camera, wrist, intake, indexer, () -> topCalculator.getValueForDistance(lastRecordedDistance.getAsDouble()),
            () -> bottomCalculator.getValueForDistance(lastRecordedDistance.getAsDouble()), () -> Rotation2d.fromRadians(wristCalculator.getValueForDistance(lastRecordedDistance.getAsDouble()))));
        NamedCommands.registerCommand("spit", new Spit(shooter, wrist, indexer, intake));
            AutoBuilder.configureHolonomic(poseSupplier, resetPose, drive::getChassisSpeeds, speeds -> drive.drive(speeds, setStateCommands, true, false),
            config, shouldFlipPath, drive);
        NamedCommands.registerCommand("prepareSpit", new NFRRotatingArmJointSetAngle(wrist, Rotation2d.fromDegrees(25), Rotation2d.fromDegrees(5), 0, true));
        ArrayList<AutonomousRoutine> autoRoutines = new ArrayList<>();
        getAllAutos().forEach(name -> autoRoutines.add(new AutonomousRoutine(name, Pose2d::new, new SequentialCommandGroup(
            Commands.runOnce(() -> drive.scheduleCommands(setStateCommands)),
            new PathPlannerAuto(name),
            Commands.runOnce(() -> drive.cancelCommands(setStateCommands)),
            new NFRSwerveDriveStop(drive, setStateCommands, true)
        ))));
        return autoRoutines;
    }
}