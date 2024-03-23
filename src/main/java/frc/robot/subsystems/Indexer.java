package frc.robot.subsystems;

import org.littletonrobotics.junction.Logger;
import org.northernforce.motors.NFRSparkMax;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.CrabbyConstants;
import frc.robot.sensors.NFRBeamBreak;
import frc.robot.utils.LoggableHardware;

public class Indexer extends SubsystemBase implements LoggableHardware {
    private final NFRSparkMax motors;
    private final NFRBeamBreak beamBreak;
    /** Creates a new Intake subsystem
     * @param motors the motor of the intake
     * @param beamBreak the beam break
    */
    public Indexer(NFRSparkMax motors, NFRBeamBreak beamBreak) {
        this.motors = motors;
        this.beamBreak = beamBreak;
    }
    /**
     * Runs the intake motors
     * @param speed raw speed value to run motors at
     */
    public void run(double speed) {
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
    public boolean getBeamBreakState() {
        return beamBreak.beamIntact();
    }
    public boolean hasPiece()
    {
        return beamBreak.beamBroken();
    }
    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("Motor Speeds", () -> this.motors.get(), speed -> this.motors.set(speed));
    }
    public double getMotorCurrent()
    {
        return motors.getOutputCurrent();
    }
    @Override
    public void startLogging(double period) {
    }
    @Override
    public void logOutputs(String name) {
        Logger.recordOutput(name + "/HasPiece", hasPiece());
        Logger.recordOutput(name + "/Speed", motors.get());
        Logger.recordOutput(name + "/TargetSpeed", CrabbyConstants.IndexerConstants.indexerSpeed);
        Logger.recordOutput(name + "/TargetPurgeSpeed", CrabbyConstants.IndexerConstants.indexerPurgeSpeed);
        Logger.recordOutput(name + "/TargetShootSpeed", CrabbyConstants.IndexerConstants.indexerShootSpeed);
    }
}
