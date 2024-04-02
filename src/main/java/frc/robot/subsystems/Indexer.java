package frc.robot.subsystems;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;
import org.northernforce.motors.NFRSparkMax;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.sensors.NFRBeamBreak;

public class Indexer extends SubsystemBase {
    private final NFRSparkMax motors;
    private final NFRBeamBreak beamBreak;
    private final String name;
    /** Creates a new Intake subsystem
     * @param motors the motor of the intake
     * @param beamBreak the beam break
    */
    public Indexer(NFRSparkMax motors, NFRBeamBreak beamBreak) {
        this.motors = motors;
        this.beamBreak = beamBreak;
        this.name = getName();
    }
    /**
     * Runs the intake motors
     * @param speed raw speed value to run motors at
     */
    public void run(double speed) {
        Logger.recordOutput(name + "/Speed", speed);
        this.motors.set(speed);
    }

    /**
     * @return the beam break object
     */
    public NFRBeamBreak getBeamBreak() {
        return beamBreak;
    }

    /**
     * @return the beam break status (true: intact, false: broken)
     */
    @AutoLogOutput(key="{name}/BeamBreakIntact")
    public boolean getBeamBreakState() {
        return beamBreak.beamIntact();
    }
    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("Motor Speeds", () -> this.motors.get(), speed -> this.motors.set(speed));
    }
    @AutoLogOutput(key="{name}/MotorCurrent")
    public double getMotorCurrent()
    {
        return motors.getOutputCurrent();
    }
}
