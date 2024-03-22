package frc.robot.logging;

import java.util.HashMap;
import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.function.Function;

import org.littletonrobotics.junction.Logger;
import org.northernforce.motors.NFRSparkMax;

import com.revrobotics.CANSparkLowLevel.PeriodicFrame;

public class NFRSparkMaxLogger implements NFRLogger {
    protected final NFRSparkMax sparkMax;
    protected final String name;
    protected final HashMap<String, DoubleSupplier> doubleSignals;

    public enum DataToLog {
        kPosition(spark -> () -> spark.getEncoder().getPosition()),
        kVelocity(spark -> () -> spark.getEncoder().getVelocity()),
        kCurrent(spark -> () -> spark.getOutputCurrent()),
        kVoltage(spark -> () -> spark.getBusVoltage()),
        kTemperature(spark -> () -> spark.getMotorTemperature()),
        kOutputPercent(spark -> () -> spark.getAppliedOutput()),
        kSensorPosition(spark -> () -> spark.getIntegratedEncoder().getPosition()),
        kSensorVelocity(spark -> () -> spark.getIntegratedEncoder().getVelocity());
        private final Function<NFRSparkMax, DoubleSupplier> signalFunction;
        private DataToLog(Function<NFRSparkMax, DoubleSupplier> signalFunction) {
            this.signalFunction = signalFunction;
        }
        public DoubleSupplier getSignal(NFRSparkMax sparkMax) {
            return signalFunction.apply(sparkMax);
        }
    };

    public NFRSparkMaxLogger(String name, NFRSparkMax sparkMax, List<DataToLog> dataToLog) {
        this.sparkMax = sparkMax;
        this.name = name;
        doubleSignals = new HashMap<>();
        for (DataToLog data : dataToLog) {
            doubleSignals.put(data.name().substring(1), data.getSignal(sparkMax));
        }
        sparkMax.setPeriodicFramePeriod(PeriodicFrame.kStatus0, 20);
        sparkMax.setPeriodicFramePeriod(PeriodicFrame.kStatus1, 20);
        sparkMax.setPeriodicFramePeriod(PeriodicFrame.kStatus2, 20);
        sparkMax.setPeriodicFramePeriod(PeriodicFrame.kStatus3, 20);
        sparkMax.setPeriodicFramePeriod(PeriodicFrame.kStatus4, 20);
        sparkMax.setPeriodicFramePeriod(PeriodicFrame.kStatus5, 20);
        sparkMax.setPeriodicFramePeriod(PeriodicFrame.kStatus6, 20);
        sparkMax.setPeriodicFramePeriod(PeriodicFrame.kStatus7, 20);
    }

    @Override
    public void log() {
        for (String key : doubleSignals.keySet()) {
            Logger.recordOutput(name + "/" + key, doubleSignals.get(key).getAsDouble());
        }
    }
}
