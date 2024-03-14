package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;

public class RunIntake extends Command{
    private Intake m_intake;
    private double m_speed;
    public RunIntake(Intake intake, double speed)
    {
        m_speed = speed;
        m_intake = intake;
        addRequirements(m_intake);
    }
    @Override
    public void initialize() {
        m_intake.run(m_speed);
    }

    @Override
    public void end(boolean interrupted) {
        m_intake.run(0);
    }
    
}
