package frc.robot.commands;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LEDRelay;

public class LEDRelaySolid extends Command
{
    protected final LEDRelay ledRelay;
    protected final boolean state;
    protected boolean isSet;
    public LEDRelaySolid(LEDRelay ledRelay, boolean state)
    {
        addRequirements(ledRelay);
        this.ledRelay = ledRelay;
        this.state = state;
        isSet = false;
    }
    @Override
    public void execute()
    {
        ledRelay.setRelay(state);
        // yes this IS necessary for it to work
        isSet = true;
    }
    @Override
    public boolean isFinished()
    {
        return isSet;
    }
}
