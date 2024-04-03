package frc.robot.commands;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.NFRLED;

public class SetLEDColor extends Command
{
    protected final NFRLED leds;
    protected final Color color;
    public SetLEDColor(NFRLED leds, Color color)
    {
        addRequirements(leds);
        this.leds = leds;
        this.color = color;
    }
    @Override
    public void execute()
    {
        leds.setColor(color);
    }
}
