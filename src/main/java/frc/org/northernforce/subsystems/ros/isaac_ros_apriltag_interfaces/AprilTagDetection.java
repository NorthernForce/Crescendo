package org.northernforce.subsystems.ros.isaac_ros_apriltag_interfaces;

import java.io.StringReader;
import java.util.Arrays;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.northernforce.subsystems.ros.geometry_msgs.PoseWithCovarianceStamped;

import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.geometry.Point;

public class AprilTagDetection extends Message
{
    public final String family;
    public final int id;
    public final Point center;
    public final Point[] corners;
    public final PoseWithCovarianceStamped pose;
    public AprilTagDetection()
    {
        this("", 0, new Point(), new Point[] {new Point(), new Point(), new Point(), new Point()},
            new PoseWithCovarianceStamped());
    }
    public AprilTagDetection(String family, int id, Point center, Point[] corners, PoseWithCovarianceStamped pose)
    {
        super(Json.createObjectBuilder()
            .add("family", family)
            .add("id", id)
            .add("center", center.toJsonObject())
            .add("corners", Json.createReader(
                new StringReader(Arrays.deepToString(corners)))
                .readArray())
            .add("pose", pose.toJsonObject())
            .build(), "isaac_ros_apriltag_interfaces/AprilTagDetection");
        this.family = family;
        this.id = id;
        this.center = center;
        this.corners = corners;
        this.pose = pose;
    }
    public static AprilTagDetection fromJsonString(String json)
    {
        return fromMessage(new Message(json));
    }
    public static AprilTagDetection fromMessage(Message message)
    {
        return fromJsonObject(message.toJsonObject());
    }
    public static AprilTagDetection fromJsonObject(JsonObject object)
    {
        String family = object.containsKey("family") ?
            object.getString("family") : "";
        int id = object.containsKey("id") ? 
            object.getInt("id") : 0;
        Point center = object.containsKey("center") ?
            Point.fromJsonObject(object.getJsonObject("center")) : new Point();
        Point[] corners;
        if (object.containsKey("corners"))
        {
            JsonArray array = object.getJsonArray("corners");
            corners = new Point[array.size()];
            for (int i = 0; i < array.size(); i++)
            {
                corners[i] = Point.fromJsonObject(array.getJsonObject(i));
            }
        }
        else
        {
            corners = new Point[] {
                new Point(), new Point(), new Point(), new Point()
            };
        }
        PoseWithCovarianceStamped pose = object.containsKey("pose") ?
            PoseWithCovarianceStamped.fromJsonObject(object.getJsonObject("pose")) : new PoseWithCovarianceStamped();
        return new AprilTagDetection(family, id, center, corners, pose);
    }
}
