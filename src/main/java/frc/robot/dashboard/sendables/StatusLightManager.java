package frc.robot.dashboard.sendables;

import frc.robot.dashboard.Dashboard;

public class StatusLightManager {
    private final StatusLight canTargetLight = new StatusLight("canTarget");
    private final StatusLight hasNoteLight = new StatusLight("hasNote");
    private final StatusLight shooterReadyLight = new StatusLight("shooterReady");
    private final StatusLight wristReadyLight = new StatusLight("wristReady");
    private final StatusLight wristManualLight = new StatusLight("wristManual");
    private final StatusLight aimReadyLight = new StatusLight("aimReady");
    private final StatusLight readyToShootLight = new StatusLight("readyToShoot");
    private final StatusLight closeShotEnabledLight = new StatusLight("closeShotEnabled");
    private final StatusLight ampShotEnabledLight = new StatusLight("ampShotEnabledLight");

    public StatusLightManager(Dashboard dashboard) {
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
}