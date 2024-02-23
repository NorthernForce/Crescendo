package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;

public class RumbleController extends Command {
    protected final XboxController controller;
    protected final double strength, time;
    protected final Timer timer;
    /**
     * Rumbles an xbox controller for a set period of time
     * @param controller the controller
     * @param strength the strength [0, 1]
     * @param time the time to rumble for in seconds
     */
    public RumbleController(XboxController controller, double strength, double time)
    {
        this.controller = controller;
        this.strength = strength;
        this.time = time;
        this.timer = new Timer();
    }
    @Override
    public void initialize()
    {
        controller.setRumble(RumbleType.kBothRumble, strength);
        timer.restart();
    }
    @Override
    public void end(boolean interrupted)
    {
        controller.setRumble(RumbleType.kBothRumble, 0);
    }
    @Override
    public boolean isFinished()
    {
        return timer.hasElapsed(time);
    }
}
