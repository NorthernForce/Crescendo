package frc.robot.subsystems;

import java.util.Map;

import org.northernforce.subsystems.NFRSubsystem;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.utils.NFRLED;

/**
 * The NFRBlinkin subsystem.
 * 
 * <p><b>Important to note that the REV Blinkin only supports a few preset colors so
 * we map them to WPILib Colors and set the LEDs to white if the color is
 * incompatible (these colors should be enough for most needs)
 */
public class NFRBlinkin extends NFRSubsystem implements NFRLED {
    public static class NFRBlinkinConfiguration extends NFRSubsystemConfiguration {
        int blinkinID;
        public NFRBlinkinConfiguration(String name, int blinkinID) {
            super(name);
            this.blinkinID = blinkinID;
        }
    }
    protected final Spark blinkinSpark;
    public static final Map<Color, Double> colorMap = Map.ofEntries(
        // https://www.revrobotics.com/content/docs/REV-11-1105-UM.pdf
        Map.entry(Color.kHotPink,    .57),
        Map.entry(Color.kDarkRed,    .59),
        Map.entry(Color.kRed,        .61),
        Map.entry(Color.kOrangeRed,  .63),
        Map.entry(Color.kOrange,     .65),
        Map.entry(Color.kGold,       .67),
        Map.entry(Color.kYellow,     .69),
        Map.entry(Color.kLawnGreen,  .71),
        Map.entry(Color.kLime,       .73),
        Map.entry(Color.kDarkGreen,  .75),
        Map.entry(Color.kGreen,      .77),
        // Blue Green      .79
        Map.entry(Color.kAqua,       .81),
        Map.entry(Color.kSkyBlue,    .83),
        Map.entry(Color.kDarkBlue,   .85),
        Map.entry(Color.kBlue,       .87),
        Map.entry(Color.kBlueViolet, .89),
        Map.entry(Color.kViolet,     .91),
        Map.entry(Color.kWhite,      .93),
        Map.entry(Color.kGray,       .95),
        Map.entry(Color.kDarkGray,   .97),
        Map.entry(Color.kBlack,      .99));
    public NFRBlinkin(NFRBlinkinConfiguration config) {
        super(config);
        this.blinkinSpark = new Spark(config.blinkinID);
    }
    public double mapColor(Color color) {
        return (colorMap.get(color) == null) ? colorMap.get(color) : .93;
    }
    public void setColor(Color color) {
        blinkinSpark.set(mapColor(color));
    }
    public void rainbow() {
        blinkinSpark.set(-0.99); // rainbow PWM
    }
}
