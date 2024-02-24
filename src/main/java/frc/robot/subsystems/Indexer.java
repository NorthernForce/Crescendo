package frc.robot.subsystems;

import org.northernforce.motors.NFRSparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.sensors.NFRBeamBreak;

public class Indexer extends SubsystemBase {
    private NFRSparkMax indexerMotor; //Neo that runs indexer
    private NFRBeamBreak beamBreak; //Beam break sensor to stop note intake

    /**
     * Wrapper class for SparkMax and beam break.
     * Handles intaking and shooting of indexer
     */
    public Indexer(NFRSparkMax motor, NFRBeamBreak beamBreak){
        indexerMotor = motor;
        this.beamBreak = beamBreak;
    }

    /**
     * Starts the motor
     * @param speed Speed to start the motor at
     */
    public void startMotor(double speed){
        indexerMotor.set(speed);
    }

    /**
     * Stops the motor
     */
    public void stopMotor(){
        indexerMotor.set(0);
    }

    /**
     * Returns the state of the beam break
     * @return Beam break state (true: intact, false: broken)
     */
    public boolean getBeamBreakState(){
        return beamBreak.beamIntact();
    }

    /**
     * Returns the beam break object
     * @return Beam break object
     */
    public NFRBeamBreak getBeamBreak(){
        return beamBreak;
    }
}
