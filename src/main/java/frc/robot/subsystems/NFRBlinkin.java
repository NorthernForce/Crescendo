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
    public static final Map<String, Double> colorMap = Map.ofEntries(
        // https://www.revrobotics.com/content/docs/REV-11-1105-UM.pdf
        // strings so only the exact preset colors are chosen
        Map.entry("kHotPink",    .57),
        Map.entry("kDarkRed",    .59),
        Map.entry("kRed",        .61),
        Map.entry("kOrangeRed",  .63),
        Map.entry("kOrange",     .65),
        Map.entry("kGold",       .67),
        Map.entry("kYellow",     .69),
        Map.entry("kLawnGreen",  .71),
        Map.entry("kLime",       .73),
        Map.entry("kDarkGreen",  .75),
        Map.entry("kGreen",      .77),
        // Blue Green      .79
        Map.entry("kAqua",       .81),
        Map.entry("kSkyBlue",    .83),
        Map.entry("kDarkBlue",   .85),
        Map.entry("kBlue",       .87),
        Map.entry("kBlueViolet", .89),
        Map.entry("kViolet",     .91),
        Map.entry("kWhite",      .93),
        Map.entry("kGray",       .95),
        Map.entry("kDarkGray",   .97),
        Map.entry("kBlack",      .99));
    public NFRBlinkin(NFRBlinkinConfiguration config) {
        super(config);
        this.blinkinSpark = new Spark(config.blinkinID);
    }
    public double mapColor(Color color) {
        return colorMap.containsKey(color.toString()) ? colorMap.get(color.toString()) : .93;
    }
    public void setColor(Color color) {
        blinkinSpark.set(mapColor(color));
    }
    public void rainbow() {
        blinkinSpark.set(-0.99); // rainbow PWM
    }
}
