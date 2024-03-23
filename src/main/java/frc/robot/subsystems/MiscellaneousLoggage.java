package frc.robot.subsystems;

import org.littletonrobotics.junction.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.robots.CrabbyContainer;
import frc.robot.utils.LoggableHardware;

public class MiscellaneousLoggage extends JPanel implements LoggableHardware {
    private CommandXboxController manipulator;
    private CommandXboxController driver;
    private JFrame frame;
    private CrabbyContainer crabby;

    public MiscellaneousLoggage(CrabbyContainer crabbyy) {
        crabby = crabbyy;
        frame = new JFrame("Xbox Controller Visual");
        frame.setSize(300, 200);
        frame.add(this);
        frame.setVisible(true);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image xbocsController = null;
        try {
            xbocsController = ImageIO.read(new File("src\\main\\images\\xboxController.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Draw rectangles for buttons
        g.drawImage(xbocsController, -50, -100, frame);
        g.drawString(
                "(" + MouseInfo.getPointerInfo().getLocation().getX() + ", "
                        + MouseInfo.getPointerInfo().getLocation().getY() + ")",
                (int) MouseInfo.getPointerInfo().getLocation().getX(),
                (int) MouseInfo.getPointerInfo().getLocation().getY() + 20);
        if (driver != null) {
            g.setColor(Color.YELLOW);
            if (driver.b().getAsBoolean()) {
                g.fillOval(290, 95, 25, 25);
            }
            if (driver.a().getAsBoolean()) {
                g.fillOval(270, 115, 25, 25);
            }
            if (driver.x().getAsBoolean()) {
                g.fillOval(250, 95, 25, 25);
            }
            if (driver.povDown().getAsBoolean()) {
                g.fillRect(151, 165, 15, 20);
            }
            if (driver.povUp().getAsBoolean()) {
                g.fillRect(151, 135, 15, 20);
            }
            if (driver.a().getAsBoolean()) {
                g.fillOval(270, 115, 25, 25);
            }
            if (driver.x().getAsBoolean()) {
                g.fillOval(250, 95, 25, 25);
            }
            if (driver.y().getAsBoolean()) {
                g.fillOval(270, 75, 25, 25);
            }
        }
    }

    @Override
    public void startLogging(double period) {
    }

    @Override
    public void logOutputs(String name) {
        frame.repaint();
        // Xbox controllers
        if (driver != null) {
            Logger.recordOutput(name + "/Controllers/Driver/LeftAxis/XboxDriverLeftStickX", driver.getLeftX());
            Logger.recordOutput(name + "/Controllers/Driver/LeftAxis/XboxDriverLeftStickY", driver.getLeftY());
            Logger.recordOutput(name + "/Controllers/Driver/LeftDirection/XboxDriverLeftDirection",
                    Math.acos(driver.getLeftX() * (-Math.abs(driver.getLeftY()) / driver.getLeftY()) + 90));
            Logger.recordOutput(name + "/Controllers/Driver/RightAxis/XboxDriverRightStickX", driver.getRightX());
            Logger.recordOutput(name + "/Controllers/Driver/RightAxis/XboxDriverRightStickY", driver.getRightY());
            Logger.recordOutput(name + "/Controllers/Driver/RightDirection/XboxDriverRightDirection",
                    Math.acos(driver.getRightX() * (-Math.abs(driver.getRightY()) / driver.getRightY()) + 90));
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/A", driver.a().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/B", driver.b().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/X", driver.x().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/Y", driver.y().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/POV/Center", driver.povCenter().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/POV/Up", driver.povUp().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/POV/UpRight", driver.povUpRight().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/POV/Right", driver.povRight().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/POV/DownRight",
                    driver.povDownRight().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/POV/Down", driver.povDown().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/POV/DownLeft", driver.povDownLeft().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/POV/Left", driver.povLeft().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/POV/UpLeft", driver.povUpLeft().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/Triggers/LeftTrigger",
                    driver.leftTrigger().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/Triggers/LeftTriggerAxis",
                    driver.getLeftTriggerAxis());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/Triggers/RightTrigger",
                    driver.rightTrigger().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/Triggers/RightTriggerAxis",
                    driver.getRightTriggerAxis());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/Bumpers/LeftBumper",
                    driver.leftBumper().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/Bumpers/RightBumper",
                    driver.rightBumper().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/Middle/Start", driver.start().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Driver/Buttons/Middle/Back", driver.back().getAsBoolean());
        }
        if (manipulator != null) {
            Logger.recordOutput(name + "/Controllers/Manipulator/LeftAxis/XboxManipulatorLeftStickX",
                    manipulator.getLeftX());
            Logger.recordOutput(name + "/Controllers/Manipulator/LeftAxis/XboxManipulatorLeftStickY",
                    manipulator.getLeftY());
            Logger.recordOutput(name + "/Controllers/Manipulator/LeftDirection/XboxManipulatorLeftDirection", Math
                    .acos(manipulator.getLeftX() * (-Math.abs(manipulator.getLeftY()) / manipulator.getLeftY()) + 90));
            Logger.recordOutput(name + "/Controllers/Manipulator/RightAxis/XboxManipulatorRightStickX",
                    manipulator.getRightX());
            Logger.recordOutput(name + "/Controllers/Manipulator/RightAxis/XboxManipulatorRightStickY",
                    manipulator.getRightY());
            Logger.recordOutput(name + "/Controllers/Manipulator/RightDirection/XboxManipulatorRightDirection",
                    Math.acos(manipulator.getRightX() * (-Math.abs(manipulator.getRightY()) / manipulator.getRightY())
                            + 90));
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/A", manipulator.a().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/B", manipulator.b().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/X", manipulator.x().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/Y", manipulator.y().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/POV/Center",
                    manipulator.povCenter().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/POV/Up", manipulator.povUp().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/POV/UpRight",
                    manipulator.povUpRight().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/POV/Right",
                    manipulator.povRight().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/POV/DownRight",
                    manipulator.povDownRight().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/POV/Down",
                    manipulator.povDown().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/POV/DownLeft",
                    manipulator.povDownLeft().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/POV/Left",
                    manipulator.povLeft().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/POV/UpLeft",
                    manipulator.povUpLeft().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/Triggers/LeftTrigger",
                    manipulator.leftTrigger().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/Triggers/LeftTriggerAxis",
                    manipulator.getLeftTriggerAxis());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/Triggers/RightTrigger",
                    manipulator.rightTrigger().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/Triggers/RightTriggerAxis",
                    manipulator.getRightTriggerAxis());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/Bumpers/LeftBumper",
                    manipulator.leftBumper().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/Bumpers/RightBumper",
                    manipulator.rightBumper().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/Middle/Start",
                    manipulator.start().getAsBoolean());
            Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/Middle/Back",
                    manipulator.back().getAsBoolean());
        } else {
            if (crabby.oi != null) {
                manipulator = crabby.oi.getManipulatorController();
                driver = crabby.oi.getDriverController();
            }
        }

    }

}