package frc.robot.logging;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import org.littletonrobotics.junction.Logger;
import org.northernforce.motors.NFRTalonFX;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;

public class NFRTalonFXLogger implements NFRLogger {
    protected final NFRTalonFX talonFX;
    protected final String name;
    protected final HashMap<String, StatusSignal<Double>> doubleSignals;
    public enum DataToLog {
        kPosition(talonFX -> talonFX.getPosition()),
        kVelocity(talonFX -> talonFX.getVelocity()),
        kCurrent(talonFX -> talonFX.getStatorCurrent()),
        kVoltage(talonFX -> talonFX.getMotorVoltage()),
        kTemperature(talonFX -> talonFX.getDeviceTemp()),
        kOutputPercent(talonFX -> talonFX.getDutyCycle()),
        kClosedLoopError(talonFX -> talonFX.getClosedLoopError()),
        kClosedLoopTarget(talonFX -> talonFX.getClosedLoopReference()),
        kSensorPosition(talonFX -> talonFX.getRotorPosition()),
        kSensorVelocity(talonFX -> talonFX.getRotorVelocity());
        private final Function<NFRTalonFX, StatusSignal<Double>> signalFunction;
        private DataToLog(Function<NFRTalonFX, StatusSignal<Double>> signalFunction) {
            this.signalFunction = signalFunction;
        }
        public StatusSignal<Double> getSignal(NFRTalonFX talonFX) {
            return signalFunction.apply(talonFX);
        }
    };
    public NFRTalonFXLogger(String name, NFRTalonFX talonFX, List<DataToLog> dataToLog) {
        this.talonFX = talonFX;
        this.name = name;
        doubleSignals = new HashMap<>();
        for (DataToLog data : dataToLog) {
            doubleSignals.put(data.name().substring(1), data.getSignal(talonFX));
        }
        BaseStatusSignal.setUpdateFrequencyForAll(0.02, doubleSignals.values().toArray(new BaseStatusSignal[0]));
    }
    @Override
    public void log() {
        for (String key : doubleSignals.keySet()) {
            Logger.recordOutput(name + "/" + key, doubleSignals.get(key).getValueAsDouble());
        }
    }
}
