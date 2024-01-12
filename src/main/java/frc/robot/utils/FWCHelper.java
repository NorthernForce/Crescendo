// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.PubSubOption;
import edu.wpi.first.networktables.Publisher;
import edu.wpi.first.networktables.StringPublisher;
import edu.wpi.first.networktables.Topic;

/** To publish a value first create the publisher `FWCHelper.table.get____Topic(NAME).publish()` and store the return value.
 * Where NAME is the ket value for the data;
 * Where ____ is the type of value to publish.
 * Call .set on the publisher with the value to set
*/
public class FWCHelper {
    public static final NetworkTableInstance inst = NetworkTableInstance.getDefault();
    public static final NetworkTable table = inst.getTable("FWCTable");

    //sort of example code
    // public static final StringPublisher stringPublisher = table.getStringTopic("Msg").publish();
    // public static Map<String, Publisher> publishers = new HashMap<String, Publisher>();

    // public static void postString(String key, String value) {
    //     if (!publishers.containsKey(key)) {
    //         publishers.put(key, table.getStringTopic(key).publish());
    //     }
    //     publishers.get(key).set(value);
    // }
}
