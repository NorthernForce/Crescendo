package frc.robot.subsystems;

import org.northernforce.motors.NFRSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Indexer extends SubsystemBase {
    private NFRSparkMax indexerMotor;

    public Indexer(){
        indexerMotor = new NFRSparkMax(MotorType.kBrushless, 0); //TODO
    }

    public void setSpeed(double speed){
        indexerMotor.set(speed);
    }
}
