package frc.robot.commands;

import org.northernforce.motors.NFRSparkMax;
import org.northernforce.subsystems.arm.NFRRotatingArmJoint;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;

public class NFRResetWristCommand extends Command {
    NFRRotatingArmJoint m_wrist;
    public NFRResetWristCommand(NFRRotatingArmJoint wrist)
    {
        m_wrist = wrist;
        addRequirements(m_wrist);
    }
    @Override public void initialize()
    {
        m_wrist.getController().getAbsoluteEncoder().get().setAbsoluteOffset(
            MathUtil.inputModulus(Rotation2d.fromDegrees(22).getRotations() * 3 - 
            m_wrist.getController().getAbsoluteEncoder().get().getAbsolutePosition() + 
            m_wrist.getController().getAbsoluteEncoder().get().getAbsoluteOffset(), 0, 1));
        ((NFRSparkMax)m_wrist.getController()).burnFlash();
    }
}
