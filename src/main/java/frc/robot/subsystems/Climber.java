package frc.robot.subsystems;

import org.northernforce.motors.NFRSparkMax;

public class Climber {
    private NFRSparkMax motor;
    public Climber(NFRSparkMax motor){
        this.motor = motor;
    }

    public void startMotor(double speed){
        motor.set(speed);
    }

    public void stopMotor(){
        motor.set(0);
    }
}
