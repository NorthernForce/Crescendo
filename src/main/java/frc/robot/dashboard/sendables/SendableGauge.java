package frc.robot.dashboard.sendables;

import java.util.function.DoubleSupplier;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableValue;

public class SendableGauge implements NFRSendableBase {
    private NetworkTable table;
    private ObjectHolder<Double> value = new ObjectHolder<Double>("value", NetworkTableValue::makeDouble, null);
    private ObjectHolder<Double> min = new ObjectHolder<Double>("min", NetworkTableValue::makeDouble, null);
    private ObjectHolder<Double> max = new ObjectHolder<Double>("max", NetworkTableValue::makeDouble, null);
    private double defaultValue;
    private double defaultMax;
    private double defaultMin;
    private DoubleSupplier valueSupplier;


    public SendableGauge(double defaultValue, double defaultMin, double defaultMax) {
        this.defaultValue = defaultValue;
        this.defaultMax = defaultMax;
        this.defaultMin = defaultMin;
    }

    @Override
    public void close() {
        value.close();
        min.close();
        max.close();
    }

    public void setSupplier(DoubleSupplier valueSupplier) {
        this.valueSupplier = valueSupplier;
    }
    
    public void update() {
        value.setValue(valueSupplier.getAsDouble());
    }

    @Override
    public void initSendable(NTSendableBuilder builder) {
        builder.setSmartDashboardType("Gauge");

        synchronized (this) {
            table = builder.getTable();
            synchronized (value) {
                value.init(table);
                value.setDefault(defaultValue);
            }
            synchronized (min) {
                min.init(table);
                min.setDefault(defaultMin);
            }
            synchronized (max) {
                max.init(table);
                max.setDefault(defaultMax);
            }
        }
    }
    
}
