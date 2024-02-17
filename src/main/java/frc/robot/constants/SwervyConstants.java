package frc.robot.constants;

import edu.wpi.first.math.geometry.Translation2d;

public class SwervyConstants
{
    public static class Drive
    {
        public static final Translation2d[] offsets = new Translation2d[] {
            new Translation2d(0.581025, 0.581025),
            new Translation2d(0.581025, -0.581025),
            new Translation2d(-0.581025, 0.581025),
            new Translation2d(-0.581025, -0.581025)
        };
    }
}
