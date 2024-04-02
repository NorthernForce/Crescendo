package frc.robot.subsystems;
import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;
import org.northernforce.motors.NFRSparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private final NFRSparkMax motors;
    private final String name;
    public Intake(NFRSparkMax motor)
    {
        motors = motor;
        this.name = getName();
    }
    public void run(double speed) {
        Logger.recordOutput(name + "/Speed", speed);
        this.motors.set(speed);
    }
    @AutoLogOutput(key="{name}/MotorCurrent")
    public double getMotorCurrent()
    {
        return motors.getOutputCurrent();
    }
}
