package frc.robot.subsystems;

import java.util.EnumSet;
import java.util.function.Consumer;

import org.northernforce.subsystems.NFRSubsystem;

import edu.wpi.first.math.Pair;
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
import edu.wpi.first.networktables.NetworkTableEvent.Kind;
import edu.wpi.first.wpilibj.Timer;

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
    protected final DoublePublisher odometryX, odometryY, odometryTheta, odometryDeltaX, odometryDeltaY, odometryDeltaTheta;
    protected final IntegerPublisher odometryStamp;
    protected final NetworkTable globalPoseTable;
    protected final DoublePublisher globalPoseX, globalPoseY, globalPoseTheta;
    protected final IntegerPublisher globalPoseStamp;
    protected final NetworkTable targetPoseTable;
    protected final DoublePublisher targetPoseX, targetPoseY, targetPoseTheta;
    protected final IntegerPublisher targetPoseStamp;
    protected final BooleanPublisher targetPoseCancel;
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
        odometryX = odometryTable.getDoubleTopic("x").publish();
        odometryY = odometryTable.getDoubleTopic("y").publish();
        odometryTheta = odometryTable.getDoubleTopic("theta").publish();
        odometryDeltaX = odometryTable.getDoubleTopic("vx").publish();
        odometryDeltaY = odometryTable.getDoubleTopic("vy").publish();
        odometryDeltaTheta = odometryTable.getDoubleTopic("vtheta").publish();
        odometryStamp = odometryTable.getIntegerTopic("stamp").publish();
        targetPoseTable = table.getSubTable("target_pose");
        targetPoseX = targetPoseTable.getDoubleTopic("x").publish();
        targetPoseY = targetPoseTable.getDoubleTopic("y").publish();
        targetPoseTheta = targetPoseTable.getDoubleTopic("theta").publish();
        targetPoseStamp = targetPoseTable.getIntegerTopic("stamp").publish();
        targetPoseCancel = targetPoseTable.getBooleanTopic("cancel").publish();
        globalPoseTable = table.getSubTable("global_set_pose");
        globalPoseX = globalPoseTable.getDoubleTopic("x").publish();
        globalPoseY = globalPoseTable.getDoubleTopic("y").publish();
        globalPoseTheta = globalPoseTable.getDoubleTopic("theta").publish();
        globalPoseStamp = globalPoseTable.getIntegerTopic("stamp").publish();
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
    public void setOdometry(Pose2d pose, double deltaX, double deltaY, Rotation2d deltaTheta, double stamp)
    {
        odometryX.set(pose.getX());
        odometryY.set(pose.getY());
        odometryTheta.set(pose.getRotation().getRadians());
        odometryDeltaX.set(deltaX);
        odometryDeltaY.set(deltaY);
        odometryDeltaTheta.set(deltaTheta.getRadians());
        odometryStamp.set((long)(stamp * 1e9));
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
     * Gets the command velocity from nav2
     * @return dx, dy, and dtheta
     */
    public Twist2d getCommandVelocity()
    {
        return new Twist2d(cmdVelX.get(), cmdVelY.get(), cmdVelTheta.get());
    }
    // /**
    //  * Sets the map -> base_link pose
    //  * @param pose in meters
    //  * @param stamp in seconds
    //  */
    // public void setPose(Pose2d pose, double stamp)
    // {
    //     poseX.set(pose.getX());
    //     poseY.set(pose.getY());
    //     poseTheta.set(pose.getRotation().getRadians());
    //     poseStamp.set((long)(stamp * 1e9));
    // }
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
    public static record TargetDetection(double area, double tx, double ty, double pitch, double yaw, double depth, int fiducialID)
    {
        /**
         * Calculates distance to target
         * @param cameraPitch the pitch of the camera
         * @param cameraHeight the height of the camera in meters
         * @param targetHeight the height of the target in meters
         * @return a distance in meters to the target
         */
        public double calculateDistance(Rotation2d cameraPitch, double cameraHeight, double targetHeight)
        {
            return (targetHeight - cameraHeight) / Math.tan(pitch + cameraPitch.getRadians());
        }
        public double calculateDistanceWithDepth(double cameraHeight, double targetHeight)
        {
            double opposite = cameraHeight - targetHeight;
            return Math.sqrt(depth * depth - opposite * opposite);
        }
    }
    public class TargetCamera
    {
        protected final NetworkTable table;
        protected final DoubleArraySubscriber area;
        protected final DoubleArraySubscriber tx;
        protected final DoubleArraySubscriber ty;
        protected final DoubleArraySubscriber yaw;
        protected final DoubleArraySubscriber pitch;
        protected final DoubleArraySubscriber depth;
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
            depth = table.getDoubleArrayTopic("depth").subscribe(new double[] {});
            fiducialID = table.getIntegerArrayTopic("fiducial_id").subscribe(new long[] {});
            stamp = table.getIntegerArrayTopic("stamp").subscribe(new long[] {});
        }
        public TargetDetection[] getDetections()
        {
            var stamps = stamp.get();
            var areas = area.get();
            var tx = this.tx.get();
            var ty = this.ty.get();
            var pitch = this.pitch.get();
            var yaw = this.yaw.get();
            var depth = this.depth.get();
            var fiducialID = this.fiducialID.get();
            TargetDetection[] detections = new TargetDetection[stamps.length];
            for (int i = 0; i < stamps.length; i++)
            {
                detections[i] = new TargetDetection(areas[i], tx[i], ty[i], -pitch[i], yaw[i], depth[i],
                    (int)fiducialID[i]);
            }
            return detections;
        }
    }
    public class PoseSupplier
    {
        protected final NetworkTable table;
        protected final DoubleSubscriber x, y, theta;
        protected final IntegerSubscriber stamp;
        public PoseSupplier(String name, Consumer<Pair<Pose2d, Double>> consumer)
        {
            table = OrangePi.this.table.getSubTable("poses").getSubTable(name);
            x = table.getDoubleTopic("x").subscribe(0);
            y = table.getDoubleTopic("y").subscribe(0);
            theta = table.getDoubleTopic("theta").subscribe(0);
            stamp = table.getIntegerTopic("stamp").subscribe(0);
            NetworkTableInstance.getDefault().addListener(stamp, EnumSet.of(Kind.kValueAll), event -> {
                consumer.accept(Pair.of(new Pose2d(x.get(), y.get(), Rotation2d.fromRadians(theta.get())), (double)stamp.get() / 1e9));
            });
        }
    }
    public void setGlobalPose(Pose2d pose)
    {
        globalPoseX.set(pose.getX());
        globalPoseY.set(pose.getY());
        globalPoseTheta.set(pose.getRotation().getRadians());
        globalPoseStamp.set((long)(Timer.getFPGATimestamp() * 1e9));
    }
}
