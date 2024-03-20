package frc.robot.oi;

import org.northernforce.commands.NFRRotatingArmJointSetAngle;
import org.northernforce.commands.NFRRotatingArmJointWithJoystick;
import org.northernforce.commands.NFRSwerveDriveStop;
import org.northernforce.commands.NFRSwerveDriveWithJoystick;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandGenericHID;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.PurgeIndexer;
import frc.robot.commands.RampShooterContinuous;
import frc.robot.commands.RampShooterWithDifferential;
import frc.robot.commands.RumbleController;
import frc.robot.commands.RunIndexerAndIntake;
import frc.robot.commands.ShootIndexerAndIntake;
import frc.robot.constants.CrabbyConstants;
import frc.robot.robots.CrabbyContainer;

public class DefaultCrabbyOI implements CrabbyOI {
    // private CommandXboxController driverController = null;
    @Override
    public void bindDriverToXBoxController(CrabbyContainer container, CommandXboxController controller)
    {
        // this.driverController = controller;

        container.drive.setDefaultCommand(new NFRSwerveDriveWithJoystick(container.drive, container.setStateCommands,
            () -> -MathUtil.applyDeadband(controller.getLeftY(), 0.1, 1),
            () -> -MathUtil.applyDeadband(controller.getLeftX(), 0.1, 1),
            () -> -MathUtil.applyDeadband(controller.getRightX(), 0.1, 1), true, true));
        
        controller.back().onTrue(Commands.runOnce(container.drive::clearRotation, container.drive));
        
        controller.x().whileTrue(new NFRSwerveDriveStop(container.drive, container.setStateCommands, true));

        // controller.a()
        //     .whileTrue(new RunIndexerAndIntake(container.indexer, container.intake, CrabbyConstants.IndexerConstants.indexerSpeed,
        //         CrabbyConstants.IntakeConstants.intakeSpeed)
        //     .deadlineWith(new FollowNote(container.xavier, container.drive, container.setStateCommands,
        //         () -> -MathUtil.applyDeadband(controller.getLeftX(), 0.1, 1), true)));
        
        controller.leftTrigger().whileTrue(new RunIndexerAndIntake(container.indexer, container.intake, CrabbyConstants.IndexerConstants.indexerSpeed,
            CrabbyConstants.IntakeConstants.intakeSpeed));
        
        new Trigger(() -> container.indexer.getBeamBreak().beamBroken()).onTrue(new RumbleController(controller.getHID(), 0.5, 0.5));
        
        controller.b().whileTrue(new PurgeIndexer(container.indexer, container.intake, CrabbyConstants.IntakeConstants.intakePurgeSpeed,
            CrabbyConstants.IndexerConstants.indexerPurgeSpeed ));
        
        // controller.rightBumper().whileTrue(new TurnToTarget(container.drive, container.setStateCommands,
        //     CrabbyConstants.DriveConstants.turnToTargetController, 
        //     () -> -MathUtil.applyDeadband(controller.getLeftY(), 0.1, 1),
        //     () -> -MathUtil.applyDeadband(controller.getLeftX(), 0.1, 1),
        //     () -> -MathUtil.applyDeadband(controller.getRightX(), 0.1, 1),
        //     container.aprilTagCamera::getSpeakerTag, true, true)
        //     .alongWith(new RampShooterWithDifferential(container.shooter,
        //         () -> container.topSpeedCalculator.getValueForDistance(container.lastRecordedDistance),
        //         () -> container.bottomSpeedCalculator.getValueForDistance(container.lastRecordedDistance)))
        //     .alongWith(new NFRWristContinuousAngle(container.wristJoint,
        //         () -> Rotation2d.fromRadians(container.angleCalculator.getValueForDistance(container.lastRecordedDistance)))));

        // controller.rightBumper().toggleOnTrue(new AutoShot(container.drive, container.setStateCommands, container.intake, container.indexer, container.wristJoint, 
        //     () -> -MathUtil.applyDeadband(controller.getLeftY(), 0.1, 1),
        //     () -> -MathUtil.applyDeadband(controller.getLeftX(), 0.1, 1),
        //     () -> -MathUtil.applyDeadband(controller.getRightX(), 0.1, 1), container.aprilTagCamera, container.shooter, () -> container.lastRecordedDistance, container.topSpeedCalculator, container.bottomSpeedCalculator, container.angleCalculator));
        
        controller.rightTrigger().and(() -> container.shooter.isAtSpeed(CrabbyConstants.ShooterConstants.tolerance))
            .and(() -> container.shooter.isRunning())
            .onTrue(new ShootIndexerAndIntake(container.indexer, container.intake, CrabbyConstants.IndexerConstants.indexerShootSpeed, -0.7));
        
        controller.start().whileTrue(new RampShooterWithDifferential(container.shooter,
            () -> container.shooterSpeed.getDouble(30) + container.topRollerChange.getDouble(0), () -> container.shooterSpeed.getDouble(30)));
        
        controller.rightBumper().whileTrue(new NFRRotatingArmJointSetAngle(container.wristJoint, CrabbyConstants.WristConstants.closeShotRotation,
            CrabbyConstants.WristConstants.tolerance, 0, true)
            .alongWith(new RampShooterContinuous(container.shooter, () -> CrabbyConstants.ShooterConstants.closeShotSpeed)));
        
        controller.leftBumper().whileTrue(new NFRRotatingArmJointSetAngle(container.wristJoint, CrabbyConstants.WristConstants.ampRotation,
            CrabbyConstants.WristConstants.tolerance, 0, true)
            .alongWith(new RampShooterWithDifferential(container.shooter, () -> CrabbyConstants.ShooterConstants.ampTopSpeed,
                () -> CrabbyConstants.ShooterConstants.ampBottomSpeed)));
        
        controller.start().toggleOnTrue(new RampShooterWithDifferential(
            container.shooter, () -> container.shooterSpeed.getDouble(0) + container.topRollerChange.getDouble(0),
                () -> container.shooterSpeed.getDouble(0)));
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
        
        controller.leftTrigger().whileTrue(new RunIndexerAndIntake(container.indexer, container.intake, CrabbyConstants.IndexerConstants.indexerSpeed,
            CrabbyConstants.IntakeConstants.intakeSpeed));
        
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

        container.climber.setDefaultCommand(Commands.run(() -> container.climber.startMotor(MathUtil.applyDeadband(-controller.getRightY(), 0.1)),
            container.climber));

        container.wristJoint.setDefaultCommand(new NFRRotatingArmJointWithJoystick(container.wristJoint, () -> MathUtil.applyDeadband(-controller.getLeftY(), 0.1))
                .alongWith(Commands.runOnce(() -> container.manualWrist = true)));
        
        // if (driverController != null)
        // {
        //     controller.rightBumper().whileTrue(new TurnToTarget(container.drive, container.setStateCommands,
        //         CrabbyConstants.DriveConstants.turnToTargetController, 
        //         () -> -MathUtil.applyDeadband(driverController.getLeftY(), 0.1, 1),
        //         () -> -MathUtil.applyDeadband(driverController.getLeftX(), 0.1, 1),
        //         () -> -MathUtil.applyDeadband(driverController.getRightX(), 0.1, 1),
        //         container.aprilTagCamera::getSpeakerTag, true, true)
        //         .alongWith(new RampShooterWithDifferential(container.shooter,
        //             () -> container.topSpeedCalculator.getValueForDistance(container.lastRecordedDistance),
        //             () -> container.bottomSpeedCalculator.getValueForDistance(container.lastRecordedDistance)))
        //         .alongWith(new NFRWristContinuousAngle(container.wristJoint,
        //             () -> Rotation2d.fromRadians(container.angleCalculator.getValueForDistance(container.lastRecordedDistance)))));
        //     // controller.rightBumper().toggleOnTrue(new AutoShot(container.drive, container.setStateCommands, container.intake, container.indexer, container.wristJoint, 
        //     //     () -> -MathUtil.applyDeadband(driverController.getLeftY(), 0.1, 1),
        //     //     () -> -MathUtil.applyDeadband(driverController.getLeftX(), 0.1, 1),
        //     //     () -> -MathUtil.applyDeadband(driverController.getRightX(), 0.1, 1), container.aprilTagCamera, container.shooter, () -> container.lastRecordedDistance, container.topSpeedCalculator, container.bottomSpeedCalculator, container.angleCalculator));
        // }
        
        controller.rightBumper().whileTrue(new NFRRotatingArmJointSetAngle(container.wristJoint, CrabbyConstants.WristConstants.closeShotRotation,
            CrabbyConstants.WristConstants.tolerance, 0, true)
            .alongWith(new RampShooterContinuous(container.shooter, () -> CrabbyConstants.ShooterConstants.closeShotSpeed)));
    }
    @Override
    public void bindManipulatorToJoystick(CrabbyContainer container, CommandGenericHID joystick)
    {
    }
}
