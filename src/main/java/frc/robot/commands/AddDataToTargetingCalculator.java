package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.TargetingCalculator;

public class AddDataToTargetingCalculator extends Command {
    private TargetingCalculator calculator;
    private DoubleSupplier distance;
    private DoubleSupplier speed;
    
    public AddDataToTargetingCalculator(TargetingCalculator calculator, DoubleSupplier distance, DoubleSupplier speed){
        this.calculator = calculator;
        this.distance = distance;
        this.speed = speed;
    }

    @Override
    public void initialize(){
        calculator.addData(distance.getAsDouble(), speed.getAsDouble());
    }
}