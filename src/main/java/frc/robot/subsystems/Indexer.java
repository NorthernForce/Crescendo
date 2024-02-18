package frc.robot.subsystems;

import org.northernforce.motors.NFRSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Indexer extends SubsystemBase {
    private NFRSparkMax indexerMotor; //Neo that runs indexer
    private DigitalInput beamBreak; //Beam break sensor to stop note intake

    /**
     * Wrapper class for SparkMax and beam break.
     * Handles intaking and shooting of indexer
     */
    public Indexer(){
        indexerMotor = new NFRSparkMax(MotorType.kBrushless, 0); //TODO: Find primaryID
        beamBreak = new DigitalInput(0); //TODO: Find channel
    }

    /**
     * Intake method that has internal interation
     */
    public void intake(){
        while(beamBreak.get()){
            indexerMotor.set(1);
        }
        indexerMotor.set(0);
    }

    /**
     * Outtake method that has internal iteration
     */
    public void shoot(){
        while(!beamBreak.get()){
            indexerMotor.set(1);
        }
        indexerMotor.set(0);
    }
}
