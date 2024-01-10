package frc.robot.subsystems;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.northernforce.subsystems.NFRSubsystem;

import edu.wpi.first.apriltag.AprilTagDetection;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.BooleanSubscriber;
import edu.wpi.first.networktables.DoubleArraySubscriber;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.IntegerPublisher;
import edu.wpi.first.networktables.IntegerSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEvent;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableEvent.Kind;

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
    protected final DoublePublisher odometryDeltaX, odometryDeltaY;
    protected final IntegerPublisher odometryStamp;
    protected final NetworkTable imuTable;
    protected final DoublePublisher imuTheta;
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
        odometryStamp = odometryTable.getIntegerTopic("stamp").publish();
        imuTable = table.getSubTable("imu");
        imuTheta = imuTable.getDoubleTopic("theta").publish();
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
     * @param stamp in seconds
     */
    public void setOdometry(double deltaX, double deltaY, double stamp)
    {
        odometryDeltaX.set(deltaX);
        odometryDeltaY.set(deltaY);
        odometryStamp.set((long)(stamp * 1e9));
    }
    /**
     * Sets the imu
     * @param theta absolute rotation blue relative
     * @param stamp in seconds
     */
    public void setIMU(Rotation2d theta, double stamp)
    {
        imuTheta.set(theta.getRadians());
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
    public class AprilTagCamera
    {
        protected class AprilTagTable
        {
            protected final int tagID;
            protected final NetworkTable apriltagTable;
            protected final IntegerSubscriber apriltagHamming, apriltagStamp;
            protected final DoubleSubscriber apriltagDecisionMargin, apriltagCenterX, apriltagCenterY;
            protected final DoubleArraySubscriber apriltagHomography, apriltagCorners;
            protected final BooleanSubscriber apriltagPresent;
            protected final Consumer<AprilTagDetection> detectionConsumer;
            /**
             * Creates a table that holds the values for an apriltag
             * @param tagID the id for the tag
             */
            public AprilTagTable(int tagID, Consumer<AprilTagDetection> detectionConsumer)
            {
                this.tagID = tagID;
                apriltagTable = cameraTable.getSubTable(tagFamily + "_" + tagID);
                apriltagHamming = apriltagTable.getIntegerTopic("hamming").subscribe(0);
                apriltagDecisionMargin = apriltagTable.getDoubleTopic("decision_margin").subscribe(0);
                apriltagHomography = apriltagTable.getDoubleArrayTopic("homography").subscribe(new double[] {});
                apriltagCenterX = apriltagTable.getDoubleTopic("centerX").subscribe(0);
                apriltagCenterY = apriltagTable.getDoubleTopic("centerY").subscribe(0);
                apriltagCorners = apriltagTable.getDoubleArrayTopic("corners").subscribe(new double[] {});
                apriltagStamp = apriltagTable.getIntegerTopic("stamp").subscribe(0);
                apriltagPresent = apriltagTable.getBooleanTopic("present").subscribe(false);
                this.detectionConsumer = detectionConsumer;
                NetworkTableInstance.getDefault().addListener(apriltagStamp, EnumSet.of(Kind.kValueAll), this::handleDetection);
            }
            /**
             * Gets the current detection
             * @return the detection, if any
             */
            public Optional<AprilTagDetection> getDetection()
            {
                if (apriltagPresent.get())
                {
                    return Optional.of(new AprilTagDetection(tagFamily, tagID, (int)apriltagHamming.get(), (float)apriltagDecisionMargin.get(),
                        apriltagHomography.get(), apriltagCenterX.get(), apriltagCenterY.get(), apriltagCorners.get()));
                }
                else
                {
                    return Optional.empty();
                }
            }
            public void handleDetection(NetworkTableEvent event)
            {
                getDetection().ifPresent(detectionConsumer);
            }
        };
        protected final String tagFamily;
        protected final NetworkTable cameraTable;
        protected final Map<Integer, AprilTagTable> tableMap;
        protected final Consumer<AprilTagDetection> detectionConsumer;
        public AprilTagCamera(String name, String tagFamily, int tagCount, Consumer<AprilTagDetection> detectionConsumer)
        {
            cameraTable = table.getSubTable("apriltags").getSubTable(name);
            this.tagFamily = tagFamily;
            tableMap = new HashMap<>();
            for (int i = 1; i <= tagCount; i++)
            {
                tableMap.put(i, new AprilTagTable(i, this::handleApriltagDetection));
            }
            this.detectionConsumer = detectionConsumer;
        }
        public void handleApriltagDetection(AprilTagDetection detection)
        {
            detectionConsumer.accept(detection);
        }
    };
    public static record TargetDetection(double area, double x, double y, double width, double height)
    {
    }
    public class NoteDetectorCamera
    {
        protected final NetworkTable detectorTable;
        protected final DoubleSubscriber area, x, y, width, height;
        protected final BooleanSubscriber present;
        protected final IntegerSubscriber stamp;
        protected final Consumer<TargetDetection> detectionConsumer;
        public NoteDetectorCamera(String cameraName, Consumer<TargetDetection> detectionConsumer)
        {
            detectorTable = table.getSubTable(cameraName);
            area = detectorTable.getDoubleTopic("area").subscribe(0);
            x = detectorTable.getDoubleTopic("x").subscribe(0);
            y = detectorTable.getDoubleTopic("y").subscribe(0);
            width = detectorTable.getDoubleTopic("width").subscribe(0);
            height = detectorTable.getDoubleTopic("height").subscribe(0);
            present = detectorTable.getBooleanTopic("present").subscribe(false);
            stamp = detectorTable.getIntegerTopic("stamp").subscribe(0);
            this.detectionConsumer = detectionConsumer;
            NetworkTableInstance.getDefault().addListener(stamp, EnumSet.of(Kind.kValueAll), this::handleDetection);
        }
        public Optional<TargetDetection> getDetection()
        {
            if (present.get())
            {
                return Optional.of(new TargetDetection(area.get(), x.get(), y.get(), width.get(), height.get()));
            }
            else
            {
                return Optional.empty();
            }
        }
        public void handleDetection(NetworkTableEvent event)
        {
            getDetection().ifPresent(detectionConsumer);
        }
    };
}
