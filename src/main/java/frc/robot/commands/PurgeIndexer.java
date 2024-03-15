package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;

public class PurgeIndexer extends Command {
    private final Intake intake;
    private final Indexer indexer;
    private final double intakeSpeed;
    private final double indexerSpeed;

    /** Creates a new RunIntake command
     * @param intake the intake subsystem
     * @param speed raw speed value for intake motors
     */
    public PurgeIndexer(Indexer indexer, Intake intake, double intakeSpeed, double indexerSpeed) {
        addRequirements(intake);
        this.intake = intake;
        this.indexer = indexer;
        this.intakeSpeed = intakeSpeed;
        this.indexerSpeed = indexerSpeed;
    }
    @Override
    public void initialize() {
        intake.run(intakeSpeed);
        indexer.run(indexerSpeed);
    }
    @Override
    public void end(boolean interrupted) {
        intake.run(0);
        indexer.run(0);
    }
}

