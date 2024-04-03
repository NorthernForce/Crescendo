package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LEDRelay extends SubsystemBase
{
    protected final Relay relay;
    public LEDRelay(int relayChannel)
    {
        super("LEDRelay");
        this.relay = new Relay(relayChannel, Direction.kForward);
        this.relay.setSafetyEnabled(false);
    }
    public void setRelay(boolean state)
    {
        relay.set(state ? Value.kOn : Value.kOff);
    }
}
