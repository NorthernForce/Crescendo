package org.northernforce.subsystems.ros.isaac_ros_apriltag_interfaces;

import java.io.StringReader;
import java.util.Arrays;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import edu.wpi.rail.jrosbridge.messages.Message;

public class AprilTagDetectionArray extends Message
{
    public final AprilTagDetection[] detections;
    public AprilTagDetectionArray()
    {
        this(new AprilTagDetection[0]);
    }
    public AprilTagDetectionArray(AprilTagDetection[] detections)
    {
        super(Json.createObjectBuilder()
            .add("detections", Json.createReader(
                new StringReader(Arrays.deepToString(detections)))
                .readArray())
            .build(), "isaac_ros_apriltag_interfaces/AprilTagDetection");
        this.detections = detections;
    }
    public static AprilTagDetectionArray fromJsonString(String json)
    {
        return fromMessage(new Message(json));
    }
    public static AprilTagDetectionArray fromMessage(Message message)
    {
        return fromJsonObject(message.toJsonObject());
    }
    public static AprilTagDetectionArray fromJsonObject(JsonObject object)
    {
        AprilTagDetection[] detections;
        if (object.containsKey("detections"))
        {
            JsonArray array = object.getJsonArray("detections");
            detections = new AprilTagDetection[array.size()];
            for (int i = 0; i < array.size(); i++)
            {
                detections[i] = AprilTagDetection.fromJsonObject(array.getJsonObject(i));
            }
        }
        else
        {
            detections = new AprilTagDetection[0];
        }
        return new AprilTagDetectionArray(detections);
    }
}
