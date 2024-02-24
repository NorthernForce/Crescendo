package frc.robot.commands;

import java.util.List;

import org.northernforce.motors.NFRTalonFX;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class OrchestraCommand extends Command {
    protected final List<NFRTalonFX> talons;
    protected final Orchestra orchestra;
    /**
     * Plays on orchestra on talons
     * @param filepath the path to the chrp file
     * @param talons the talons to control
     * @param requirements the subsytsems to require
     */
    public OrchestraCommand(String filepath, List<NFRTalonFX> talons, SubsystemBase... requirements)
    {
        addRequirements(requirements);
        this.talons = talons;
        this.orchestra = new Orchestra(filepath);
    }
    @Override
    public void initialize()
    {
        for (var talon : talons)
        {
            for (var controller : talon.getMotorControllers())
            {
                orchestra.addInstrument((TalonFX)controller);
            }
        }
        orchestra.play();
    }
    @Override
    public void end(boolean interrupted)
    {
        orchestra.stop();
        orchestra.clearInstruments();
    }
    @Override
    public boolean isFinished()
    {
        return !orchestra.isPlaying();
    }
}
