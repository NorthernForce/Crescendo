package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.WristJoint;

public class NFRWristContinuousAngle extends Command
{
    private Supplier<Rotation2d> m_angle;
    private WristJoint m_wrist;
    public NFRWristContinuousAngle (WristJoint wrist, Supplier<Rotation2d> angle)
    {
        m_angle = angle;
        m_wrist = wrist;
        addRequirements(m_wrist);
    }
    @Override public void initialize()   
    {
        
    }
    @Override public void execute()
    {
        m_wrist.getController().setPositionTrapezoidal(0, m_angle.get().getRotations());
    }
    @Override public boolean isFinished()
    {
        return false;
    }
}
