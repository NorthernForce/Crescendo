package frc.robot.commands;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.NFRleds;

public class SetColor extends Command {
    private final NFRleds leds;
    private AddressableLEDBuffer buffer;
    public SetColor(NFRleds leds, Color color) {
        addRequirements(leds);
        this.leds = leds;
        this.buffer = leds.createBuffer();
        leds.setColor(new Color8Bit(color), buffer);
    }

    @Override
    public void initialize() {
        if (!leds.isOn()) leds.ledOn();
        leds.setBuffer(buffer);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}