package frc.robot.subsystems;

import org.northernforce.motors.NFRSparkMax;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.constants.CrabbyConstants;

public class BeachBallExtending {
    private NFRSparkMax max;
    private boolean startingState;
    private DigitalInput limitSwitch = new DigitalInput(CrabbyConstants.OffSeasonExtendingConstants.limitSwitch1);
    public BeachBallExtending(NFRSparkMax max) {
        this.max = max;
        startingState = limitSwitch.get();
    }
    public void run() {
        startingState = !limitSwitch.get();
        max.set(CrabbyConstants.OffSeasonExtendingConstants.extendSpeed * (startingState ? 1.0 : -1.0));


    }

    public boolean isExtended() {
        return limitSwitch.get();
    }

    public boolean isDone() {
        return limitSwitch.get() != startingState;
    }
    public void stop() {
        max.set(0);
    }
}
