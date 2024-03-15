package frc.robot.dashboard.sendables;

import edu.wpi.first.networktables.NTSendable;

public interface NFRSendableBase extends NTSendable, AutoCloseable{
    public void update();
}
