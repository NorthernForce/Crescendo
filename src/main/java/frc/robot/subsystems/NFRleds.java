package frc.robot.subsystems;

import java.util.concurrent.atomic.AtomicInteger;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.CrabbyConstants;

public class NFRleds extends SubsystemBase {
    private AddressableLED m_led;
    private AtomicInteger m_ledTick;
    private final Notifier m_tickNotifier;
    private boolean isStarted = false;
    public NFRleds() {
        // PWM port 9
        // Must be a PWM header, not MXP or DIO
        m_led = new AddressableLED(0);
        m_led.setBitTiming(350, 900, 900, 600);
        m_led.setSyncTime(2500);
        m_l
        // Reuse buffer
        // Default to a length of 60, start empty output
        // Length is expensive to set, so only set it once, then just update data

        m_ledTick = new AtomicInteger();
        m_tickNotifier = new Notifier(() -> {
            m_ledTick.incrementAndGet();
        });
        m_led.setLength(CrabbyConstants.LEDConstants.ledCount);
        m_tickNotifier.startPeriodic(CrabbyConstants.LEDConstants.tickPeriod);
    }

    public void setColor(short r, short g, short b, AddressableLEDBuffer m_ledBuffer) {
        // Set the data
        
        for (var i = 0; i < m_ledBuffer.getLength(); i++) {
            // Sets the specified LED to the RGB values for red
            m_ledBuffer.setRGB(i, r, g, b);
        }
    }

    public void setColor(Color8Bit color, AddressableLEDBuffer m_ledBuffer) {
        
        for (var i = 0; i < m_ledBuffer.getLength(); i++) {
            // Sets the specified LED to the RGB values for red
            m_ledBuffer.setLED(i, color);
        }
    }
    public void ledOn() {
        isStarted = true;
        m_led.start();
    }

    public boolean isOn() {
        return isStarted;
    }

    public void ledOff() {
        isStarted = false;
        m_led.stop();
    }

    public void setBuffer(AddressableLEDBuffer buffer) {
        m_led.setData(buffer);
    }

    public AddressableLEDBuffer createBuffer() {
        return new AddressableLEDBuffer(CrabbyConstants.LEDConstants.ledCount);
    }

    public void rainbow(AddressableLEDBuffer buffer) {
        for (int i = 0; i < buffer.getLength(); i++) {
            final var hue = (m_ledTick.get() % 180 + (i * 180 / buffer.getLength())) % 180;
            buffer.setHSV(i, hue, 255, 128);
        }
    }
}