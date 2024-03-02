package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;

public class ShootIntake extends Command {
    private final Intake intake;
    private final double speed;

    /** Creates a new RunIntake command
     * @param intake the intake subsystem
     * @param speed raw speed value for intake motors
     */
    public ShootIntake(Intake intake, double speed) {
        addRequirements(intake);
        this.intake = intake;
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
    public boolean isFinished(){
        return intake.getBeamBreak().beamIntact();
    }
}

