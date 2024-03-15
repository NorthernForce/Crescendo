package frc.robot.dashboard.sendables;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableValue;

public class SendableNumberSlider implements NTSendable, AutoCloseable {
    private NetworkTable table;
    private ObjectHolder<Double> value = new ObjectHolder<Double>("value", NetworkTableValue::makeDouble, ObjectHolder::getDouble);
    private ObjectHolder<Double> min = new ObjectHolder<Double>("min", NetworkTableValue::makeDouble, null);
    private ObjectHolder<Double> max = new ObjectHolder<Double>("max", NetworkTableValue::makeDouble, null);
    private double defaultValue;
    private double defaultMax;
    private double defaultMin;


    public SendableNumberSlider(double defaultValue, double defaultMin, double defaultMax) {
        this.defaultValue = defaultValue;
        this.defaultMax = defaultMax;
        this.defaultMin = defaultMin;
    }

    public synchronized double getValue() {
        return value.getValue();
    }

    @Override
    public void close() {
        value.close();
        min.close();
        max.close();
    }

    @Override
    public void initSendable(NTSendableBuilder builder) {
        builder.setSmartDashboardType("Number Slider");

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
