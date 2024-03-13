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
import frc.robot.commands.FollowNote;
import frc.robot.commands.NFRWristContinuousAngle;
import frc.robot.commands.PurgeIntake;
import frc.robot.commands.RampShooterContinuous;
import frc.robot.commands.RampShooterWithDifferential;
import frc.robot.commands.RumbleController;
import frc.robot.commands.RunIntake;
import frc.robot.commands.ShootIntake;
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
        
        controller.b().onTrue(Commands.runOnce(container.drive::clearRotation, container.drive));
        
        controller.y().whileTrue(new NFRSwerveDriveStop(container.drive, container.setStateCommands, true));
        
        controller.a().whileTrue(new FollowNote(container.xavier, container.drive, container.setStateCommands,
            () -> -MathUtil.applyDeadband(controller.getLeftX(), 0.1, 1), true));
        
        controller.leftTrigger().whileTrue(new RunIntake(container.intake, CrabbyConstants.IntakeConstants.intakeSpeed));
        
        new Trigger(() -> container.intake.getBeamBreak().beamBroken()).onTrue(new RumbleController(controller.getHID(), 0.5, 0.5));
        
        controller.back().whileTrue(new PurgeIntake(container.intake, CrabbyConstants.IntakeConstants.intakePurgeSpeed));
        
        controller.rightBumper().toggleOnTrue(new TurnToTarget(container.drive, container.setStateCommands,
            CrabbyConstants.DriveConstants.turnToTargetController, 
            () -> -MathUtil.applyDeadband(controller.getLeftY(), 0.1, 1),
            () -> -MathUtil.applyDeadband(controller.getLeftX(), 0.1, 1),
            () -> -MathUtil.applyDeadband(controller.getRightX(), 0.1, 1),
            container.aprilTagCamera::getSpeakerTag, true, true)
            .alongWith(new RampShooterContinuous(container.shooter,
                () -> container.speedCalculator.getValueForDistance(container.lastRecordedDistance)))
            .alongWith(new NFRWristContinuousAngle(container.wristJoint,
                () -> Rotation2d.fromRadians(container.angleCalculator.getValueForDistance(container.lastRecordedDistance)))));
        
        controller.rightTrigger().and(() -> container.shooter.isAtSpeed(CrabbyConstants.ShooterConstants.tolerance))
            .onTrue(new ShootIntake(container.intake, CrabbyConstants.IntakeConstants.intakeSpeed));
        
        controller.start().toggleOnTrue(new RampShooterContinuous(container.shooter, () -> container.shooterSpeed.getDouble(30)));
        
        controller.povLeft().toggleOnTrue(new NFRRotatingArmJointSetAngle(container.wristJoint, CrabbyConstants.WristConstants.closeShotRotation,
            CrabbyConstants.WristConstants.tolerance, 0, true)
            .alongWith(new RampShooterContinuous(container.shooter, () -> CrabbyConstants.ShooterConstants.closeShotSpeed)));
        
        controller.leftBumper().toggleOnTrue(new NFRRotatingArmJointSetAngle(container.wristJoint, CrabbyConstants.WristConstants.ampRotation,
            CrabbyConstants.WristConstants.tolerance, 0, true)
            .alongWith(new RampShooterWithDifferential(container.shooter, () -> CrabbyConstants.ShooterConstants.ampTopSpeed,
                () -> CrabbyConstants.ShooterConstants.ampBottomSpeed)));
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
        controller.leftTrigger().whileTrue(new RunIntake(container.intake, CrabbyConstants.IntakeConstants.intakeSpeed));
        
        new Trigger(() -> container.intake.getBeamBreak().beamBroken())
            .onTrue(new RumbleController(controller.getHID(), 0.5, 0.5));
        
        controller.back().whileTrue(new PurgeIntake(container.intake, CrabbyConstants.IntakeConstants.intakePurgeSpeed));
        
        container.wristJoint.setDefaultCommand(new NFRRotatingArmJointWithJoystick(container.wristJoint,
            () -> -MathUtil.applyDeadband(controller.getLeftY(), 0.1, 1)).alongWith(Commands.runOnce(() -> container.manualWrist = true)));
        
        controller.rightTrigger().and(() -> container.shooter.isAtSpeed(CrabbyConstants.ShooterConstants.tolerance))
            .onTrue(new ShootIntake(container.intake, CrabbyConstants.IntakeConstants.intakeSpeed));

        controller.leftBumper().toggleOnTrue(new NFRRotatingArmJointSetAngle(container.wristJoint, CrabbyConstants.WristConstants.ampRotation,
            CrabbyConstants.WristConstants.tolerance, 0, true)
            .alongWith(new RampShooterWithDifferential(container.shooter, () -> CrabbyConstants.ShooterConstants.ampTopSpeed,
                () -> CrabbyConstants.ShooterConstants.ampBottomSpeed)));
        
        controller.rightBumper().toggleOnTrue(new TurnToTarget(container.drive, container.setStateCommands,
            CrabbyConstants.DriveConstants.turnToTargetController, 
            () -> -MathUtil.applyDeadband(controller.getLeftY(), 0.1, 1),
            () -> -MathUtil.applyDeadband(controller.getLeftX(), 0.1, 1),
            () -> -MathUtil.applyDeadband(controller.getRightX(), 0.1, 1),
            container.aprilTagCamera::getSpeakerTag, true, true)
            .alongWith(new RampShooterContinuous(container.shooter,
                () -> container.speedCalculator.getValueForDistance(container.lastRecordedDistance)))
            .alongWith(new NFRWristContinuousAngle(container.wristJoint,
                () -> Rotation2d.fromRadians(container.angleCalculator.getValueForDistance(container.lastRecordedDistance)))));
        
        controller.povLeft().toggleOnTrue(new NFRRotatingArmJointSetAngle(container.wristJoint, CrabbyConstants.WristConstants.closeShotRotation,
            CrabbyConstants.WristConstants.tolerance, 0, true)
            .alongWith(new RampShooterContinuous(container.shooter, () -> CrabbyConstants.ShooterConstants.closeShotSpeed)));
    }
    @Override
    public void bindManipulatorToJoystick(CrabbyContainer container, CommandGenericHID joystick)
    {
    }
}
