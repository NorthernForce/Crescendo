package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.NFRLED;

/**
 * Command that sets LEDs to rainbow when executed
 */
public class SetLEDRainbow extends Command {
    protected final NFRLED leds;
    public SetLEDRainbow(NFRLED leds)
    {
        addRequirements(leds);
        this.leds = leds;
    }
    @Override
    public void execute()
    {
        leds.rainbow();
    }
}
