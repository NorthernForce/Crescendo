package frc.robot.subsystems;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.northernforce.subsystems.NFRSubsystem;

import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.geometry.struct.Pose2dStruct;
import edu.wpi.first.math.geometry.struct.Twist2dStruct;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.IntegerPublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArraySubscriber;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.networktables.StructSubscriber;
import edu.wpi.first.networktables.NetworkTableEvent.Kind;
import edu.wpi.first.util.struct.Struct;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.dashboard.Dashboard.Alert;
import frc.robot.dashboard.Dashboard.AlertType;
import frc.robot.utils.AlertProvider;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

/**
 * This is a subsystem for the Orange Pi 5+ that runs nfr_ros.
 */
public class OrangePi extends NFRSubsystem implements AlertProvider
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
    protected final StructPublisher<Twist2d> odometryPublisher;
    protected final StructSubscriber<Twist2d> cmdVelSubscriber;
    protected final StructPublisher<Pose2d> globalPosePublisher, targetPosePublisher;
    protected final IntegerPublisher odometryStamp, globalPoseStamp, targetPoseStamp;
    protected final BooleanPublisher targetPoseCancel;
    protected final StructSubscriber<Pose2d> poseSubscriber;
    protected final Alert orangePiMissing;
    /**
     * Create a new orange pi.
     * @param config the configuration for the orange pi.
     */
    public OrangePi(OrangePiConfiguration config)
    {
        super(config);
        table = NetworkTableInstance.getDefault().getTable(config.tableName);
        odometryPublisher = table.getStructTopic("odometry", new Twist2dStruct()).publish();
        odometryStamp = table.getIntegerTopic("odometry_stamp").publish();
        targetPosePublisher = table.getStructTopic("target_pose", new Pose2dStruct()).publish();
        targetPoseStamp = table.getIntegerTopic("target_pose_stamp").publish();
        targetPoseCancel = table.getBooleanTopic("target_pose_cancel").publish();
        globalPosePublisher = table.getStructTopic("global_set_pose", new Pose2dStruct()).publish();
        globalPoseStamp = table.getIntegerTopic("global_pose_stamp").publish();
        cmdVelSubscriber = table.getStructTopic("cmd_vel", new Twist2dStruct()).subscribe(new Twist2d());
        poseSubscriber = table.getStructTopic("pose", new Pose2dStruct()).subscribe(new Pose2d());
        orangePiMissing = new Alert(AlertType.kError, "Orange pi is not connected");
    }
    /**
     * Sets the odometry
     * @param deltaX in m/s
     * @param deltaY in m/s
     * @param deltaTheta change in angle
     * @param stamp in seconds
     */
    public void setOdometry(ChassisSpeeds speeds)
    {
        odometryPublisher.set(new Twist2d(speeds.vxMetersPerSecond, speeds.vyMetersPerSecond, speeds.omegaRadiansPerSecond));
        odometryStamp.set((long)(Timer.getFPGATimestamp() * 1e9));
        NetworkTableInstance.getDefault().flush();
    }
    /**
     * Sends a target pose
     * @param pose the target pose
     * @param stamp in seconds
     */
    public void sendTargetPose(Pose2d pose)
    {
        targetPosePublisher.set(pose);
        targetPoseStamp.set((long)(Timer.getFPGATimestamp() * 1e9));
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
        return cmdVelSubscriber.get();
    }
    /**
     * Gets the position recorded by the sensor fusion
     * @return the pose on the topic "pose" (in meters)
     */
    public Pose2d getPose()
    {
        return poseSubscriber.get();
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
    /**
     * This is a record of each target detection
     * @param area the area of the detection. 0 for apriltags
     * @param tx the image coordinate x of the tag
     * @param ty the image coordinate y of the tag
     * @param pitch the angle of pitch (up/down) in radians
     * @param yaw the angle of yaw (left/right) in radians
     * @param depth the depth of the target via depth sensor
     * @param fiducialID the fiducialID of the target
     */
    public static record TargetDetection(double area, double tx, double ty, double pitch, double yaw, double depth, int fiducialID)
    {
        /**
         * Calculates distance to target. <li>
         * (targetHeight - cameraHeight) / tan(pitch + cameraPitch)
         * @param cameraPitch the pitch of the camera
         * @param cameraHeight the height of the camera in meters
         * @param targetHeight the height of the target in meters
         * @return a distance in meters to the target
         */
        public double calculateDistanceWithPitch(Rotation2d cameraPitch, double cameraHeight, double targetHeight)
        {
            return (targetHeight - cameraHeight) / Math.tan(pitch + cameraPitch.getRadians());
        }
        /**
         * This calculates the distance to the target using the depth estimation. Gets rid of height as a factor. <li>
         * sqrt(depth * depth - opposite * opposite)
         * @param cameraHeight The height of the camera in meters
         * @param targetHeight The height of the target in meters
         * @return the distance along the xy plane
         */
        public double calculateDistanceWithDepth(double cameraHeight, double targetHeight)
        {
            double opposite = cameraHeight - targetHeight;
            return Math.sqrt(depth * depth - opposite * opposite);
        }
    }
    /**
     * This is a simple struct class for the target detection
     */
    public static class TargetDetectionStruct implements Struct<TargetDetection>
    {
        @Override
        public Class<TargetDetection> getTypeClass()
        {
            return TargetDetection.class;
        }
        @Override
        public String getTypeString()
        {
            return "struct:TargetDetection";
        }
        @Override
        public int getSize()
        {
            return Struct.kSizeDouble * 6 + Struct.kSizeInt64;
        }
        @Override
        public String getSchema()
        {
            return "double area; double tx; double ty; double pitch; double yaw; double depth; long fiducialID";
        }
        @Override
        public TargetDetection unpack(ByteBuffer buffer)
        {
            return new TargetDetection(buffer.getDouble(), buffer.getDouble(), buffer.getDouble(), buffer.getDouble(), buffer.getDouble(),
                buffer.getDouble(), (int)buffer.getLong());
        }
        @Override
        public void pack(ByteBuffer buffer, TargetDetection detection)
        {
            buffer.putDouble(detection.area);
            buffer.putDouble(detection.tx);
            buffer.putDouble(detection.ty);
            buffer.putDouble(detection.pitch);
            buffer.putDouble(detection.yaw);
            buffer.putDouble(detection.depth);
            buffer.putLong(detection.fiducialID);
        }
    }
    /**
     * Each target camera subscribes to a topic for getting target detection structs
     */
    public class TargetCamera
    {
        protected final StructArraySubscriber<TargetDetection> detections;
        /**
         * Creates a new Target Camera.
         * @param name topic on nt to subscribe to
         */
        public TargetCamera(String name)
        {
            detections = table.getStructArrayTopic(name, new TargetDetectionStruct()).subscribe(new TargetDetection[] {});
        }
        /**
         * Returns the list of target detections.
         * @return current detections on Network tables.
         */
        public TargetDetection[] getDetections()
        {
            return detections.get();
        }
        /**
         * Get a specific tag detection if present
         * @param fiducialID the tag id
         * @return tag detection if present in latest frame
         */
        public Optional<TargetDetection> getTarget(int fiducialID)
        {
            for (var detection : getDetections())
            {
                if (detection.fiducialID == fiducialID)
                {
                    return Optional.of(detection);
                }
            }
            return Optional.empty();
        }
        /**
         * Gets the distance to the speaker using the depth camera. Needs the height of the depth camera.
         * @param cameraHeight the height in meters of the depth camera
         * @return the distance from the camera to the speaker along the xy plane
         */
        public Optional<Double> getDistanceToSpeaker(double cameraHeight)
        {
            int targetId = DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red ? 4 : 7;
            for (var detection : getDetections())
            {
                if (detection.fiducialID == targetId)
                {
                    return Optional.of(detection.calculateDistanceWithDepth(cameraHeight, Units.inchesToMeters(57)));
                }
            }
            return Optional.empty();
        }
        /**
         * Gets the speaker tag if within the frame of the camera
         * @return speaker tag, if present
         */
        public Optional<TargetDetection> getSpeakerTag()
        {
            return getTarget(DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red ? 4 : 7);
        }
    }
    /**
     * Each pose supplier supplies to poses/poseName for getting pose estimations via vision.
     */
    public class PoseSupplier
    {
        protected final StructSubscriber<Pose2d> poseSubscriber;
        /**
         * Creates a new PoseSupplier
         * @param name the name of the topic under poses to subscribe to
         * @param consumer the consumer for the pose estimation
         */
        public PoseSupplier(String name, Consumer<Pair<Pose2d, Double>> consumer)
        {
            poseSubscriber = table.getSubTable("poses").getStructTopic(name, new Pose2dStruct()).subscribe(new Pose2d());
            NetworkTableInstance.getDefault().addListener(poseSubscriber, EnumSet.of(Kind.kValueAll), event -> {
                var pose = poseSubscriber.getAtomic();
                consumer.accept(Pair.of(pose.value, (double)pose.timestamp / 1e9));
            });
        }
    }
    /**
     * Sets the global pose for resetting purposes (start of match for example)
     * @param pose the pose of the robot at start of match
     */
    public void setGlobalPose(Pose2d pose)
    {
        globalPosePublisher.set(pose);
        globalPoseStamp.set((long)(Timer.getFPGATimestamp() * 1e9));
    }
    @Override
    public void periodic() {
        orangePiMissing.shouldDisplay().set(!isConnected());
    }
    @Override
    public List<Alert> getPossibleAlerts()
    {
        return List.of(orangePiMissing);
    }
}
