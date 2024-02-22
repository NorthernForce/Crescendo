package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

public class RestShooter extends Command
{
    protected final Shooter shooter;
    /**
     * Sets the shooter rps to 0 on both top and bottom
     * @param shooter the shooter subsystem
     */
    public RestShooter(Shooter shooter)
    {
        addRequirements(shooter);
        this.shooter = shooter;
    }
    @Override
    public void initialize()
    {
        shooter.run(0);
    }
}
