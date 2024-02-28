package frc.robot.subsystems;

import org.northernforce.motors.NFRSparkMax;

import frc.robot.sensors.NFRBeamBreak;

public class Climber {
    private NFRSparkMax motor;
    private NFRBeamBreak beamBreak;
    public Climber(NFRSparkMax motor, NFRBeamBreak beamBreak){
        this.motor = motor;
        this.beamBreak = beamBreak;
    }

    public void startMotor(double speed){
        motor.set(speed);
    }

    public void stopMotor(){
        motor.set(0);
    }

    public NFRBeamBreak getBeamBreak(){
        return beamBreak;
    }

    public boolean getBeamBreakState(){
        return beamBreak.beamIntact();
    }

    public double getMotorCurrent(){
        return motor.getOutputCurrent();
    }
}
