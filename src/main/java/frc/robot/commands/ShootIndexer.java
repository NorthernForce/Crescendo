package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Indexer;

public class ShootIndexer extends Command {
    private final Indexer intake;
    private final double speed;

    /** Creates a new RunIntake command
     * @param intake the intake subsystem
     * @param speed raw speed value for intake motors
     */
    public ShootIndexer(Indexer intake, double speed) {
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

