package frc.robot.subsystems;

import java.util.List;
import java.util.Optional;

import org.littletonrobotics.junction.Logger;
import org.northernforce.subsystems.NFRSubsystem;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.dashboard.Dashboard.Alert;
import frc.robot.dashboard.Dashboard.AlertType;
import frc.robot.utils.AlertProvider;
import frc.robot.utils.LoggableHardware;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.FieldConstants;

/**
 * This is a subsystem for the Orange Pi 5+ that runs nfr_ros.
 */
public class NFRPhotonCamera extends NFRSubsystem implements AlertProvider, LoggableHardware
{
    /**
     * This is the configuration class for the OrangePi subsystem.
     */
    public static class NFRPhotonCameraConfiguration extends NFRSubsystemConfiguration
    {
        protected String cameraName;
        protected Transform3d cameraTransform;
        /**
         * Create a new NFRPhotonCameraConfiguration.
         * @param name the name of the subsystem
         */
        public NFRPhotonCameraConfiguration(String name)
        {
            super(name);
        }
        /**
         * Create a new NFRPhotonCameraConfiguration.
         * @param name the name of the subsystem
         * @param cameraName the name of the PhotonVision camera
         * @param cameraTransform the tranform from robot center to camera
         */
        public NFRPhotonCameraConfiguration(String name, String cameraName, Transform3d cameraTransform)
        {
            super(name);
            this.cameraName = cameraName;
            this.cameraTransform = cameraTransform;
        }
        /**
         * With camera name
         * @param cameraName the name of the PhotonVision camera
         * @return this
         */
        public NFRPhotonCameraConfiguration withCameraName(String cameraName)
        {
            this.cameraName = cameraName;
            return this;
        }
        /**
         * With camera transform
         * @param cameraTransform the tranform from robot center to camera
         * @return this
         */
        public NFRPhotonCameraConfiguration withCameraTransform(Transform3d cameraTransform)
        {
            this.cameraTransform = cameraTransform;
            return this;
        }
    }
    protected final Alert isMissing;
    protected final PhotonCamera camera;
    protected final PhotonPoseEstimator poseEstimator;
    protected Optional<EstimatedRobotPose> estimatedPose;
    /**
     * Create a new PhotonCamera
     * @param config the configuration for the NFRPhotonCamera.
     */
    public NFRPhotonCamera(NFRPhotonCameraConfiguration config)
    {
        super(config);
        isMissing = new Alert(AlertType.kError, config.cameraName + " is not connected");
        camera = new PhotonCamera(config.cameraName);
        poseEstimator = new PhotonPoseEstimator(AprilTagFields.k2024Crescendo.loadAprilTagLayoutField(), PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR, config.cameraTransform);
        estimatedPose = Optional.empty();
    }
    /**
     * Checks to see if the PhotonCamera is connected
     * @return whether the PhotonCamera is connected
     */
    public boolean isConnected()
    {
        return camera.isConnected();
    }
    public List<PhotonTrackedTarget> getDetections()
    {
        return camera.getLatestResult().targets;
    }
    /**
     * Get a specific tag detection if present
     * @param fiducialID the tag id
     * @return tag detection if present in latest frame
     */
    public Optional<PhotonTrackedTarget> getTarget(int fiducialID)
    {
        for (var detection : getDetections())
        {
            if (detection.getFiducialId() == fiducialID)
            {
                return Optional.of(detection);
            }
        }
        return Optional.empty();
    }
    /**
     * Gets the distance to the speaker without using the depth camera. Needs the height of the apriltag camera.
     * @param cameraHeight the height in meters of the apriltag camera
     * @return the distance from the camera to the speaker along the xy plane
     */
    public Optional<Double> getDistanceToSpeaker(double cameraHeight, Rotation2d cameraPitch)
    {
        int targetId = DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red ? 4 : 7;
        for (var detection : getDetections())
        {
            if (detection.getFiducialId() == targetId)
            {
                return Optional.of(
                    Math.sqrt(
                        Math.pow(detection.getBestCameraToTarget().getTranslation().getDistance(new Translation3d()), 2)
                        + Math.pow(cameraHeight - FieldConstants.SpeakerConstants.speakerHeight, 2)));
            }
        }
        return Optional.empty();
    }
    /**
     * Gets the speaker tag if within the frame of the camera
     * @return speaker tag, if present
     */
    public Optional<PhotonTrackedTarget> getSpeakerTag()
    {
        return getTarget(DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red ? 4 : 7);
    }
    public Optional<Rotation2d> getSpeakerTagYaw()
    {
        var speakerTag = getSpeakerTag();
        if (speakerTag.isPresent())
        {
            return Optional.of(Rotation2d.fromDegrees(speakerTag.get().getYaw()));
        }
        return Optional.empty();
    }
    @Override
    public void periodic() {
        isMissing.shouldDisplay().set(!isConnected());
    }
    public Optional<EstimatedRobotPose> getPose()
    {
        return poseEstimator.update(camera.getLatestResult());
    }
    @Override
    public List<Alert> getPossibleAlerts()
    {
        return List.of(isMissing);
    }
    @Override
    public void startLogging(double period) {
    }
    @Override
    public void logOutputs(String name) {
        Logger.recordOutput(name + "/IsConnected", isConnected());
        Logger.recordOutput(name + "/PoseEstimate", estimatedPose.isPresent() ? estimatedPose.get().estimatedPose : new Pose3d());
    }
}
