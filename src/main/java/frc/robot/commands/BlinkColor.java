package frc.robot.commands;

import java.util.concurrent.atomic.AtomicBoolean;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.NFRleds;

public class BlinkColor extends Command {
    private final NFRleds leds;
    private final AddressableLEDBuffer onBuffer, offBuffer;
    private final AtomicBoolean switcher;
    private final Notifier notifier;
    public BlinkColor(NFRleds leds, Color color, double alternatePeriod) {
        this.leds = leds;
        onBuffer = leds.createBuffer();
        offBuffer = leds.createBuffer();
        leds.setColor(new Color8Bit(color), onBuffer);
        leds.setColor(new Color8Bit(0, 0, 0), offBuffer);
        switcher = new AtomicBoolean();
        notifier = new Notifier(() -> {
            switcher.set(!switcher.get());
        });
        notifier.startPeriodic(alternatePeriod);
        addRequirements(leds);
    }
    @Override
    public void execute() {
        if (switcher.get()) {
            leds.setBuffer(onBuffer);
        }
        else
        {
            leds.setBuffer(offBuffer);
        }
    }
}
