package frc.robot.dashboard.sendables;

import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableValue;

public class Camera implements NFRSendableBase {

    private NetworkTable table;
    private ObjectHolder<String[]> srcArr;

    public Camera() {
        srcArr = new ObjectHolder<String[]>("srcs", NetworkTableValue::makeStringArray, ObjectHolder::getStringArray);
    }

    @Override
    public void close() {
        srcArr.close();
    }

    public void update() {
        return;
    }

    @Override
    public void initSendable(NTSendableBuilder builder) {
        builder.setSmartDashboardType("Camera");

        synchronized (this) {
            table = builder.getTable();
            synchronized (srcArr) {
                srcArr.init(table);
                srcArr.setDefault(new String[] {});
            }

            // synchronized (obj) {
            //     obj.m_entry = m_table.getDoubleArrayTopic(obj.m_name).getEntry(new double[] {});
            //     obj.updateEntry(true);
            // }
        }
    }
    
}
