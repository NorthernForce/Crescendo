package frc.robot.commands;

import org.northernforce.commands.NFRRotatingArmJointSetAngle;
import org.northernforce.subsystems.arm.NFRRotatingArmJoint;

import edu.wpi.first.math.geometry.Rotation2d;

public class NFRRotatingArmJointSetAngleWithGear extends NFRRotatingArmJointSetAngle{

    public NFRRotatingArmJointSetAngleWithGear(NFRRotatingArmJoint arm, Rotation2d targetAngle, Rotation2d tolerance,
            int pidSlot, boolean useTrapezoidalPositioning, Rotation2d gearRatio) {
        super(arm, new Rotation2d(targetAngle.getDegrees()*gearRatio.getDegrees()), tolerance, pidSlot, useTrapezoidalPositioning);
        
    }
    public NFRRotatingArmJointSetAngleWithGear(NFRRotatingArmJoint arm, Rotation2d targetAngle, Rotation2d tolerance,
            int pidSlot, boolean useTrapezoidalPositioning, double gearRatio) {
        super(arm, new Rotation2d(targetAngle.getDegrees()*gearRatio), tolerance, pidSlot, useTrapezoidalPositioning);
        
    }
    public NFRRotatingArmJointSetAngleWithGear(NFRRotatingArmJoint arm, double targetAngle, Rotation2d tolerance,
            int pidSlot, boolean useTrapezoidalPositioning, Rotation2d gearRatio) {
        super(arm, new Rotation2d(targetAngle*gearRatio.getDegrees()), tolerance, pidSlot, useTrapezoidalPositioning);
        
    }
    public NFRRotatingArmJointSetAngleWithGear(NFRRotatingArmJoint arm, double targetAngle, Rotation2d tolerance,
            int pidSlot, boolean useTrapezoidalPositioning, double gearRatio) {
        super(arm, new Rotation2d(targetAngle*gearRatio), tolerance, pidSlot, useTrapezoidalPositioning);
        
    }
    
}