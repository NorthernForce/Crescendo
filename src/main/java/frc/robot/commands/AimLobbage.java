package frc.robot.commands;

import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.WristJoint;

import org.northernforce.commands.NFRRotatingArmJointSetAngle;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class AimLobbage extends ParallelCommandGroup{
    public AimLobbage(WristJoint wrist, Shooter shooter)
    {
       addCommands(
            new NFRRotatingArmJointSetAngle(wrist, Rotation2d.fromDegrees(30), Rotation2d.fromDegrees(0.2), 0, true), //TODO deez
            new RampShooterWithDifferential(shooter, () -> 30, () -> 20) //TODO doze
       ); 
    }

}
