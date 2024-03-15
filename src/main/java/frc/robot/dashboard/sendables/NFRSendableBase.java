package frc.robot.dashboard.sendables;

import java.util.function.Supplier;

import edu.wpi.first.networktables.NTSendable;

public interface NFRSendableBase extends NTSendable, AutoCloseable{
    public void update();
}
