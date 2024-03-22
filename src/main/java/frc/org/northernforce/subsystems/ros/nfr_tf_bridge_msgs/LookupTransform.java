package org.northernforce.subsystems.ros.nfr_tf_bridge_msgs;

import javax.json.Json;
import javax.json.JsonObject;

import org.northernforce.subsystems.ros.geometry_msgs.TransformStamped;

import org.northernforce.subsystems.ros.primitives.Time;
import edu.wpi.rail.jrosbridge.services.ServiceRequest;
import edu.wpi.rail.jrosbridge.services.ServiceResponse;

public class LookupTransform
{
    public static class Request extends ServiceRequest
    {
        public final String base_frame;
        public final String child_frame;
        public final Time time;
        public Request()
        {
            this("", "", new Time());
        }
        public Request(String base_frame, String child_frame, Time time)
        {
            super(Json.createObjectBuilder()
                .add("base_frame", base_frame)
                .add("child_frame", child_frame)
                .add("time", time.toJsonObject())
                .build(), "nfr_tf_bridge_msgs/LookupTransform");
            this.base_frame = base_frame;
            this.child_frame = child_frame;
            this.time = time;
        }
        @Override
        public Request clone()
        {
            return new Request(base_frame, child_frame, time);
        }
        public static Request fromJsonString(String jsonString)
        {
			return fromServiceRequest(new ServiceRequest(jsonString));
		}
		public static Request fromServiceRequest(ServiceRequest req)
        {
			return fromJsonObject(req.toJsonObject());
		}
		public static Request fromJsonObject(JsonObject jsonObject)
        {
            String base_frame = jsonObject.containsKey("base_frame")
                ? jsonObject.getString("base_frame") : "";
            String child_frame = jsonObject.containsKey("child_frame")
                ? jsonObject.getString("child_frame") : "";
            Time time = jsonObject.containsKey("time")
                ? Time.fromJsonObject(jsonObject.getJsonObject("time")) : new Time();
			return new Request(base_frame, child_frame, time);
		}
    }
    public static class Response extends ServiceResponse
    {
        public final TransformStamped transform;
        public final boolean success;
        public Response()
        {
            this(new TransformStamped(), false);
        }
        public Response(TransformStamped transform, boolean success)
        {
            super(Json.createObjectBuilder()
                .add("transform", transform.toJsonObject())
                .add("success", success)
                .build(), "nfr_tf_bridge_msgs/LookupTransform", true);
            this.transform = transform;
            this.success = success;
        }
        @Override
        public Response clone()
        {
            return new Response(transform, success);
        }
        public static Response fromJsonString(String jsonString)
        {
			return fromServiceResponse(new ServiceResponse(jsonString, true));
		}
		public static Response fromServiceResponse(ServiceResponse response)
        {
			return Response.fromJsonObject(response.toJsonObject());
		}
		public static Response fromJsonObject(JsonObject jsonObject)
        {
            TransformStamped transform = jsonObject.containsKey("transform")
                ? TransformStamped.fromJsonObject(jsonObject.getJsonObject("transform")) : new TransformStamped();
            boolean success = jsonObject.containsKey("success")
                ? jsonObject.getBoolean("success") : false;
			return new Response(transform, success);
		}
    }
}
