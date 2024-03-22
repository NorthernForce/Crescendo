package org.northernforce.subsystems.ros.nav_msgs;

import javax.json.Json;
import javax.json.JsonObject;

import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.geometry.PoseWithCovariance;
import edu.wpi.rail.jrosbridge.messages.geometry.TwistWithCovariance;

import org.northernforce.subsystems.ros.std_msgs.Header;

public class Odometry extends Message
{
    public final Header header;
    public final String child_frame_id;
    public final PoseWithCovariance pose;
    public final TwistWithCovariance twist;
    public Odometry()
    {
        this(new Header(), "", new PoseWithCovariance(), new TwistWithCovariance());
    }
    public Odometry(Header header, String child_frame_id, PoseWithCovariance pose, TwistWithCovariance twist)
    {
        super(Json.createObjectBuilder()
            .add("header", header.toJsonObject())
            .add("child_frame_id", child_frame_id)
            .add("pose", pose.toJsonObject())
            .add("twist", twist.toJsonObject())
            .build(), "nav_msgs/Odometry");
        this.header = header;
        this.child_frame_id = child_frame_id;
        this.pose = pose;
        this.twist = twist;
    }
    @Override
    public Odometry clone()
    {
        return new Odometry(header, child_frame_id, pose, twist);
    }
    public static Odometry fromJsonString(String json)
    {
        return fromMessage(new Message(json));
    }
    public static Odometry fromMessage(Message message)
    {
        return fromJsonObject(message.toJsonObject());
    }
    public static Odometry fromJsonObject(JsonObject object)
    {
        Header header = object.containsKey("header") ? Header.fromJsonObject(object.getJsonObject("header"))
            : new Header();
        String child_frame_id = object.containsKey("child_frame_id") ? object.getString("child_frame_id") : "";
        PoseWithCovariance pose = object.containsKey("pose") ?
            PoseWithCovariance.fromJsonObject(object.getJsonObject("pose")) : new PoseWithCovariance();
        TwistWithCovariance twist = object.containsKey("twist") ?
            TwistWithCovariance.fromJsonObject(object.getJsonObject("twist")) : new TwistWithCovariance();
        return new Odometry(header, child_frame_id, pose, twist);
    }
}
