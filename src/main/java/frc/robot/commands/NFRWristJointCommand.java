package frc.robot.commands;

import org.northernforce.commands.NFRRotatingArmJointSetAngle;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.WristJoint;

public class NFRWristJointCommand extends NFRRotatingArmJointSetAngle
{

    public NFRWristJointCommand(WristJoint wrist, boolean useAbsolutePositioning) 
    {
        super(wrist.getMotor(), new Rotation2d(wrist.getAmpAngle(useAbsolutePositioning)*wrist.getGearRatio()), new Rotation2d(0.5), 0, false);
        System.out.println("Rotated motor " + wrist.getAmpAngle(useAbsolutePositioning)*wrist.getGearRatio() + " degrees.");
    }
    
    
}