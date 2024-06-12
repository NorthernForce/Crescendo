package frc.robot.subsystems;

import org.northernforce.subsystems.NFRSubsystem;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.utils.NFRLED;

/**
 * the NFRAdressableLED subsystem.
 * 
 * <p>This subsystem allows an AdressableLED to be compliant with the NFRLED
 * interface, allowing for use with more general commands.
 */
public class NFRAddressableLED extends NFRSubsystem implements NFRLED
{
    /**
     * configuration for an NFRAddressableLED
     */
    public static class NFRAddressableLEDConfiguration extends NFRSubsystemConfiguration
    {
        int ledPort;
        int ledCount;
        /**
         * Initializes an NFRAddressableLED.
         * @param name name of subsystem
         * @param ledPort PWM port of the LEDs
         * @param ledCount number of LEDs on the strip
         */
        public NFRAddressableLEDConfiguration(String name, int ledPort, int ledCount)
        {
            super(name);
            this.ledPort = ledPort;
            this.ledCount = ledCount;
        }
    }
    protected final AddressableLED led;
    protected final AddressableLEDBuffer ledBuffer;
    boolean runRainbow;
    int rainbowFirstPixelHue;
    public NFRAddressableLED(NFRAddressableLEDConfiguration config)
    {
        super(config);
        led = new AddressableLED(config.ledPort);
        ledBuffer = new AddressableLEDBuffer(config.ledCount);
        runRainbow = false;
        rainbowFirstPixelHue = 0;
        led.setLength(ledBuffer.getLength());
        led.setData(ledBuffer);
        led.start();
    }
    public void setColor(Color color)
    {
        runRainbow = false;
        ledBuffer.forEach((index, r, g, b) -> ledBuffer.setLED(index, color));
        led.setData(ledBuffer);
    }
    public void rainbow() {
        runRainbow = true;
    }
    @Override
    public void periodic()
    {
        if (!runRainbow)
        {
            return;
        }
        // For every pixel
        for (var i = 0; i < ledBuffer.getLength(); i++) {
          // Calculate the hue - hue is easier for rainbows because the color
          // shape is a circle so only one value needs to precess
          final var hue = (rainbowFirstPixelHue + (i * 180 / ledBuffer.getLength())) % 180;
          // Set the value
          ledBuffer.setHSV(i, hue, 255, 128);
        }
        // Increase by to make the rainbow "move"
        rainbowFirstPixelHue += 3;
        // Check bounds
        rainbowFirstPixelHue %= 180;
    }
}
