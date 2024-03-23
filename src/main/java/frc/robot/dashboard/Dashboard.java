package frc.robot.dashboard;

import java.util.List;

import frc.robot.utils.AutonomousRoutine;

public interface Dashboard {
    public void displayAutonomousRoutines(List<AutonomousRoutine> autoChooser);
    public void periodic();
    public AutonomousRoutine getSelectedRoutine();
}
