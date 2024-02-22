package frc.robot.constants;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

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
    public static class IndexerConstants
    {
        public static final double indexerShootSpeed = 1;
    }
    public static class ShooterConstants
    {
        public static final TalonFXConfiguration motorConfig = new TalonFXConfiguration();
        static {
            Slot0Configs slot0Config = new Slot0Configs().withKP(0).withKI(0).withKD(0);
            motorConfig.withSlot0(slot0Config);
        }
    }
}
