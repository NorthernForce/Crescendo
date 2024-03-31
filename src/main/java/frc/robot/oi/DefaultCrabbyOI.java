package frc.robot.oi;

import org.northernforce.commands.NFRRotatingArmJointSetAngle;
import org.northernforce.commands.NFRRotatingArmJointWithJoystick;
import org.northernforce.commands.NFRSwerveDriveStop;
import org.northernforce.commands.NFRSwerveDriveWithJoystick;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.CloseShotPreset;
import frc.robot.commands.AlignToAmp;
import frc.robot.commands.NFRWristContinuousAngle;
import frc.robot.commands.PurgeIndexer;
import frc.robot.commands.RampShooterWithDifferential;
import frc.robot.commands.RumbleController;
import frc.robot.commands.RunIndexerAndIntake;
import frc.robot.commands.ShootIndexerAndIntake;
import frc.robot.commands.TurnToTarget;
import frc.robot.constants.CrabbyConstants;
import frc.robot.robots.CrabbyContainer;

public class DefaultCrabbyOI implements CrabbyOI {
    @Override
    public void bindDriverToXBoxController(CrabbyContainer container, CommandXboxController controller)
    {
        container.drive.setDefaultCommand(new NFRSwerveDriveWithJoystick(container.drive, container.setStateCommands,
            () -> -MathUtil.applyDeadband(controller.getLeftY(), 0.1, 1),
            () -> -MathUtil.applyDeadband(controller.getLeftX(), 0.1, 1),
            () -> -MathUtil.applyDeadband(controller.getRightX(), 0.1, 1), true, true));
        
        controller.back().onTrue(Commands.runOnce(container.drive::clearRotation, container.drive));
        
        controller.x().whileTrue(new NFRSwerveDriveStop(container.drive, container.setStateCommands, true));
        
        controller.leftTrigger().whileTrue(new RunIndexerAndIntake(container.indexer, container.intake, CrabbyConstants.IndexerConstants.indexerSpeed,
            CrabbyConstants.IntakeConstants.intakeSpeed).andThen(new PurgeIndexer(container.indexer, container.intake,
                CrabbyConstants.IntakeConstants.intakePurgeSpeed, CrabbyConstants.IndexerConstants.indexerPurgeSpeed).withTimeout(0.20)
                .andThen(new RunIndexerAndIntake(container.indexer, container.intake, CrabbyConstants.IndexerConstants.indexerSpeed,
                    CrabbyConstants.IntakeConstants.intakeSpeed))));
        
        new Trigger(() -> container.indexer.getBeamBreak().beamBroken()).onTrue(new RumbleController(controller.getHID(), 0.5, 0.5));
        
        controller.b().whileTrue(new PurgeIndexer(container.indexer, container.intake, CrabbyConstants.IntakeConstants.intakePurgeSpeed,
            CrabbyConstants.IndexerConstants.indexerPurgeSpeed));
        
        controller.rightBumper().whileTrue(new TurnToTarget(container.drive, container.setStateCommands,
            CrabbyConstants.DriveConstants.controller,
            () -> -MathUtil.applyDeadband(controller.getLeftY(), 0.1, 1),
            () -> -MathUtil.applyDeadband(controller.getLeftX(), 0.1, 1),
            () -> -MathUtil.applyDeadband(controller.getRightX(), 0.1, 1),
            container.orangePi::getSpeakerTagYaw, true, true)
            .alongWith(new RampShooterWithDifferential(container.shooter,
                () -> container.topSpeedCalculator.getValueForDistance(container.lastRecordedDistance),
                () -> container.bottomSpeedCalculator.getValueForDistance(container.lastRecordedDistance)))
            .alongWith(new NFRWristContinuousAngle(container.wristJoint,
                () -> Rotation2d.fromRadians(container.angleCalculator.getValueForDistance(container.lastRecordedDistance)).minus(Rotation2d.fromDegrees(0 * container.lastRecordedDistance)))));
        
        controller.rightTrigger().and(() -> container.shooter.isRunning() && container.shooter.isAtSpeed(CrabbyConstants.ShooterConstants.tolerance))
            .onTrue(new ShootIndexerAndIntake(container.indexer, container.intake, CrabbyConstants.IndexerConstants.indexerShootSpeed, -0.7));
        
        controller.leftBumper().whileTrue(new NFRRotatingArmJointSetAngle(container.wristJoint, CrabbyConstants.WristConstants.ampRotation,
            CrabbyConstants.WristConstants.tolerance, 0, true)
            .alongWith(new RampShooterWithDifferential(container.shooter, () -> CrabbyConstants.ShooterConstants.ampTopSpeed,
                () -> CrabbyConstants.ShooterConstants.ampBottomSpeed))
            .alongWith(new AlignToAmp(container.drive, container.setStateCommandsVelocity, true, () -> container.poseEstimator.getEstimatedPosition()))
        );

        controller.y().whileTrue(new CloseShotPreset(container.shooter, container.wristJoint));
    }
    @Override
    public void bindDriverToJoystick(CrabbyContainer container, CommandGenericHID joystick)
    {
        container.drive.setDefaultCommand(new NFRSwerveDriveWithJoystick(container.drive, container.setStateCommands,
            () -> -MathUtil.applyDeadband(joystick.getRawAxis(1), 0.1, 1),
            () -> -MathUtil.applyDeadband(joystick.getRawAxis(0), 0.1, 1),
            () -> -MathUtil.applyDeadband(joystick.getRawAxis(4), 0.1, 1), true, true));
        
        joystick.button(2).onTrue(Commands.runOnce(container.drive::clearRotation, container.drive));
        
        joystick.button(3).whileTrue(new NFRSwerveDriveStop(container.drive, container.setStateCommands, true));
    }
    @Override
    public void bindManipulatorToXboxController(CrabbyContainer container, CommandXboxController controller)
    {
        NFRRotatingArmJointWithJoystick wristManual = new NFRRotatingArmJointWithJoystick(container.wristJoint,
            () -> -MathUtil.applyDeadband(controller.getLeftY(), 0.1, 1));
        
        container.dashboard.statusLightManager.wristManualLight.setSupplier(() -> wristManual.isScheduled());
        
        controller.leftTrigger()
                .whileTrue(new RunIndexerAndIntake(container.indexer, container.intake,
                        CrabbyConstants.IndexerConstants.indexerSpeed,
                        CrabbyConstants.IntakeConstants.intakeSpeed)
                        .andThen(new PurgeIndexer(container.indexer, container.intake,
                                0.7,
                                -0.7).withTimeout(0.175)
                                .andThen(new RunIndexerAndIntake(container.indexer, container.intake,
                                        CrabbyConstants.IndexerConstants.indexerSpeed,
                                        CrabbyConstants.IntakeConstants.intakeSpeed))));
        
        new Trigger(() -> container.indexer.getBeamBreak().beamBroken())
            .onTrue(new RumbleController(controller.getHID(), 0.5, 0.5));
        
        controller.b().whileTrue(new PurgeIndexer(container.indexer, container.intake, CrabbyConstants.IntakeConstants.intakePurgeSpeed,
            CrabbyConstants.IndexerConstants.indexerPurgeSpeed));
        
        controller.rightTrigger().and(() -> container.shooter.isAtSpeed(CrabbyConstants.ShooterConstants.tolerance))
            .onTrue(new ShootIndexerAndIntake(container.indexer, container.intake, CrabbyConstants.IndexerConstants.indexerShootSpeed, -0.7));

        controller.leftBumper().whileTrue(new NFRRotatingArmJointSetAngle(container.wristJoint, CrabbyConstants.WristConstants.ampRotation,
            CrabbyConstants.WristConstants.tolerance, 0, true)
            .alongWith(new RampShooterWithDifferential(container.shooter, () -> CrabbyConstants.ShooterConstants.ampTopSpeed,
                () -> CrabbyConstants.ShooterConstants.ampBottomSpeed)));

        container.climber.setDefaultCommand(Commands.run(container.climber::stopMotor,
            container.climber));
        
        // controller.povDown().whileTrue(new NFRRotatingArmJointSetAngle(container.wristJoint, Rotation2d.fromDegrees(21), Rotation2d.fromDegrees(2), 0, true)
        //     .alongWith(Commands.run(() -> container.climber.startMotor(-1))));
        controller.povDown().whileTrue(new NFRRotatingArmJointSetAngle(container.wristJoint, Rotation2d.fromDegrees(21), Rotation2d.fromDegrees(2), 0, true)
            .alongWith(Commands.run(() -> container.climber.startMotor(container.climber.isDown() ? 0 : -1))));
        controller.povUp().whileTrue(new NFRRotatingArmJointSetAngle(container.wristJoint, Rotation2d.fromDegrees(21), Rotation2d.fromDegrees(2), 0, true)
            .alongWith(Commands.run(() -> container.climber.startMotor(1))));

        container.wristJoint.setDefaultCommand(new NFRRotatingArmJointWithJoystick(container.wristJoint, () -> MathUtil.applyDeadband(-controller.getLeftY(), 0.1))
                .alongWith(Commands.runOnce(() -> container.manualWrist = true)));
        
        controller.a().whileTrue(new RampShooterWithDifferential(container.shooter,
                () -> container.shooterSpeed.getDouble(30) + container.topRollerChange.getDouble(0),
                () -> container.shooterSpeed.getDouble(30))
                .alongWith(Commands.runOnce(() -> container.manualWrist = false)));
        
        controller.rightBumper().whileTrue(new RampShooterWithDifferential(container.shooter,
                () -> container.topSpeedCalculator.getValueForDistance(container.lastRecordedDistance),
                () -> container.bottomSpeedCalculator.getValueForDistance(container.lastRecordedDistance))
            .alongWith(new NFRWristContinuousAngle(container.wristJoint,
                () -> Rotation2d.fromRadians(container.angleCalculator.getValueForDistance(container.lastRecordedDistance)))));
        
        controller.y().whileTrue(new CloseShotPreset(container.shooter, container.wristJoint));
    }
    @Override
    public void bindManipulatorToJoystick(CrabbyContainer container, CommandGenericHID joystick)
    {

    }
}
