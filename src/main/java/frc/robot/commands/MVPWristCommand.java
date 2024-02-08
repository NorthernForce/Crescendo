package frc.robot.commands;

import org.northernforce.motors.NFRTalonFX;

import com.ctre.phoenix6.hardware.CANcoder;

import edu.wpi.first.wpilibj2.command.Command;

public class MVPWristCommand extends Command{
    private final double m_percent;
    private final NFRTalonFX m_motor;
    private final double m_degrees;
    private final CANcoder m_can;

    public MVPWristCommand(double speed, double degrees, NFRTalonFX motor, CANcoder can){
        m_percent = speed;
        m_motor = motor;
        m_degrees = degrees;
        m_can = can;
    }

    @Override public void execute(){
        m_motor.set(m_percent);
    }

    @Override public void initialize(){

    }

    @Override public void end(boolean interrupted){
        m_motor.set(0);
    }

    @Override public boolean isFinished(){
        return m_can.getPosition().getValue() >= m_degrees;
    }

}