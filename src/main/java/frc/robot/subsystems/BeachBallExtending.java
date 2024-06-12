package frc.robot.subsystems;

import org.northernforce.motors.NFRSparkMax;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.constants.CrabbyConstants;

public class BeachBallExtending {
    private NFRSparkMax max;
    private DigitalInput limitSwitch = new DigitalInput(CrabbyConstants.OffSeasonExtendingConstants.limitSwitch);
    public BeachBallExtending(NFRSparkMax max) {
        this.max = max;
    }
    public void run() {
        max.set(CrabbyConstants.OffSeasonExtendingConstants.extendSpeed * (isExtended() ? 1.0 : -1.0));
    }

    public boolean isExtended() {
        return limitSwitch.get();
    }

}
