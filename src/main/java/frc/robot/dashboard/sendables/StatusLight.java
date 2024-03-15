package frc.robot.dashboard.sendables;

import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableValue;

public class StatusLight implements NFRSendableBase {

    private NetworkTable table;
    private ObjectHolder<Boolean> value;
    private ObjectHolder<String> label;
    public String name;

    public StatusLight(String name) {
        value = new ObjectHolder<Boolean>("value", NetworkTableValue::makeBoolean, ObjectHolder::getBoolean);
        label = new ObjectHolder<String>("label", NetworkTableValue::makeString, ObjectHolder::getString);
        this.name = name;
    }

    @Override
    public void close(){
        value.close();
        label.close();
    }

    public synchronized void set(boolean value) {
        this.value.setValue(value);
    }

    @Override
    public void initSendable(NTSendableBuilder builder) {
        builder.setSmartDashboardType("StatusLight");

        synchronized (this) {
            table = builder.getTable();
            synchronized (value) {
                value.init(table);
                value.setDefault(false);
            }
            synchronized (label) {
                label.init(table);
                label.setDefault(name);
            }

            // synchronized (obj) {
            //     obj.m_entry = m_table.getDoubleArrayTopic(obj.m_name).getEntry(new double[] {});
            //     obj.updateEntry(true);
            // }
        }
    }

    @Override
    public void update() {
       return;
    }
    
}
