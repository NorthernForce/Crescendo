package frc.robot.dashboard;

import java.util.function.BooleanSupplier;

import edu.wpi.first.math.geometry.Rotation2d;

public interface CrabbyDashboard extends Dashboard {
    public double getShooterSpeed();
    public double getTopRollerChange();
    public Rotation2d getTargetWristAngle();
    public void setWristManual(BooleanSupplier supplier);
}