package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.NFRleds;

public class SetColor extends Command {
    private final NFRleds leds;
    public SetColor(NFRleds leds) {
        addRequirements(leds);
        this.leds = leds;
    }
    public void Color(short r, short g, short b) {
        leds.setColor(r, g, b);
    }
    public void offColor() {
        leds.ledOff();
    }
}