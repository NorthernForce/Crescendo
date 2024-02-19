package frc.robot.subsystems;

import org.northernforce.motors.NFRSparkMax;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private final int PRIMARY_MOTOR_ID = 0; // TODO: set actual motor IDs
    private final int[] FOLLOWER_MOTOR_IDS = {1, 2, 3};
    private final NFRSparkMax motors;
    /** Creates a new Intake subsystem */
    public Intake() {
        this.motors = new NFRSparkMax(MotorType.kBrushless, PRIMARY_MOTOR_ID, FOLLOWER_MOTOR_IDS);
    }
    /**
     * Runs the intake motors
     * @param speed raw speed value to run motors at
     */
    public void run(double speed) {
        this.motors.set(speed);
    }
    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("Motor Speeds", () -> this.motors.get(), speed -> this.motors.set(speed));
    }
}
