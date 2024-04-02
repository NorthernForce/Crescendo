package frc.robot.commands;

import org.northernforce.commands.NFRRotatingArmJointSetAngle;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.WristJoint;

public class FixedShot extends SequentialCommandGroup {
    /**
     * Prepares the shooter and wrist for a fixed shot, then shoots, and waits for a set amount of time after as clearance.
     * @param shooter the shooter subsystem
     * @param wrist the wrist joint
     * @param intake the intake subsystem
     * @param shooterSpeed the speed to run the shooter at (RPS)
     * @param shooterTolerance the tolerance for the shooter to be "ramped" (RPS)
     * @param targetAngle the target angle of the wrist
     * @param angularTolerance the tolerance for being aligned
     * @param intakeSpeed the speed to run the intake at
     * 
     * @param clearanceTime the time to wait after feeding the piece to the shooter
     */
    public FixedShot(Shooter shooter, WristJoint wrist, Indexer indexer, Intake intake, double topSpeed, double bottomSpeed, Rotation2d targetAngle,
        Rotation2d angularTolerance, double indexerSpeed, double intakeSpeed, double clearanceTime)
    {
        addCommands(
            new ParallelCommandGroup(
                new NFRRotatingArmJointSetAngle(wrist, targetAngle, angularTolerance, 0, true),
                new RampShooterWithDifferential(shooter, () -> topSpeed, () -> bottomSpeed)
                    .until(() -> shooter.isRunning() && shooter.isAtSpeed())
            ),
            new ShootIndexerAndIntake(indexer, intake, indexerSpeed, intakeSpeed),
            new WaitCommand(clearanceTime)
        );
        addRequirements(shooter, wrist, intake, indexer);
    }
}
