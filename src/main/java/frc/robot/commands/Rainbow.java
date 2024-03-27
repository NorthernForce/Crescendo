package frc.robot.commands;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.NFRleds;

public class Rainbow extends Command {
    private NFRleds leds;
    private final AddressableLEDBuffer buffer;
    public Rainbow(NFRleds leds) {
        this.leds = leds;
        this.buffer = leds.createBuffer();
        addRequirements(leds);
    }
    @Override
    public void initialize() {
        if (!leds.isOn()) leds.ledOn();
    }
    @Override
    public void execute() {
        leds.rainbow(buffer);
        leds.setBuffer(buffer);
    }
}
