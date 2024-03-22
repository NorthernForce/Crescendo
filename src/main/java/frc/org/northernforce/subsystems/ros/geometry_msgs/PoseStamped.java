package org.northernforce.subsystems.ros.geometry_msgs;

import javax.json.Json;
import javax.json.JsonObject;

import org.northernforce.subsystems.ros.std_msgs.Header;

import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.geometry.Pose;

public class PoseStamped extends Message
{
    public final Header header;
    public final Pose pose;
    public PoseStamped()
    {
        this(new Header(), new Pose());
    }
    public PoseStamped(Header header, Pose pose)
    {
        super(Json.createObjectBuilder()
            .add("header", header.toJsonObject())
            .add("pose", pose.toJsonObject())
            .build(), "geometry_msgs/PoseStamped");
        this.header = header;
        this.pose = pose;
    }
    @Override
    public PoseStamped clone()
    {
        return new PoseStamped(header, pose);
    }
    public static PoseStamped fromJsonString(String json)
    {
        return fromMessage(new Message(json));
    }
    public static PoseStamped fromMessage(Message message)
    {
        return fromJsonObject(message.toJsonObject());
    }
    public static PoseStamped fromJsonObject(JsonObject object)
    {
        Header header = object.containsKey("header") ? Header.fromJsonObject(object.getJsonObject("header"))
            : new Header();
        Pose pose = object.containsKey("pose") ?
            Pose.fromJsonObject(object.getJsonObject("pose")) : new Pose();
        return new PoseStamped(header, pose);
    }
}
