package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class NFRleds extends SubsystemBase {
    private AddressableLED m_led;
    private AddressableLEDBuffer m_ledBuffer;
    public NFRleds() {
        // PWM port 9
        // Must be a PWM header, not MXP or DIO
        m_led = new AddressableLED(9);
        // Reuse buffer
        // Default to a length of 60, start empty output
        // Length is expensive to set, so only set it once, then just update data

        m_ledBuffer = new AddressableLEDBuffer(60/*led strip length*/);
        m_led.setLength(m_ledBuffer.getLength());
    }

    public void setColor(short r, short g, short b) {
        // Set the data
        m_led.setData(m_ledBuffer);
        m_led.start();
        
        for (var i = 0; i < m_ledBuffer.getLength(); i++) {
            // Sets the specified LED to the RGB values for red
            m_ledBuffer.setRGB(i, r, g, b);
         }
         
         m_led.setData(m_ledBuffer);
    }

    public void setColor(Color8Bit color) {
        // Set the data
        m_led.setData(m_ledBuffer);
        m_led.start();
        
        for (var i = 0; i < m_ledBuffer.getLength(); i++) {
            // Sets the specified LED to the RGB values for red
            m_ledBuffer.setLED(i, color);
         }
         
         m_led.setData(m_ledBuffer);
    }

    public void ledOff() {
        m_led.stop();
    }
}