package frc.robot.commands;

import org.northernforce.commands.NFRRotatingArmJointSetAngle;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.WristJoint;

public class NFRWristJointCommand extends NFRRotatingArmJointSetAngle
{

    public NFRWristJointCommand(WristJoint wrist, boolean useAbsolutePositioning, double distance) 
    {
        super(wrist, wrist.getSpeakerAngle(useAbsolutePositioning, distance), new Rotation2d(0.5), 0, false);
    }
    
    
}