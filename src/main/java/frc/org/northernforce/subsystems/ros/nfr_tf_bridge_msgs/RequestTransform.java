package org.northernforce.subsystems.ros.nfr_tf_bridge_msgs;

import javax.json.Json;

import edu.wpi.rail.jrosbridge.messages.Message;

public class RequestTransform extends Message
{
    public final double frequency;
    public final String base_frame;
    public final String child_frame;
    public final String publish_topic;
    public RequestTransform()
    {
        this(0.0, "", "", "");
    }
    public RequestTransform(double frequency, String base_frame, String child_frame, String publish_topic)
    {
        super(Json.createObjectBuilder()
            .add("frequency", frequency)
            .add("base_frame", base_frame)
            .add("child_frame", child_frame)
            .add("publish_topic", publish_topic)
            .build(), "nfr_tf_bridge_msgs/RequestTransform");
        this.frequency = frequency;
        this.base_frame = base_frame;
        this.child_frame = child_frame;
        this.publish_topic = publish_topic;
    }
}