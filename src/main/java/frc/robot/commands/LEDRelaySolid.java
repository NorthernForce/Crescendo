package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LEDRelay;

public class LEDRelaySolid extends Command
{
    protected final LEDRelay ledRelay;
    protected final boolean state;
    public LEDRelaySolid(LEDRelay ledRelay, boolean state)
    {
        addRequirements(ledRelay);
        this.ledRelay = ledRelay;
        this.state = state;
    }
    @Override
    public void initialize()
    {
        ledRelay.setRelay(state);
    }
}
