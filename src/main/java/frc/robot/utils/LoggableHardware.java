package frc.robot.utils;

public interface LoggableHardware {
    /**
     * Starts the logger for a motor
     */
    public void startLogging(double period);
    /**
     * Updates the outputs
     * @param name where to put the logs (ie. Swerve/Drive1 or Wrist/Motor)
     */
    public void logOutputs(String name);
}
