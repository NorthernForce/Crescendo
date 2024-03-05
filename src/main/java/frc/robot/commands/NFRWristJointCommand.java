package frc.robot.commands;

import org.northernforce.commands.NFRRotatingArmJointSetAngle;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.WristJoint;
import frc.robot.subsystems.WristJoint.PositioningType;

public class NFRWristJointCommand extends NFRRotatingArmJointSetAngle
{

    public NFRWristJointCommand(WristJoint wrist, PositioningType positioningType, double distance) 
    {
        super(wrist, wrist.getSpeakerAngle(positioningType, distance), new Rotation2d(0.5), 0, false);
    }
    
    
}