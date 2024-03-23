package frc.robot.motors;

import java.util.HashMap;

import org.littletonrobotics.junction.Logger;
import org.northernforce.motors.NFRTalonFX;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

import frc.robot.utils.LoggableHardware;

public class LoggableTalonFX extends NFRTalonFX implements LoggableHardware {
    protected final HashMap<String, StatusSignal<Double>> doubleSignals;
    protected final HashMap<String, StatusSignal<Boolean>> booleanSignals;
    /**
     * Creates a new NFRTalon FX instance that contains a list of followers.
     * @param config The TalonFXConfiguration
     * @param primaryID the canbus id of the primary motor
     * @param followerIDs the canbus ids of the follower motors
     */
    public LoggableTalonFX(TalonFXConfiguration config, int primaryID, int... followerIDs)
    {
        super(config, primaryID, followerIDs);
        doubleSignals = new HashMap<>();
        booleanSignals = new HashMap<>();
    }
    /**
     * Creates a new NFRTalon FX instance that contains a list of followers.
     * @param config The TalonFXConfiguration
     * @param primaryID the canbus id of the primary motor
     */
    public LoggableTalonFX(TalonFXConfiguration config, int primaryID)
    {
        super(config, primaryID);
        doubleSignals = new HashMap<>();
        booleanSignals = new HashMap<>();
    }
    /**
     * Creates a new NFRTalon FX instance that contains a list of followers.
     * @param canbus the canbus name to find the talonFX
     * @param config The TalonFXConfiguration
     * @param primaryID the canbus id of the primary motor
     * @param followerIDs the canbus ids of the follower motors
     */
    public LoggableTalonFX(String canbus, TalonFXConfiguration config, int primaryID, int... followerIDs)
    {
        super(canbus, config, primaryID, followerIDs);
        doubleSignals = new HashMap<>();
        booleanSignals = new HashMap<>();
    }
    /**
     * Creates a new NFRTalon FX instance that contains a list of followers.
     * @param canbus the canbus name to find the talonFX
     * @param config The TalonFXConfiguration
     * @param primaryID the canbus id of the primary motor
     */
    public LoggableTalonFX(String canbus, TalonFXConfiguration config, int primaryID)
    {
        super(canbus, config, primaryID);
        doubleSignals = new HashMap<>();
        booleanSignals = new HashMap<>();
    }
    protected void initLogging()
    {
        doubleSignals.put("DeviceTemperature", getDeviceTemp());
        doubleSignals.put("ProcessorTemperature", getProcessorTemp());
        doubleSignals.put("Acceleration", getAcceleration());
        doubleSignals.put("Velocity", getVelocity());
        doubleSignals.put("Position", getPosition());
        doubleSignals.put("RotorVelocity", getRotorVelocity());
        doubleSignals.put("RotorPosition", getRotorPosition());
        doubleSignals.put("ClosedLoopError", getClosedLoopError());
        doubleSignals.put("ClosedLoopOutput", getClosedLoopOutput());
        doubleSignals.put("ClosedLoopReference", getClosedLoopReference());
        doubleSignals.put("DutyCycle", getDutyCycle());
        doubleSignals.put("MotorVoltage", getMotorVoltage());
        doubleSignals.put("StatorCurrent", getStatorCurrent());
        doubleSignals.put("SupplyVoltage", getSupplyVoltage());
        doubleSignals.put("SupplyCurrent", getSupplyCurrent());
        booleanSignals.put("FaultBootDuringEnable", getFault_BootDuringEnable());
        booleanSignals.put("FaultBridgeBrownout", getFault_BridgeBrownout());
        booleanSignals.put("FaultDeviceTemperature", getFault_DeviceTemp());
        booleanSignals.put("FaultForwardSoftLimit", getFault_ForwardSoftLimit());
        booleanSignals.put("FaultReverseSoftLimit", getFault_ReverseSoftLimit());
        booleanSignals.put("FaultCurrentLimit", getFault_StatorCurrLimit());
    }
    @Override
    public void startLogging(double period)
    {
        StatusSignal.setUpdateFrequencyForAll(1 / period, doubleSignals.values().toArray(new BaseStatusSignal[doubleSignals.size()]));
        StatusSignal.setUpdateFrequencyForAll(1 / period, booleanSignals.values().toArray(new BaseStatusSignal[booleanSignals.size()]));
    }
    /**
     * Updates the advantage scope logs for this motor
     * @param name to publish motor information under
     */
    @Override
    public void logOutputs(String name)
    {
        for (var signal : doubleSignals.entrySet())
        {
            Logger.recordOutput(name + "/" + signal.getKey(), signal.getValue().getValue());
        }
        for (var signal : booleanSignals.entrySet())
        {
            Logger.recordOutput(name + "/" + signal.getKey(), signal.getValue().getValue());
        }
    }
}
