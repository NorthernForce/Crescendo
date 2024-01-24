package Sensors;



import edu.wpi.first.wpilibj.DigitalInput;


public class BeamBreak 
{
    private DigitalInput initialConveyorSensor;
    private int pinNum;
    public BeamBreak(int pinNum)
    {
        initialConveyorSensor = new DigitalInput(pinNum);
        this.pinNum = pinNum;
    }
    public boolean beamIntact()
    {
        return initialConveyorSensor.get();
    }

    // public BeamStatus GetStatus()
    // {
    //     return (initialConveyorSensor.get() == true ? BeamStatus.Intact : BeamStatus.Broken);
    // }

    public int getPinNum()
    {
        return pinNum;
    }
}
