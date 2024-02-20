package frc.robot.constants;

import edu.wpi.first.math.geometry.Translation2d;

public class CrabbyConstants {
    public static class Drive {
        public static final Translation2d[] offsets = new Translation2d[] {
            new Translation2d(0.225425, 0.307975),
            new Translation2d(0.225425, -0.307975),
            new Translation2d(-0.225425, 0.307975),
            new Translation2d(-0.225425, -0.307975)
        };
    }
}
