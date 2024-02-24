package frc.robot.commands;

import java.util.Optional;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.WristJoint;

public class NFRWristContinuous extends Command
{
    private Supplier<Optional<Double>> m_distanceSupplied;
    private WristJoint m_wrist;
    public NFRWristContinuous (WristJoint wrist, Supplier<Optional<Double>> distanceSupplied)
    {
        m_distanceSupplied = distanceSupplied;
        m_wrist = wrist;
        addRequirements(m_wrist);
    }
    @Override public void initialize()   
    {

    }
    @Override public void execute()
    {
        Optional<Double> distance = m_distanceSupplied.get();
        if(distance.isPresent())
        {
            m_wrist.getController().setPosition(0, m_wrist.getSpeakerAngle(false, distance.get()).getRotations()); //TODO get actual distance
        }
        
    }
    @Override public boolean isFinished()
    {
        return false;
    }
    
}