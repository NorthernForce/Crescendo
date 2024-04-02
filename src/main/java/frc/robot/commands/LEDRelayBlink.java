package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LEDRelay;

public class LEDRelayBlink extends Command
{
    protected final LEDRelay ledRelay;
    protected final double interval;
    protected final Timer timer;
    boolean relayState;
    public LEDRelayBlink(LEDRelay ledRelay, double interval)
    {
        this.ledRelay = ledRelay;
        this.interval = interval;
        this.timer = new Timer();
        this.relayState = true;
    }
    @Override
    public void initialize()
    {
        timer.start();
    }
    @Override
    public void execute()
    {
        if (timer.hasElapsed(interval))
        {
            relayState = !relayState;
            ledRelay.setRelay(relayState);
            timer.reset();
        }
    }
    @Override
    public void end(boolean interrupted)
    {
        timer.stop();
    }
}
