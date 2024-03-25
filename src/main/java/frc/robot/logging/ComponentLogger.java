package frc.robot.logging;

import java.util.HashMap;

import org.littletonrobotics.junction.Logger;
import org.northernforce.motors.NFRSparkMax;
import org.northernforce.motors.NFRTalonFX;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.CANSparkBase.FaultID;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.robots.CrabbyContainer;
import frc.robot.utils.LoggableHardware;

public class ComponentLogger implements LoggableHardware {
    private TalonFXLogger[] m_talons;
    private SparkLogger[] m_sparks;
    private ComponentLogger[] m_componentLoggers;
    private ComponentType type;
    private ControllerLogger m_cLogger;

    public ComponentLogger(NFRTalonFX... talons) {
        type = ComponentType.Talon;
        m_talons = new TalonFXLogger[talons.length];
        for (int i = 0; i < talons.length; i++) {
            m_talons[i] = new TalonFXLogger(talons[i]);
        }
    }

    public ComponentLogger(NFRSparkMax... sparks) {
        type = ComponentType.SparkMax;
        m_sparks = new SparkLogger[sparks.length];
        for (int i = 0; i < sparks.length; i++) {
            m_sparks[i] = new SparkLogger(sparks[i]);
        }
    }

    public ComponentLogger(CrabbyContainer crabby) {
        type = ComponentType.ControllerLogger;
        m_cLogger = new ControllerLogger(crabby);
    }
    public ComponentLogger(ComponentLogger... componentLoggers) {
        type = ComponentType.ComponentLogger;
        m_componentLoggers = componentLoggers;
    }

    public class TalonFXLogger implements LoggableHardware {
        private TalonFX m_motor;
        protected final HashMap<String, StatusSignal<Double>> doubleSignals;
        protected final HashMap<String, StatusSignal<Boolean>> booleanSignals;

        public TalonFXLogger(NFRTalonFX motor) {
            m_motor = motor;
            doubleSignals = new HashMap<>();
            booleanSignals = new HashMap<>();
        }

        protected void initLogging() {
            doubleSignals.put("DeviceTemperature", m_motor.getDeviceTemp());
            doubleSignals.put("ProcessorTemperature", m_motor.getProcessorTemp());
            doubleSignals.put("Acceleration", m_motor.getAcceleration());
            doubleSignals.put("Velocity", m_motor.getVelocity());
            doubleSignals.put("Position", m_motor.getPosition());
            doubleSignals.put("RotorVelocity", m_motor.getRotorVelocity());
            doubleSignals.put("RotorPosition", m_motor.getRotorPosition());
            doubleSignals.put("ClosedLoopError", m_motor.getClosedLoopError());
            doubleSignals.put("ClosedLoopOutput", m_motor.getClosedLoopOutput());
            doubleSignals.put("ClosedLoopReference", m_motor.getClosedLoopReference());
            doubleSignals.put("DutyCycle", m_motor.getDutyCycle());
            doubleSignals.put("MotorVoltage", m_motor.getMotorVoltage());
            doubleSignals.put("StatorCurrent", m_motor.getStatorCurrent());
            doubleSignals.put("SupplyVoltage", m_motor.getSupplyVoltage());
            doubleSignals.put("SupplyCurrent", m_motor.getSupplyCurrent());
            booleanSignals.put("FaultBootDuringEnable", m_motor.getFault_BootDuringEnable());
            booleanSignals.put("FaultBridgeBrownout", m_motor.getFault_BridgeBrownout());
            booleanSignals.put("FaultDeviceTemperature", m_motor.getFault_DeviceTemp());
            booleanSignals.put("FaultForwardSoftLimit", m_motor.getFault_ForwardSoftLimit());
            booleanSignals.put("FaultReverseSoftLimit", m_motor.getFault_ReverseSoftLimit());
            booleanSignals.put("FaultCurrentLimit", m_motor.getFault_StatorCurrLimit());
        }

        @Override
        public void startLogging(double period) {
            StatusSignal.setUpdateFrequencyForAll(1 / period,
                    doubleSignals.values().toArray(new BaseStatusSignal[doubleSignals.size()]));
            StatusSignal.setUpdateFrequencyForAll(1 / period,
                    booleanSignals.values().toArray(new BaseStatusSignal[booleanSignals.size()]));
        }

        /**
         * Updates the advantage scope logs for this motor
         * 
         * @param name to publish motor information under
         */
        @Override
        public void logOutputs(String name) {
            for (var signal : doubleSignals.entrySet()) {
                Logger.recordOutput(name + "/" + signal.getKey(), signal.getValue().getValue());
            }
            for (var signal : booleanSignals.entrySet()) {
                Logger.recordOutput(name + "/" + signal.getKey(), signal.getValue().getValue());
            }
        }

    }

    public class SparkLogger implements LoggableHardware {
        private NFRSparkMax m_motor;

