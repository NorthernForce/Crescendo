package frc.robot.utils;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Subsystem;

public interface NFRLED extends Subsystem {
    public void setColor(Color color);
    public void rainbow();
}
