package frc.robot.subsystems.swerve;

import java.util.Optional;

import org.northernforce.encoders.NFRAbsoluteEncoder;
import org.northernforce.motors.NFRMotorController;
import org.northernforce.subsystems.drive.swerve.NFRSwerveModule;

public class SwerveModule extends NFRSwerveModule {
    /**
     * Creates a new NFRSwerveModule.
     * @param config the configuration for the swerve module.
     * @param driveController the drive controller.
     * @param turnController the turn controller.
     * @param externalEncoder the external encoder, if not linked directly to the turnController.
     */
    public SwerveModule(NFRSwerveModuleConfiguration config, NFRMotorController driveController,
        NFRMotorController turnController, Optional<NFRAbsoluteEncoder> externalEncoder)
    {
        super(config, driveController, turnController, externalEncoder);
    }
    public NFRMotorController getDriveController()
    {
        return driveController;
    }
    public NFRMotorController getTurnController()
    {
        return turnController;
    }
}
