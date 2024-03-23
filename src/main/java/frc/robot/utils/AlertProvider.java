package frc.robot.utils;

import java.util.List;

import frc.robot.dashboard.FWCDashboard.Alert;

public interface AlertProvider {
    /**
     * Returns a list of alerts that can be checked from time to time to see whether they are valid or not
     * @return a list of alerts that may need to be displayed on a dashboard
     */
    public List<Alert> getPossibleAlerts();
}
