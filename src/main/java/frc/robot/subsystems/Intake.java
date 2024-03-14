package frc.robot.subsystems;
import org.northernforce.motors.NFRSparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase{
    private final NFRSparkMax motors;
    public Intake(NFRSparkMax motor)
    {
        motors = motor;
    }
    public void run(double speed) {
        this.motors.set(speed);
    }
}
