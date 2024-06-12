package frc.robot.subsystems;

import org.northernforce.subsystems.NFRSubsystem;

import com.ctre.phoenix.led.CANdle;
import com.ctre.phoenix.led.CANdleConfiguration;
import com.ctre.phoenix.led.RainbowAnimation;

import edu.wpi.first.wpilibj.util.Color;
import frc.robot.utils.NFRLED;
/**
 * The NFRCandleLed subsystem.
 * 
 * This subsystem implements the NFRLed subsystem and should support any Color
 * passed to it without problem.
 */
public class NFRCANdle extends NFRSubsystem implements NFRLED
{
    /**
     * Configuration class for the NFRCandleLedConfiguration subsystem
     */
    public static class NFRCANdleConfiguration extends NFRSubsystemConfiguration
    {
        protected int candleId;
        protected CANdleConfiguration candleConfig;
        protected int ledCount;
        /**
         * Creates a new NFRCandleLEDConfiguration
         * @param name name of subsystem
         * @param candleId the ID of the CANdle
         * @param candleConfig configuration for the CANdle
         * @param ledCount number of leds controlled by the CANdle
         */
        public NFRCANdleConfiguration(String name, int candleId, CANdleConfiguration candleConfig, int ledCount)
        {
            super(name);
            this.candleConfig = candleConfig;
            this.ledCount = ledCount;
        }
    }
    protected final CANdle candle;
    protected final int ledCount;
    public NFRCANdle(NFRCANdleConfiguration config)
    {
        super(config);
        candle = new CANdle(config.candleId);
        candle.configAllSettings(config.candleConfig);
        this.ledCount = config.ledCount;
    }
    public void setColor(Color color)
    {
        candle.setLEDs((int)color.red*255, (int)color.green*255, (int)color.blue*255);
    }
    public void rainbow()
    {
        RainbowAnimation animation = new RainbowAnimation(1, 1, ledCount);
        candle.animate(animation);
    }
}
