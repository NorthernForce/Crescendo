package org.northernforce.cameras;

import java.util.ArrayList;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;

/**
 * An interface for dealing with cameras.
 */
public interface NFRCamera
{
    /**
     * An interface defining functions for camera pipelines.
     */
    public interface Pipeline
    {
        /**
         * A pipeline type. Names are self-explanatory.
         */
        public enum Type
        {
            kDriverCamera,
            kApriltag,
            kReflectiveTape,
            kColoredShape
        }
        /**
         * Gets the pipeline index. Typically 0-based.
         * @return pipeline index
         */
        public int getIndex();
        /**
         * Gets name of pipeline as camera recognizes it.
         * @return pipeline index
         */
        public String getName();
        /**
         * Gets the type of the pipeline.
         * @return the Type enum representing the pipeline type.
         */
        public Type getType();
    }
    /**
     * Interface for an apriltag pipeline
     */
    public interface ApriltagPipeline extends Pipeline
    {
        /**
         * Gets the type of the pipeline.
         * @return the Type enum representing the pipeline type.
         */
        @Override
        public default Type getType()
        {
            return Type.kApriltag;
        }
        /**
         * Sets the field layout used for pose estimations
         * @param layout the wpilib AprilTagFieldLayout
         */
        public void setFieldLayout(AprilTagFieldLayout layout);
        /**
         * Gets the current pose estimations by the camera
         * @return camera pose estimations
         */
        public ArrayList<Pair<Double, Pose2d>> getEstimations();
    }
    /**
     * Interface for a driver pipeline
     */
    public interface DriverPipeline extends Pipeline
    {
        /**
         * Gets the type of the pipeline.
         * @return the Type enum representing the pipeline type.
         */
        @Override
        public default Type getType()
        {
            return Type.kDriverCamera;
        }
    }
    /**
     * Interface for a reflective tape pipeline
     */
    public interface ReflectiveTapePipeline extends Pipeline
    {
        /**
         * Gets the type of the pipeline.
         * @return the Type enum representing the pipeline type.
         */
        @Override
        public default Type getType()
        {
            return Type.kReflectiveTape;
        }
        /**
         * Whether target is present in camera view
         * @return whether target is present
         */
        public boolean hasTarget();
        /**
         * Returns the coordinates of the center of the target
         * @return the coordinates of the target relative to the crosshair.
         */
        public Translation2d getTargetCoords();
    }
    /**
     * Interface for a colored shape pipeline
     */
    public interface ColoredShapePipeline extends Pipeline
    {
        /**
         * Gets the type of the pipeline.
         * @return the Type enum representing the pipeline type.
         */
        @Override
        public default Type getType()
        {
            return Type.kColoredShape;
        }
        /**
         * Whether target is present in camera view
         * @return whether target is present
         */
        public boolean hasTarget();
        /**
         * Returns the coordinates of the center of the target
         * @return the coordinates of the target relative to the crosshair.
         */
        public Translation2d getTargetCoords();
    }
    /**
     * Gets the list of available pipelines
     * @return a list of pipelines
     */
    public ArrayList<Pipeline> getPipelines();
    /**
     * Sets the current pipeline
     * @param pipeline the pipeline index to set the current pipeline to
     */
    public void setPipeline(Pipeline pipeline);
    /**
     * Gets the current pipeline
     * @return the current pipeline
     */
    public Pipeline getCurrentPipeline();
}