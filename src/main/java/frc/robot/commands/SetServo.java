package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.Command;

public class SetServo extends Command {
    protected final Servo servo;
    protected final Rotation2d angle;
    public SetServo(Servo servo, Rotation2d angle)
    {
        this.servo = servo;
        this.angle = angle;
    }
    @Override
    public void initialize() 
    {
        servo.setAngle(angle.getDegrees());

    }
}
