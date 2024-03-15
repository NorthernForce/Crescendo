package frc.robot.dashboard.sendables;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableValue;

public class SendablePDH implements NFRSendableBase {
    private NetworkTable table;
    private ObjectHolder<Double> voltage = new ObjectHolder<Double>("voltage", NetworkTableValue::makeDouble, ObjectHolder::getDouble);
    private ObjectHolder<Double> totalCurrent = new ObjectHolder<Double>("totalCurrent", NetworkTableValue::makeDouble, ObjectHolder::getDouble);
    private ObjectHolder<double[]> channels = new ObjectHolder<double[]>("channels", NetworkTableValue::makeDoubleArray, ObjectHolder::getDoubleArray);

    @Override
    public void close() {
        voltage.close();
        totalCurrent.close();
    }

    @Override
    public void initSendable(NTSendableBuilder builder) {
        builder.setSmartDashboardType("PDH");

        synchronized (this) {
            table = builder.getTable();
            synchronized (voltage) {
                voltage.init(table);
                voltage.setDefault(0.0);
            }
            synchronized (totalCurrent) {
                totalCurrent.init(table);
                totalCurrent.setDefault(0.0);
            }
            synchronized (channels) {
                channels.init(table);
                channels.setDefault(new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0} );
            }
        }
    }

    @Override
    public void update() {
        return;
    }
    
}
