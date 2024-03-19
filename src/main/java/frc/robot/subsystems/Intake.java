package frc.robot.subsystems;
import org.littletonrobotics.junction.Logger;
import org.northernforce.motors.NFRMotorController;
import org.northernforce.motors.NFRSparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.LoggableHardware;

public class Intake extends SubsystemBase implements LoggableHardware {
    private final NFRMotorController motors;
    public Intake(NFRSparkMax motor)
    {
        motors = motor;
    }
    public void run(double speed) {
        this.motors.set(speed);
    }
    @Override
    public void startLogging(double period) {
    }
    @Override
    public void logOutputs(String name) {
        Logger.recordOutput(name + "/Speed", motors.get());
    }
    
}
