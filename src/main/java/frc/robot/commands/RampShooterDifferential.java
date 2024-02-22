package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter;

public class RampShooterDifferential extends Command {
    protected final Shooter shooter;
    protected final DoubleSupplier targetTopSpeed, targetBottomSpeed;
    protected final double tolerance;
    /**
     * Creates a new command to ramp a shooter up to a dynamic speed, then finishes when at that speed
     * @param shooter the shooter
     * @param targetTopSpeed target top speed in rps
     * @param targetBottomSpeed target bottom speed in rps
     * @param tolerance in rps before shooter is considered ready
     */
    public RampShooterDifferential(Shooter shooter, DoubleSupplier targetTopSpeed, DoubleSupplier targetBottomSpeed, double tolerance)
    {
        this.shooter = shooter;
        this.targetTopSpeed = targetTopSpeed;
        this.targetBottomSpeed = targetBottomSpeed;
        this.tolerance = tolerance;
        addRequirements(shooter);
    }
    @Override
    public void execute()
    {
        shooter.runTop(targetTopSpeed.getAsDouble());
        shooter.runBottom(targetBottomSpeed.getAsDouble());
    }
    @Override
    public boolean isFinished()
    {
        return Math.abs(shooter.getTopVelocity() - targetTopSpeed.getAsDouble()) <= tolerance
            && Math.abs(shooter.getBottomVelocity() - targetBottomSpeed.getAsDouble()) <= tolerance;
    }
}
