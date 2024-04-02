package frc.robot.subsystems;

import java.awt.geom.Point2D;
import java.util.Optional;
import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Rotation2d;

public class testSpeakerAngle {
    public static void main(String[] args)
    {
        
        Point2D.Double robot = new Point2D.Double(2,0);
        Point2D.Double aprilTag4 = new Point2D.Double(0,2);
        Point2D.Double speakerPos = new Point2D.Double(0.8,aprilTag4.getY());
        Supplier<Rotation2d> degToAprilTag4 = () -> Rotation2d.fromRadians(Math.atan((aprilTag4.getY()-robot.getY())/(robot.getX()-aprilTag4.getX()))); //TODO find out how to get correct position
        double lastRecordedDistance = Math.sqrt(Math.pow(aprilTag4.getX()-robot.getX(),2)+Math.pow(aprilTag4.getY()-robot.getY(),2));

        double distanceToSpeaker =Math.sqrt(Math.pow(lastRecordedDistance,2)+Math.pow(speakerPos.getX()-aprilTag4.getX(),2)-2.0*lastRecordedDistance*(speakerPos.getX()-aprilTag4.getX())*(Math.cos(degToAprilTag4.get().getRadians())));
    


        double changeDegToSpeaker = ((Math.abs(degToAprilTag4.get().getDegrees())/(double)degToAprilTag4.get().getDegrees())*
            Math.acos((Math.pow(speakerPos.getX()-aprilTag4.getX(),2.0)-Math.pow(lastRecordedDistance,2.0)-
            Math.pow(distanceToSpeaker,2.0))/(double)(-2.0*lastRecordedDistance*distanceToSpeaker)))*(180/Math.PI);
            
            System.out.println("Change of degree with speaker: " + changeDegToSpeaker + "\nDistance to speaker: " + distanceToSpeaker + "\nDistance to April Tag 4: " + lastRecordedDistance + "\nDegree to April Tag 4: " + degToAprilTag4.get().getDegrees());
        Supplier<Optional<Rotation2d>> degToSpeaker = () -> (Optional.of(Rotation2d.fromDegrees(degToAprilTag4.get().getDegrees()+changeDegToSpeaker)));
        System.out.println(degToSpeaker.get().get().getDegrees());
    }
}
