package org.northernforce.subsystems.ros.geometry_msgs;

import javax.json.Json;
import javax.json.JsonObject;

import org.northernforce.subsystems.ros.std_msgs.Header;

import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.geometry.PoseWithCovariance;

public class PoseWithCovarianceStamped extends Message
{
    public final Header header;
    public final PoseWithCovariance pose;
    public PoseWithCovarianceStamped()
    {
        this(new Header(), new PoseWithCovariance());
    }
    public PoseWithCovarianceStamped(Header header, PoseWithCovariance pose)
    {
        super(Json.createObjectBuilder()
            .add("header", header.toJsonObject())
            .add("pose", pose.toJsonObject())
            .build(), "geometry_msgs/PoseWithCovarianceStamped");
        this.header = header;
        this.pose = pose;
    }
    @Override
    public PoseWithCovarianceStamped clone()
    {
        return new PoseWithCovarianceStamped(header, pose);
    }
    public static PoseWithCovarianceStamped fromJsonString(String json)
    {
        return fromMessage(new Message(json));
    }
    public static PoseWithCovarianceStamped fromMessage(Message message)
    {
        return fromJsonObject(message.toJsonObject());
    }
    public static PoseWithCovarianceStamped fromJsonObject(JsonObject object)
    {
        Header header = object.containsKey("header") ? Header.fromJsonObject(object.getJsonObject("header"))
            : new Header();
        PoseWithCovariance pose = object.containsKey("pose") ?
            PoseWithCovariance.fromJsonObject(object.getJsonObject("pose")) : new PoseWithCovariance();
        return new PoseWithCovarianceStamped(header, pose);
    }
}
