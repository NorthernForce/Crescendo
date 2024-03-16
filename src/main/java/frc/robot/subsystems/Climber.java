package frc.robot.subsystems;

import org.northernforce.motors.NFRSparkMax;

import com.revrobotics.CANSparkBase.FaultID;
import com.revrobotics.CANSparkBase.SoftLimitDirection;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.CrabbyConstants;

public class Climber extends SubsystemBase {
    private NFRSparkMax motor;
    public Climber(NFRSparkMax motor){
        this.motor = motor;
        motor.setSoftLimit(SoftLimitDirection.kReverse, 0);
        motor.setSoftLimit(SoftLimitDirection.kForward, (float)CrabbyConstants.ClimberConstants.climberLimit);
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

    public boolean isAtBottomSoftLimit()
    {
        return motor.getFault(FaultID.kSoftLimitRev);
    }
    
    public boolean isAtTopSoftLimit()
    {
        return motor.getFault(FaultID.kSoftLimitFwd);
    }
}
