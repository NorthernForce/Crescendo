package frc.robot.commands;

import org.northernforce.subsystems.arm.NFRRotatingArmJoint;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;

public class NFRResetWristCommand extends Command {
    NFRRotatingArmJoint m_wrist;
    public NFRResetWristCommand(NFRRotatingArmJoint wrist)
    {
        m_wrist = wrist;
    }
    @Override public void initialize()
    {
        addRequirements(m_wrist);
        m_wrist.getController().getAbsoluteEncoder().get().setAbsoluteOffset(
            MathUtil.inputModulus(Rotation2d.fromDegrees(22).getRotations() - 
            m_wrist.getController().getAbsoluteEncoder().get().getAbsolutePosition() + 
            m_wrist.getController().getAbsoluteEncoder().get().getAbsoluteOffset(), 0, 1));
    }
}
