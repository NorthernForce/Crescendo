package frc.robot.commands;

import org.northernforce.motors.NFRTalonFX;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.signals.AbsoluteSensorRangeValue;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;

import edu.wpi.first.wpilibj2.command.Command;

public class MVPWristCommand extends Command{
    private final double m_percent;
    private final NFRTalonFX m_motor;
    private final double m_degrees;
    private final CANcoder m_can;

    private double m_startDegree;
    private int m_negPos;
    private double m_finalDegree;
    private double m_turned;
    private double m_prevPos;
    /**
     * 
     * @param speed speed of motor (-1 to 1)
     * @param degrees amount of degrees to turn
     * @param motor motor object (TalonFX)
     * @param can CANcoder for motor
     */
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
        CANcoderConfiguration cc_cfg = new CANcoderConfiguration();
        cc_cfg.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Signed_PlusMinusHalf;
        cc_cfg.MagnetSensor.SensorDirection = SensorDirectionValue.CounterClockwise_Positive;
        cc_cfg.MagnetSensor.MagnetOffset = 0.4;
        m_can.getConfigurator().apply(cc_cfg);

        TalonFXConfiguration fx_cfg = new TalonFXConfiguration();
        fx_cfg.Feedback.FeedbackRemoteSensorID = m_can.getDeviceID();
        fx_cfg.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.FusedCANcoder;
        fx_cfg.Feedback.SensorToMechanismRatio = 1.0;
        fx_cfg.Feedback.RotorToSensorRatio = 12.8;
        m_motor.getConfigurator().apply(fx_cfg);
        m_startDegree = m_can.getPosition().getValueAsDouble();

        
        m_negPos =(int)(Math.abs(m_degrees)/m_degrees);
        m_finalDegree = m_startDegree+m_degrees;
    }

    @Override public void end(boolean interrupted){
        m_motor.set(0);
    }

    @Override public boolean isFinished(){

        // gets the amount turned since start of command to acount for the 360 degrees
        setTurned();
        if(m_negPos == 1){
            
          return m_startDegree+m_turned >= m_finalDegree;  
        } else {
            return m_startDegree-m_turned <=m_finalDegree;
        }
        
    }

    public void setTurned(){
    // if the degrees suddenly went over 360 and flowed over to a lesser degree (ex. if motor was at 300 and changed by 70, you get 10), then the correct change must be recorded. I am awful at writing comments.    
    if(m_can.getPosition().getValueAsDouble() >m_prevPos){
        m_turned+=(m_can.getPosition().getValueAsDouble()-m_prevPos);
        m_prevPos = m_can.getPosition().getValueAsDouble();
    } else {
        m_turned+=((360-m_prevPos)+m_can.getPosition().getValueAsDouble());
        m_prevPos = m_can.getPosition().getValueAsDouble();
    }
    }

}