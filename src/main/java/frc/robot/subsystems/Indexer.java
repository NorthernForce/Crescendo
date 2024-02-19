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
    public Indexer(int motorId, int beamBreakPin){
        indexerMotor = new NFRSparkMax(MotorType.kBrushless, motorId); //TODO: Find primaryID
        beamBreak = new NFRBeamBreak(beamBreakPin); //TODO: Find channel
    }

    /**
     * Intake method that has internal interation
     */
    public void startMotor(){
        indexerMotor.set(0.25);
    }

    /**
     * Outtake method that has internal iteration
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
