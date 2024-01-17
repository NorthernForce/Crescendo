package frc.robot.subsystems;

import org.northernforce.subsystems.NFRSubsystem;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.DoubleArraySubscriber;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.IntegerArraySubscriber;
import edu.wpi.first.networktables.IntegerPublisher;
import edu.wpi.first.networktables.IntegerSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * This is a subsystem for the Orange Pi 5+ that runs nfr_ros.
 */
public class OrangePi extends NFRSubsystem
{
    /**
     * This is the configuration class for the OrangePi subsystem.
     */
    public static class OrangePiConfiguration extends NFRSubsystemConfiguration
    {
        protected String tableName;
        /**
         * Create a new OrangePiConfiguration.
         * @param name the name of the subsystem
         */
        public OrangePiConfiguration(String name)
        {
            super(name);
        }
        /**
         * Create a new OrangePiConfiguration.
         * @param name the name of the subsystem
         * @param tableName the name of the table where the orange pi publishes its information
         */
        public OrangePiConfiguration(String name, String tableName)
        {
            super(name);
            this.tableName = tableName;
        }
        /**
         * With table name
         * @param tableName the name of the table where the orange pi publishes its information
         * @return this
         */
        public OrangePiConfiguration withTableName(String tableName)
        {
            this.tableName = tableName;
            return this;
        }
    }
    protected final NetworkTable table;
    protected final NetworkTable odometryTable;
    protected final DoublePublisher odometryDeltaX, odometryDeltaY, odometryDeltaTheta;
    protected final IntegerPublisher odometryStamp;
    protected final NetworkTable imuTable;
    protected final DoublePublisher imuDeltaTheta;
    protected final IntegerPublisher imuStamp;
    protected final NetworkTable targetPoseTable;
    protected final DoublePublisher targetPoseX, targetPoseY, targetPoseTheta;
    protected final IntegerPublisher targetPoseStamp;
    protected final BooleanPublisher targetPoseCancel;
    protected final NetworkTable globalSetPoseTable;
    protected final DoublePublisher globalSetPoseX, globalSetPoseY, globalSetPoseTheta;
    protected final IntegerPublisher globalSetPoseStamp;
    protected final NetworkTable cmdVelTable;
    protected final DoubleSubscriber cmdVelX, cmdVelY, cmdVelTheta;
    protected final NetworkTable poseTable;
    protected final DoubleSubscriber poseX, poseY, poseTheta;
    protected final IntegerSubscriber poseStamp;
    /**
     * Create a new orange pi.
     * @param config
     */
    public OrangePi(OrangePiConfiguration config)
    {
        super(config);
        table = NetworkTableInstance.getDefault().getTable(config.tableName);
        odometryTable = table.getSubTable("odometry");
        odometryDeltaX = odometryTable.getDoubleTopic("vx").publish();
        odometryDeltaY = odometryTable.getDoubleTopic("vy").publish();
        odometryDeltaTheta = odometryTable.getDoubleTopic("vtheta").publish();
        odometryStamp = odometryTable.getIntegerTopic("stamp").publish();
        imuTable = table.getSubTable("imu");
        imuDeltaTheta = imuTable.getDoubleTopic("vtheta").publish();
        imuStamp = imuTable.getIntegerTopic("stamp").publish();
        targetPoseTable = table.getSubTable("target_pose");
        targetPoseX = targetPoseTable.getDoubleTopic("x").publish();
        targetPoseY = targetPoseTable.getDoubleTopic("y").publish();
        targetPoseTheta = targetPoseTable.getDoubleTopic("theta").publish();
        targetPoseStamp = targetPoseTable.getIntegerTopic("stamp").publish();
        targetPoseCancel = targetPoseTable.getBooleanTopic("cancel").publish();
        globalSetPoseTable = table.getSubTable("global_set_pose");
        globalSetPoseX = globalSetPoseTable.getDoubleTopic("x").publish();
        globalSetPoseY = globalSetPoseTable.getDoubleTopic("y").publish();
        globalSetPoseTheta = globalSetPoseTable.getDoubleTopic("theta").publish();
        globalSetPoseStamp = globalSetPoseTable.getIntegerTopic("stamp").publish();
        cmdVelTable = table.getSubTable("cmd_vel");
        cmdVelX = cmdVelTable.getDoubleTopic("x").subscribe(0);
        cmdVelY = cmdVelTable.getDoubleTopic("y").subscribe(0);
        cmdVelTheta = cmdVelTable.getDoubleTopic("theta").subscribe(0);
        poseTable = table.getSubTable("pose");
        poseX = poseTable.getDoubleTopic("x").subscribe(0);
        poseY = poseTable.getDoubleTopic("y").subscribe(0);
        poseTheta = poseTable.getDoubleTopic("theta").subscribe(0);
        poseStamp = poseTable.getIntegerTopic("stamp").subscribe(0);
    }
    /**
     * Sets the odometry
     * @param deltaX in m/s
     * @param deltaY in m/s
     * @param deltaTheta change in angle
     * @param stamp in seconds
     */
    public void setOdometry(double deltaX, double deltaY, Rotation2d deltaTheta, double stamp)
    {
        odometryDeltaX.set(deltaX);
        odometryDeltaY.set(deltaY);
        odometryDeltaTheta.set(deltaTheta.getRadians());
        odometryStamp.set((long)(stamp * 1e9));
    }
    /**
     * Sets the imu
     * @param deltaTheta change in angle
     * @param stamp in seconds
     */
    public void setIMU(Rotation2d deltaTheta, double stamp)
    {
        imuDeltaTheta.set(deltaTheta.getRadians());
        imuStamp.set((long)(stamp * 1e9));
    }
    /**
     * Sends a target pose
     * @param pose the target pose
     * @param stamp in seconds
     */
    public void sendTargetPose(Pose2d pose, double stamp)
    {
        targetPoseX.set(pose.getX());
        targetPoseY.set(pose.getY());
        targetPoseTheta.set(pose.getRotation().getRadians());
        targetPoseStamp.set((long)(stamp * 1e9));
    }
    /**
     * Cancels a target pose
     */
    public void cancelTargetPose()
    {
        targetPoseCancel.set(true);
    }
    /**
     * Sends a global set pose
     * @param pose the global set pose
     * @param stamp in seconds
     */
    public void sendGlobalSetPose(Pose2d pose, double stamp)
    {
        globalSetPoseX.set(pose.getX());
        globalSetPoseY.set(pose.getY());
        globalSetPoseTheta.set(pose.getRotation().getRadians());
        globalSetPoseStamp.set((long)(stamp * 1e9));
    }
    /**
     * Gets the command velocity from nav2
     * @return dx, dy, and dtheta
     */
    public Twist2d getCommandVelocity()
    {
        return new Twist2d(cmdVelX.get(), cmdVelY.get(), cmdVelTheta.get());
    }
    /**
     * Gets the pose of the robot via localization
     * @return robot pose
     */
    public Pose2d getPose()
    {
        return new Pose2d(poseX.get(), poseY.get(), Rotation2d.fromRadians(poseTheta.get()));
    }
    /**
     * Checks to see if the orange pi is connected
     * @return whether the orange pi is connected
     */
    public boolean isConnected()
    {
        boolean connectionFound = false;
        for (var connection : NetworkTableInstance.getDefault().getConnections())
        {
            if (connection.remote_id.startsWith("xavier"))
            {
                connectionFound = true;
            }
        }
        return connectionFound;
    }
    public static record TargetDetection(double area, double tx, double ty, double pitch, double yaw, int fiducialID)
    {
    }
    public class TargetCamera
    {
        protected final NetworkTable table;
        protected final DoubleArraySubscriber area;
        protected final DoubleArraySubscriber tx;
        protected final DoubleArraySubscriber ty;
        protected final DoubleArraySubscriber yaw;
        protected final DoubleArraySubscriber pitch;
        protected final IntegerArraySubscriber fiducialID;
        protected final IntegerArraySubscriber stamp;
        public TargetCamera(String name)
        {
            table = OrangePi.this.table.getSubTable(name);
            area = table.getDoubleArrayTopic("area").subscribe(new double[] {});
            tx = table.getDoubleArrayTopic("tx").subscribe(new double[] {});
            ty = table.getDoubleArrayTopic("ty").subscribe(new double[] {});
            yaw = table.getDoubleArrayTopic("yaw").subscribe(new double[] {});
            pitch = table.getDoubleArrayTopic("pitch").subscribe(new double[] {});
            fiducialID = table.getIntegerArrayTopic("fiducial_id").subscribe(new long[] {});
            stamp = table.getIntegerArrayTopic("stamp").subscribe(new long[] {});
        }
        public TargetDetection[] getDetections()
        {
            TargetDetection[] detections = new TargetDetection[stamp.get().length];
            for (int i = 0; i < stamp.get().length; i++)
            {
                detections[i] = new TargetDetection(area.get()[i], tx.get()[i], ty.get()[i], yaw.get()[i], pitch.get()[i], (int)fiducialID.get()[i]);
            }
            return detections;
        }
    }
}
