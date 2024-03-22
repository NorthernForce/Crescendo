package org.northernforce.subsystems.ros.rosgraph_msgs;

import javax.json.Json;
import javax.json.JsonObject;

import edu.wpi.rail.jrosbridge.messages.Message;
import org.northernforce.subsystems.ros.primitives.Time;

public class Clock extends Message
{
    public final Time clock;
    public Clock()
    {
        this(new Time());
    }
    public Clock(Time clock)
    {
        super(Json.createObjectBuilder()
            .add("clock", clock.toJsonObject())
            .build(), "rosgraph_msgs/Clock");
        this.clock = clock;
    }
    @Override
    public Clock clone()
    {
        return new Clock(clock);
    }
    public static Clock fromJsonString(String json)
    {
        return fromMessage(new Message(json));
    }
    public static Clock fromMessage(Message message)
    {
        return fromJsonObject(message.toJsonObject());
    }
    public static Clock fromJsonObject(JsonObject object)
    {
        Time clock = object.containsKey("clock") ? Time.fromJsonObject(object.getJsonObject("clock"))
            : new Time();
        return new Clock(clock);
    }
}
