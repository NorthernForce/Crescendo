package frc.robot.commands;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.NFRleds;

public class SetColor extends Command {
    private final NFRleds leds;
    private Color color;
    public SetColor(NFRleds leds, Color color) {
        addRequirements(leds);
        this.leds = leds;
        this.color = color;
    }

    @Override
    public void initialize() {
        leds.setColor(new Color8Bit(color));
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}