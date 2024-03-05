package frc.robot.utils;

import frc.robot.subsystems.WristJoint;
import frc.robot.subsystems.WristJoint.PositioningType;

public class test {
    public static void main(String args[])
    {
        System.out.println(WristJoint.getSpeakerAngle(PositioningType.Regressive,10));
    }
}