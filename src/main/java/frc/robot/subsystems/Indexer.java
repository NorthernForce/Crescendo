package frc.robot.subsystems;

import org.northernforce.motors.NFRSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.sensors.NFRBeamBreak;

public class Indexer extends SubsystemBase {
    private NFRSparkMax indexerMotor; //Neo that runs indexer
    private NFRBeamBreak beamBreak; //Beam break sensor to stop note intake

    /**
     * Wrapper class for SparkMax and beam break.
     * Handles intaking and shooting of indexer
     */
    public Indexer(int neoNum, int beamBreakPin){
        indexerMotor = new NFRSparkMax(MotorType.kBrushless, neoNum); //TODO: Find primaryID
        beamBreak = new NFRBeamBreak(beamBreakPin); //TODO: Find channel
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
        System.out.println("Motor stopped");
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
