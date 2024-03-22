package org.northernforce.subsystems.ros.geometry_msgs;

import javax.json.Json;
import javax.json.JsonObject;

import org.northernforce.subsystems.ros.std_msgs.Header;

import edu.wpi.rail.jrosbridge.messages.Message;
import edu.wpi.rail.jrosbridge.messages.geometry.Transform;

public class TransformStamped extends Message
{
    public final Header header;
    public final String child_frame_id;
    public final Transform transform;
    public TransformStamped()
    {
        this(new Header(), "", new Transform());
    }
    public TransformStamped(Header header, String child_frame_id, Transform transform)
    {
        super(Json.createObjectBuilder()
            .add("header", header.toJsonObject())
            .add("child_frame_id", child_frame_id)
            .add("transform", transform.toJsonObject())
            .build(), "geometry_msgs/TransformStamped");
        this.header = header;
        this.child_frame_id = child_frame_id;
        this.transform = transform;
    }
    @Override
    public TransformStamped clone()
    {
        return new TransformStamped(header, child_frame_id, transform);
    }
    public static TransformStamped fromJsonString(String json)
    {
        return fromMessage(new Message(json));
    }
    public static TransformStamped fromMessage(Message message)
    {
        return fromJsonObject(message.toJsonObject());
    }
    public static TransformStamped fromJsonObject(JsonObject object)
    {
        Header header = object.containsKey("header") ? Header.fromJsonObject(object.getJsonObject("header"))
            : new Header();
        String child_frame_id = object.containsKey("child_frame_id") ? object.getString("child_frame_id")
            : "";
        Transform transform = object.containsKey("transform") ?
            Transform.fromJsonObject(object.getJsonObject("transform")) : new Transform();
        return new TransformStamped(header, child_frame_id, transform);
    }
}
