package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

public class RampShooter extends Command {
    protected final Shooter shooter;
    protected final DoubleSupplier targetSpeed;
    protected final double tolerance;
    /**
     * Creates a new command to ramp a shooter up to a dynamic speed, then finishes when at that speed
     * @param shooter the shooter
     * @param targetSpeed target speed in rps
     * @param tolerance tolerance before shooter is considered ramped in rps
     */
    public RampShooter(Shooter shooter, DoubleSupplier targetSpeed, double tolerance)
    {
        this.shooter = shooter;
        this.targetSpeed = targetSpeed;
        this.tolerance = tolerance;
        addRequirements(shooter);
    }
    @Override
    public void execute()
    {
        shooter.run(targetSpeed.getAsDouble());
    }
    @Override
    public boolean isFinished()
    {
        return Math.abs(shooter.getTopVelocity() - targetSpeed.getAsDouble()) <= tolerance
            && Math.abs(shooter.getBottomVelocity() - targetSpeed.getAsDouble()) <= tolerance;
    }
}
