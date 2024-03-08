package frc.robot.dashboard.sendables;

import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableValue;

public class Camera implements NTSendable, AutoCloseable {

    private NetworkTable table;
    private ObjectHolder<String[]> srcArr;

    public Camera() {
        srcArr = new ObjectHolder<String[]>("srcs", NetworkTableValue::makeStringArray, ObjectHolder::getStringArray);
    }

    @Override
    public void close() throws Exception {
        srcArr.close();
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
