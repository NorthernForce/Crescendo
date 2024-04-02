package frc.robot.subsystems;

import org.littletonrobotics.junction.Logger;
import org.northernforce.motors.NFRSparkMax;

import com.revrobotics.CANSparkBase.FaultID;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.LoggableHardware;

public class Climber extends SubsystemBase implements LoggableHardware {
    private double offset = 0;
    private NFRSparkMax motor;
    private DigitalInput m_digitalInput;
    public Climber(NFRSparkMax motor){
        this.motor = motor;
        motor.disablePositiveLimit();
        motor.disableNegativeLimit();
        offset = motor.getEncoder().getPosition();
        m_digitalInput = new DigitalInput(6);
    }

    public void startMotor(double speed){
        motor.set(speed);
    }

    public void stopMotor(){
        motor.set(0);
    }

    public void setPosition(double position)
    {
        motor.setPositionTrapezoidal(0, position);
    }

    public boolean isDown() {
        return m_digitalInput.get();
    }

    public boolean isAtBottomSoftLimit()
    {
        return motor.getFault(FaultID.kSoftLimitRev);
    }
    
    public boolean isAtTopSoftLimit()
    {
        return motor.getFault(FaultID.kSoftLimitFwd);
    }

    public double getPosition()
    {
        return motor.getEncoder().getPosition() - offset;
    }

    @Override
    public void startLogging(double period) {
    }

    @Override
    public void logOutputs(String name) {
        Logger.recordOutput(name + "/Position", getPosition());
        Logger.recordOutput(name + "/TopSoftLimit", isAtTopSoftLimit());
        Logger.recordOutput(name + "/BottomSoftLimit", isAtBottomSoftLimit());
    }
}