        public SparkLogger(NFRSparkMax motor) {
            m_motor = motor;
        }

        @Override
        public void startLogging(double period) {
        }

        @Override
        public void logOutputs(String name) {
            Logger.recordOutput(name + "/Velocity", m_motor.getSelectedEncoder().getVelocity());
            Logger.recordOutput(name + "/Position", m_motor.getSelectedEncoder().getPosition());
            Logger.recordOutput(name + "/IntegratedVelocity", m_motor.getIntegratedEncoder().getVelocity());
            Logger.recordOutput(name + "/IntegratedPosition", m_motor.getIntegratedEncoder().getPosition());
            if (m_motor.getAbsoluteEncoder().isPresent()) {
                Logger.recordOutput(name + "/AbsolutePosition",
                        m_motor.getAbsoluteEncoder().get().getAbsolutePosition());
                Logger.recordOutput(name + "/AbsoluteOffset", m_motor.getAbsoluteEncoder().get().getAbsoluteOffset());
                Logger.recordOutput(name + "/AbsoluteVelocity",
                        m_motor.getAbsoluteEncoder().get().getAbsoluteVelocity());
            }
            Logger.recordOutput(name + "/Temperature", m_motor.getMotorTemperature());
            Logger.recordOutput(name + "/OutputCurrent", m_motor.getOutputCurrent());
            Logger.recordOutput(name + "/ForwardSoftLimit", m_motor.getFault(FaultID.kSoftLimitFwd));
            Logger.recordOutput(name + "/ReverseSoftLimit", m_motor.getFault(FaultID.kSoftLimitRev));
            Logger.recordOutput(name + "/SensorFault", m_motor.getFault(FaultID.kSensorFault));
        }

    }

    public class ControllerLogger /* extends JPanel */ implements LoggableHardware {
        private CommandXboxController manipulator;
        private CommandXboxController driver;
        private CrabbyContainer crabby;

        public ControllerLogger(CrabbyContainer crabbyy) {
            crabby = crabbyy;
        }

        @Override
        public void startLogging(double period) {
        }

        @Override
        public void logOutputs(String name) {
            // frame.repaint();
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
                Logger.recordOutput(name + "/Controllers/Driver/Buttons/POV/UpRight",
                        driver.povUpRight().getAsBoolean());
                Logger.recordOutput(name + "/Controllers/Driver/Buttons/POV/Right", driver.povRight().getAsBoolean());
                Logger.recordOutput(name + "/Controllers/Driver/Buttons/POV/DownRight",
                        driver.povDownRight().getAsBoolean());
                Logger.recordOutput(name + "/Controllers/Driver/Buttons/POV/Down", driver.povDown().getAsBoolean());
                Logger.recordOutput(name + "/Controllers/Driver/Buttons/POV/DownLeft",
                        driver.povDownLeft().getAsBoolean());
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
                        .acos(manipulator.getLeftX() * (-Math.abs(manipulator.getLeftY()) / manipulator.getLeftY())
                                + 90));
                Logger.recordOutput(name + "/Controllers/Manipulator/RightAxis/XboxManipulatorRightStickX",
                        manipulator.getRightX());
                Logger.recordOutput(name + "/Controllers/Manipulator/RightAxis/XboxManipulatorRightStickY",
                        manipulator.getRightY());
                Logger.recordOutput(name + "/Controllers/Manipulator/RightDirection/XboxManipulatorRightDirection",
                        Math.acos(
                                manipulator.getRightX() * (-Math.abs(manipulator.getRightY()) / manipulator.getRightY())
                                        + 90));
                Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/A", manipulator.a().getAsBoolean());
                Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/B", manipulator.b().getAsBoolean());
                Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/X", manipulator.x().getAsBoolean());
                Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/Y", manipulator.y().getAsBoolean());
                Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/POV/Center",
                        manipulator.povCenter().getAsBoolean());
                Logger.recordOutput(name + "/Controllers/Manipulator/Buttons/POV/Up",
                        manipulator.povUp().getAsBoolean());
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

    @Override
    public void startLogging(double period) {

    }

    @Override
    public void logOutputs(String name) {
        switch (type) {
            case Talon:
                for (TalonFXLogger i : m_talons) {
                    i.logOutputs(name + "/TalonFX(s)");
                }
            case SparkMax:
                for (SparkLogger i : m_sparks) {
                    i.logOutputs(name + "/SparkMax(s)");
                }
            case ComponentLogger:
                for (ComponentLogger i : m_componentLoggers) {
                    i.logOutputs("Components");
                }
            case ControllerLogger:
                m_cLogger.logOutputs(name);

            default:
                System.out.println("Type not found, add " + type + " to ComponentLogger");

        }
    }

    public enum ComponentType {
        Talon,
        SparkMax,
        ComponentLogger,
        ControllerLogger
    }

}
