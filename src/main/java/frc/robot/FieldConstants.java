package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;

public class FieldConstants
{
    public static class NotePositions
    {
        /**
         * All coordinates are blue origin relative (source corner)
         */
        /**
         * AMP TO SOURCE
         */
        public static Translation3d[] blueAutoNotes = new Translation3d[] {
            new Translation3d(2.945, 7.026, 0),
            new Translation3d(2.945, 5.5785, 0),
            new Translation3d(2.945, 4.1305, 0)
        };
        /**
         * AMP TO SOURCE
         */
        public static Translation3d[] centerAutoNotes = new Translation3d[] {
            new Translation3d(8.317, 7.496, 0),
            new Translation3d(8.317, 5.81, 0),
            new Translation3d(8.317, 4.134, 0),
            new Translation3d(8.317, 2.457, 0),
            new Translation3d(8.317, 0.781, 0),
        };
        /**
         * AMP TO SOURCE
         */
        public static Translation3d[] redAutoNotes = new Translation3d[] {
            new Translation3d(13.694, 7.026, 0),
            new Translation3d(13.694, 5.5785, 0),
            new Translation3d(13.694, 4.1305, 0)
        };
    }
    public static class SpeakerConstants
    {
        public static final double speakerHeight = Units.inchesToMeters(57);
        public static final Translation2d redSpeaker = new Translation2d(16.579342, 5.547867999999999);
        public static final Translation2d blueSpeaker = new Translation2d(-0.038099999999999995, 5.547867999999999);
    }
    public static final double fieldWidth = 16.4846;
}