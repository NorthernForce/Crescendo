package main.java.frc.robot.commands;

import edu.wpi.wpilibj.Command;

public class SetColor extends Command {
    private NFRleds Leds;
    public ShowPink() {
        Leds = new NFRleds();
    }
    @Override
    public Color(short r, short g, short b) {
        Leds.setColor(r, g, b);
    }
    @Override
    public offColor() {
        Leds.offColor();
    }
}