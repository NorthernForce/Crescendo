package frc.robot.dashboard.sendables;

import frc.robot.dashboard.FWCDashboard;

public class StatusLightManager {
    public final StatusLight canTargetLight = new StatusLight("canTarget");
    public final StatusLight hasNoteLight = new StatusLight("hasNote");
    public final StatusLight shooterReadyLight = new StatusLight("shooterReady");
    public final StatusLight wristReadyLight = new StatusLight("wristReady");
    public final StatusLight wristManualLight = new StatusLight("wristManual");
    public final StatusLight aimReadyLight = new StatusLight("aimReady");
    public final StatusLight readyToShootLight = new StatusLight("readyToShoot");
    public final StatusLight closeShotEnabledLight = new StatusLight("closeShotEnabled");
    public final StatusLight ampShotEnabledLight = new StatusLight("ampShotEnabledLight");

    public StatusLightManager(FWCDashboard dashboard) {
        dashboard.addSendable("statusLights/" + canTargetLight.name, canTargetLight);
        dashboard.addSendable("statusLights/" + hasNoteLight.name, hasNoteLight);
        dashboard.addSendable("statusLights/" + shooterReadyLight.name, shooterReadyLight);
        dashboard.addSendable("statusLights/" + wristReadyLight.name, wristReadyLight);
        dashboard.addSendable("statusLights/" + wristManualLight.name, wristManualLight);
        dashboard.addSendable("statusLights/" + aimReadyLight.name, aimReadyLight);
        dashboard.addSendable("statusLights/" + readyToShootLight.name, readyToShootLight);
        dashboard.addSendable("statusLights/" + closeShotEnabledLight.name, closeShotEnabledLight);
        dashboard.addSendable("statusLights/" + ampShotEnabledLight.name, ampShotEnabledLight);
    }

    public void setCanTargetLight(boolean value) {
        canTargetLight.set(value);
    }
    public void setHasNoteLight(boolean value) {
        hasNoteLight.set(value);
    }
    public void setShooterReadyLight(boolean value) {
        shooterReadyLight.set(value);
    }
    public void setWristReadyLight(boolean value) {
        wristReadyLight.set(value);
    }
    public void setWristManualLight(boolean value) {
        wristManualLight.set(value);
    }
    public void setAimReadyLight(boolean value) {
        aimReadyLight.set(value);
    }
    public void setReadyToShootLight(boolean value) {
        readyToShootLight.set(value);
    }
    public void setCloseShotEnabledLight(boolean value) {
        closeShotEnabledLight.set(value);
    }
    public void setAmpShotEnabledLight(boolean value) {
        ampShotEnabledLight.set(value);
    }

    public void updateAll() {
        canTargetLight.update();
        hasNoteLight.update();
        shooterReadyLight.update();
        wristReadyLight.update();
        wristManualLight.update();
        aimReadyLight.update();
        readyToShootLight.update();
        closeShotEnabledLight.update();
        ampShotEnabledLight.update();
    }
}