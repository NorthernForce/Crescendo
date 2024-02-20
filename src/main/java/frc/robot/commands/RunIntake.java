package frc.robot.commands;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;

public class RunIntake extends Command {
    private final DigitalInput beamBreak;
    private final Intake intake;
    private final double speed;

    /** Creates a new RunIntake command
     * @param intake the intake subsystem
     * @param beamBreakPin the digital pin of the beam break used in the intake
     * @param speed raw speed value for intake motors
     */
    public RunIntake(Intake intake, int beamBreakPin, double speed) {
        addRequirements(intake);
        this.intake = intake;
        this.beamBreak = new DigitalInput(beamBreakPin);
        this.speed = speed;
    }
    @Override
    public void initialize() {
        intake.run(speed);
    }
    @Override
    public void end(boolean interrupted) {
        intake.run(0);
    }
    @Override
    public boolean isFinished() {
        // beam break sends zero if it is broken
        return !beamBreak.get();
    }
}
