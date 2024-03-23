package frc.robot.dashboard;

import java.util.List;
import java.util.function.BooleanSupplier;

import org.northernforce.commands.NFRSwerveDriveCalibrate;
import org.northernforce.motors.NFRTalonFX;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.ProxyCommand;
import frc.robot.commands.AddDataToTargetingCalculator;
import frc.robot.commands.OrchestraCommand;
import frc.robot.constants.CrabbyConstants;
import frc.robot.robots.CrabbyContainer;
import frc.robot.utils.AutonomousRoutine;

public class ShuffleboardCrabbyDashboard implements CrabbyDashboard {
    public final GenericEntry shooterSpeed;
    public final GenericEntry topRollerChange;
    public final GenericEntry wristAngle;
    protected final SendableChooser<AutonomousRoutine> autoChooser;
    protected BooleanSupplier manualWristSupplier = () -> false;
    public ShuffleboardCrabbyDashboard(CrabbyContainer container) {
        Shuffleboard.getTab("General").add("Calibrate Swerve",
            new NFRSwerveDriveCalibrate(container.drive).ignoringDisable(true));

        Shuffleboard.getTab("General").addDouble("Climber Position", () -> container.climber.getPosition());

        Shuffleboard.getTab("Developer").add("Add Wrist Data", new AddDataToTargetingCalculator(container.angleCalculator,
            () -> container.lastRecordedDistance,  () -> container.wristJoint.getRotation().getRadians()).ignoringDisable(true));
        Shuffleboard.getTab("Developer").addDouble("Degrees of Wrist", () -> container.wristJoint.getRotation().getDegrees());
        Shuffleboard.getTab("General").addBoolean("Manual Wrist Positioning", () -> manualWristSupplier.getAsBoolean());

        Shuffleboard.getTab("General").addDouble("Intake Current", container.intake::getMotorCurrent);

        Shuffleboard.getTab("General").addDouble("Index Current", container.indexer::getMotorCurrent);

        Shuffleboard.getTab("Developer").addDouble("Distance", () -> container.lastRecordedDistance);

        Shuffleboard.getTab("General").addDouble("Top Shooter", container.shooter::getTopMotorVelocity);
        Shuffleboard.getTab("General").addDouble("Bottom Shooter", container.shooter::getBottomMotorVelocity);
        Shuffleboard.getTab("General").addBoolean("At Speed", () -> container.shooter.isAtSpeed(CrabbyConstants.ShooterConstants.tolerance));

        shooterSpeed = Shuffleboard.getTab("Developer").add("Shooter Speed", 30).getEntry();
        topRollerChange = Shuffleboard.getTab("Developer").add("Top Roller Change", 0).getEntry();
        wristAngle = Shuffleboard.getTab("Developer").add("Wrist Angle", 25).getEntry();

        Shuffleboard.getTab("Developer").add("Add Top Shooter Data",
                new AddDataToTargetingCalculator(container.topSpeedCalculator, () -> container.lastRecordedDistance,
                        () -> shooterSpeed.getDouble(30) + topRollerChange.getDouble(0)).ignoringDisable(true));
        Shuffleboard.getTab("Developer").add("Add Bottom Shooter Data",
                new AddDataToTargetingCalculator(container.bottomSpeedCalculator, () -> container.lastRecordedDistance,
                        () -> shooterSpeed.getDouble(30)).ignoringDisable(true));
        
        SendableChooser<String> musicChooser = new SendableChooser<>();
        musicChooser.setDefaultOption("Mr. Blue Sky", "blue-sky.chrp");
        musicChooser.addOption("Crab Rave", "crab-rave.chrp");
        musicChooser.addOption("The Office", "the-office.chrp");
        Shuffleboard.getTab("General").add("Music Selector", musicChooser);
        Shuffleboard.getTab("General").add("Play Music", new ProxyCommand(() -> {
            return new OrchestraCommand(musicChooser.getSelected(), List.of(
                (NFRTalonFX)container.map.modules[0].getDriveController(),
                (NFRTalonFX)container.map.modules[0].getTurnController(),
                (NFRTalonFX)container.map.modules[1].getDriveController(),
                (NFRTalonFX)container.map.modules[1].getTurnController(),
                (NFRTalonFX)container.map.modules[2].getDriveController(),
                (NFRTalonFX)container.map.modules[2].getTurnController(),
                (NFRTalonFX)container.map.modules[3].getDriveController(),
                (NFRTalonFX)container.map.modules[3].getTurnController()), container.drive, container.map.modules[0], container.map.modules[1],
                    container.map.modules[2], container.map.modules[3])
                .ignoringDisable(true);
        }));

        autoChooser = new SendableChooser<>();
    }
    @Override
    public void periodic() {
    }
    @Override
    public void displayAutonomousRoutines(List<AutonomousRoutine> routines) {
        if (routines.size() != 0)
        {
            autoChooser.addOption(routines.get(0).name(), routines.get(0));
        }
        for (int i = 1; i < routines.size(); i++)
        {
            autoChooser.addOption(routines.get(i).name(), routines.get(i));
        }
        Shuffleboard.getTab("General").add("Autonomous Selector", autoChooser);
    }
    @Override
    public AutonomousRoutine getSelectedRoutine() {
        return autoChooser.getSelected();
    }
    @Override
    public double getShooterSpeed() {
        return shooterSpeed.getDouble(0);
    }
    @Override
    public double getTopRollerChange() {
        return topRollerChange.getDouble(0);
    }
    @Override
    public Rotation2d getTargetWristAngle() {
        return Rotation2d.fromDegrees(wristAngle.getDouble(25));
    }
    @Override
    public void setWristManual(BooleanSupplier supplier) {
        manualWristSupplier = supplier;
    }
}
