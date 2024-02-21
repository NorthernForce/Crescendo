package frc.robot.subsystems;

import org.northernforce.motors.NFRSparkMax;

import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private final NFRSparkMax motors;
    /** Creates a new Intake subsystem
     * @param primaryMotor the primary motor of the intake (doesn't matter which one)
     * @param followerMotors the follower motors
    */
    public Intake(int primaryMotor, int... followerMotors) {
        this.motors = new NFRSparkMax(MotorType.kBrushless, primaryMotor, followerMotors);
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
