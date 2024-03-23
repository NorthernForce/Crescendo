package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;

public class ShootIndexerAndIntake extends Command {
    private final Indexer indexer;
    private final Intake intake;
    private final double indexerSpeed;
    private final double intakeSpeed;

    /** Creates a new RunIntake command
     * @param intake the intake subsystem
     * @param speed raw speed value for intake motors
     */
    public ShootIndexerAndIntake(Indexer indexer, Intake intake, double indexerSpeed, double intakeSpeed) {
        addRequirements(indexer, intake);
        this.intake = intake;
        this.indexer = indexer;
        this.indexerSpeed = indexerSpeed;
        this.intakeSpeed = intakeSpeed;
    }
    @Override
    public void execute() {
        indexer.run(indexerSpeed);
        intake.run(intakeSpeed);
    }
    @Override
    public void end(boolean interrupted) {
        indexer.run(0);
        intake.run(0);
    }
    @Override
    public boolean isFinished(){
        return indexer.getBeamBreak().beamIntact();
    }
}

