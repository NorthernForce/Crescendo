package org.northernforce.motors;

import org.northernforce.encoders.NFREncoder;

/**
 * This is an exception that is thrown when there is invalid input to the NFRMotorController's function to link encoders.
 */
public class MotorEncoderMismatchException extends Exception
{
    /**
     * The constructor for a motor encoder mismatch exceptions. Forms a simple message outlining the encoder and motor
     * types.
     * @param motor motor class/type
     * @param encoder encoder class/type
     */
    public MotorEncoderMismatchException(Class<? extends NFRMotorController> motor, Class<? extends NFREncoder> encoder)
    {
        super("Encoder "+ encoder.getName() + " could not be linked to " + motor.getName());
    }
}