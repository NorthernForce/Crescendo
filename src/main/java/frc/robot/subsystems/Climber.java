package frc.robot.subsystems;

import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;
import org.northernforce.motors.NFRSparkMax;

import com.revrobotics.CANSparkBase.FaultID;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
    private double offset = 0;
    private NFRSparkMax motor;
    private DigitalInput m_digitalInput;
    private final String name;
    public Climber(NFRSparkMax motor){
        this.motor = motor;
        motor.disablePositiveLimit();
        motor.disableNegativeLimit();
        offset = motor.getEncoder().getPosition();
        m_digitalInput = new DigitalInput(6);
        name = getName();
    }

    public void startMotor(double speed){
        Logger.recordOutput(name + "/Speed", speed);
        motor.set(speed);
    }

    public void stopMotor(){
        Logger.recordOutput(name + "/Speed", 0);
        motor.set(0);
    }

    public void setPosition(double position)
    {
        motor.setPositionTrapezoidal(0, position);
    }

    @AutoLogOutput(key = "{name}/IsDown")
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

    @AutoLogOutput(key = "{name}/CurrentDraw")
    public double getCurrentDraw() {
        return motor.getOutputCurrent();
    }

    @AutoLogOutput(key = "{name}/Position")
    public double getPosition()
    {
        return motor.getEncoder().getPosition() - offset;
    }
}
