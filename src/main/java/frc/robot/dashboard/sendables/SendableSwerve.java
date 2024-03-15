package frc.robot.dashboard.sendables;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.NTSendable;
import edu.wpi.first.networktables.NTSendableBuilder;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableValue;
import frc.robot.constants.CrabbyConstants;

public class SendableSwerve implements NTSendable, AutoCloseable {
    private NetworkTable table;
    private ObjectHolder<Translation2d[]> offsets =  new ObjectHolder<Translation2d[]>("wheelLocations", this::Translation2dToNetworkTableValue, null);
    private ObjectHolder<SwerveModuleState[]> states = new ObjectHolder<SwerveModuleState[]>("measuredStates", this::SwerveModuleStateToNetworkTableValue, null);
    private ObjectHolder<SwerveModuleState[]> desired = new ObjectHolder<SwerveModuleState[]>("desiredStates", this::SwerveModuleStateToNetworkTableValue, null);
    private ObjectHolder<Double> sizeLeftRight = new ObjectHolder<Double>("sizeLeftRight", NetworkTableValue::makeDouble, null);
    private ObjectHolder<Double> sizeFrontBack = new ObjectHolder<Double>("sizeFrontBack", NetworkTableValue::makeDouble, null);
    private ObjectHolder<Double> robotRotation = new ObjectHolder<Double>("robotRotation", NetworkTableValue::makeDouble, null);

    @Override
    public void close() {
        offsets.close();
        states.close();
        desired.close();
        sizeFrontBack.close();
        sizeLeftRight.close();
        robotRotation.close();
    }

    private NetworkTableValue Translation2dToNetworkTableValue(Translation2d[] offsets) {
        double[] toReturn = new double[8];
        for (int i = 0; i < 8; i += 2) {
            toReturn[i] = offsets[i/2].getX();
            toReturn[i+1] = offsets[i/2].getY();
        }
        return NetworkTableValue.makeDoubleArray(toReturn);
    }

    private NetworkTableValue SwerveModuleStateToNetworkTableValue(SwerveModuleState[] states) {
        double[] toReturn = new double[8];
        for (int i = 0; i < 8; i += 2) {
            toReturn[i] = states[i/4].angle.getRadians();
            toReturn[i+1] = states[i/4].speedMetersPerSecond;
        }
        return NetworkTableValue.makeDoubleArray(toReturn);
    }

    @Override
    public void initSendable(NTSendableBuilder builder) {
        builder.setSmartDashboardType("SwerveDriveBase");

        synchronized (this) {
            table = builder.getTable();
            synchronized (offsets) {
                offsets.init(table);
                offsets.setDefault(CrabbyConstants.DriveConstants.offsets);
            }
            synchronized (states) {
                states.init(table);
                states.setDefault(new SwerveModuleState[] {new SwerveModuleState(), new SwerveModuleState(), new SwerveModuleState(), new SwerveModuleState()});
            }
            synchronized (desired) {
                desired.init(table);
                desired.setDefault(new SwerveModuleState[] {new SwerveModuleState(), new SwerveModuleState(), new SwerveModuleState(), new SwerveModuleState()});
            }
            synchronized (sizeLeftRight) {
                sizeLeftRight.init(table);
                sizeLeftRight.setDefault(0.307975*2);
            }
            synchronized (sizeFrontBack) {
                sizeFrontBack.init(table);
                sizeFrontBack.setDefault(0.225425*2);
            }
            synchronized (robotRotation) {
                robotRotation.init(table);
                robotRotation.setDefault(0.0);
            }
        }
    }
    
}
