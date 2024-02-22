package frc.robot.commands;

import java.util.Optional;

import org.northernforce.commands.NFRRotatingArmJointSetAngle;
import org.northernforce.motors.NFRSparkMax;
import org.northernforce.subsystems.arm.NFRRotatingArmJoint;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.WristJoint;

public class NFRWristContinuous extends Command
{
    
    private WristJoint m_wrist;
    private XboxController m_controller;
    public NFRWristContinuous (WristJoint wrist, XboxController controller)
    {
        m_controller = controller;
        m_wrist = wrist;
    }
    @Override public void initialize()   
    {

    }
    @Override public void execute()
    {
        m_wrist.getController().setPosition(0, Optional.of(m_wrist.getAmpAngle(false, 0)).orElse(45.0)); //TODO get actual distance
    }
    @Override public boolean isFinished()
    {
        return false;
    }
    
}