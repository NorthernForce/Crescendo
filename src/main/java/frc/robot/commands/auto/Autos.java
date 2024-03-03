package frc.robot.commands.auto;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import org.northernforce.commands.NFRSwerveModuleSetState;
import org.northernforce.subsystems.drive.NFRSwerveDrive;

import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.RunIntake;
import frc.robot.subsystems.Intake;
import frc.robot.utils.AutonomousRoutine;

public class Autos
{   
    public static List<Class<? extends SequentialCommandGroup>> getAllAutos()
    {
        /*
        autos added here must inherit SequentialCommandGroup and have a getRoutine
        method that returns an AutonomousRoutine (refer to other autos as an example)
        */
        return List.of(
            S1CSV1.class,
            S1CSV2.class,
            S1LS.class,
            S1LV1.class,
            S1LV2.class,
            S2CS.class,
            S2CV1.class,
            S2CV2.class,
            S2LSV1.class,
            S2SV1.class,
            S2SV2.class,
            S2T.class,
            S3CS.class,
            S3CV1.class,
            S3CV2.class,
            S3LSV1.class,
            S3SV1.class,
            S3SV2.class
        );
    }
    /**
     * gets the routines
     * 
     * this method returns the AutonomousRoutines of the class and is primairly
     * here simply to remove code duplication
     * 
     * @param drive the NFRSwerveDrive to use
     * @param setStateCommands list of setStateCommands to use
     * @param poseSupplier give the autos pose predictions
     * @param controller holonomic drive controller
     * @param ignoreCommands whether to ignore commands (e.g. run intake) or not
     * @return list of AutonomousRoutines
     */
    private static List<AutonomousRoutine> getRawRoutines(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands,
        Supplier<Pose2d> poseSupplier, PPHolonomicDriveController controller, boolean ignoreCommands)
    {
        ArrayList<AutonomousRoutine> autoList = new ArrayList<>();
        for (var auto : getAllAutos())
        {
            Method getRoutineMethod;
            try
            {
                getRoutineMethod = auto.getDeclaredMethod("getRoutine",
                    NFRSwerveDrive.class,
                    NFRSwerveModuleSetState[].class,
                    Supplier.class,
                    PPHolonomicDriveController.class,
                    BooleanSupplier.class,
                    boolean.class);
            }
            catch (NoSuchMethodException e)
            {
                e.printStackTrace();
                autoList.add(new AutonomousRoutine("[ERROR] Failed to find getRoutine in class: " + auto.getName(),
                    () -> new Pose2d(), new InstantCommand())); // ignore auto and alert driver
                continue;
            }
            try
            {
                autoList.add((AutonomousRoutine)getRoutineMethod.invoke(null,
                    drive,
                    setStateCommands,
                    poseSupplier,
                    controller,
                    (BooleanSupplier)() -> DriverStation.getAlliance().orElse(Alliance.Red) == Alliance.Red,
                    ignoreCommands));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                autoList.add(new AutonomousRoutine("[ERROR] Failed to invoke getRoutine on: " + auto.getName(),
                    () -> new Pose2d(), new InstantCommand())); // ignore auto and alert driver
            }
        }
        return autoList;
    }

    /**
     * Gets the list of autonomous routines.
     * Includes S1.CS.V1, S1.CS.V2
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @return an list of AutonomousRoutines
     */
    public static List<AutonomousRoutine> getRoutines(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands,
        Supplier<Pose2d> poseSupplier, PPHolonomicDriveController controller)
    {
        return getRawRoutines(drive, setStateCommands, poseSupplier, controller, true);
    }
    /**
     * Gets the list of autonomous routines.
     * Includes S1.CS.V1, S1.CS.V2
     * @param drive the drive subsystem
     * @param setStateCommands the commands to run each module
     * @param poseSupplier the supplier for pose estimation
     * @param controller the controller for following the path
     * @param intake the intake subsystem to be used
     * @param intakeSpeed speed at which to run the intake
     * @return an list of AutonomousRoutines
     */
    public static List<AutonomousRoutine> getRoutines(NFRSwerveDrive drive, NFRSwerveModuleSetState[] setStateCommands,
        Supplier<Pose2d> poseSupplier, PPHolonomicDriveController controller, Intake intake, double intakeSpeed)
    {
        NamedCommands.registerCommand("intake", new RunIntake(intake, intakeSpeed));
        return getRawRoutines(drive, setStateCommands, poseSupplier, controller, false);
    }
}