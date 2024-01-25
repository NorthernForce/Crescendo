package frc.robot.sensors;



import edu.wpi.first.wpilibj.DigitalInput;


public class NFRBeamBreak 
{
    private DigitalInput initialConveyorSensor;
    private int pinNum;
    public NFRBeamBreak(int pinNum)
    {
        initialConveyorSensor = new DigitalInput(pinNum);
        this.pinNum = pinNum;
    }
    public boolean beamIntact()
    {
        return initialConveyorSensor.get();
    }
    public boolean beamBroken()
    {
        return !initialConveyorSensor.get();
    }

    public BeamStatus GetStatus()
    {
        return (initialConveyorSensor.get() == true ? BeamStatus.Intact : BeamStatus.Broken);
    }

    public int getPinNum()
    {
        return pinNum;
    }

    public enum BeamStatus{
        Intact,
        Broken
    }
}
