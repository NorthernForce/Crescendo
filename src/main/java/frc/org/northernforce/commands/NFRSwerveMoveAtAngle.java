// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package org.northernforce.commands;

import org.northernforce.subsystems.drive.NFRSwerveDrive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.Command;

public class NFRSwerveMoveAtAngle extends Command {
	private final NFRSwerveDrive drive;
	private final Rotation2d angle;
	private final double speed;
	private final boolean optimize;
	private final boolean endWithDistance;
	private final double distanceMeters;
	private final NFRSwerveModuleSetState[] setStateCommands;
	private Pose2d initialPos;
	
	/**
	 * Moves the robot at a given angle at a given speed (angle is driver relative)
	 * Command will not automatically end
	 * @param drive The swerve drive subsystem
	 * @param angle The angle to drive at
	 * @param speed The speed to drive at
	 * @param setStateCommands The commands to set module states
	 * @param optimize If true will run optimize
	 */
	public NFRSwerveMoveAtAngle(NFRSwerveDrive drive, Rotation2d angle, double speed, NFRSwerveModuleSetState[] setStateCommands, boolean optimize) {
		this.drive = drive;
		this.angle = angle;
		this.speed = speed;
		this.setStateCommands = setStateCommands;
		this.optimize = optimize;
		endWithDistance = false;
		distanceMeters = 0;
		// Use addRequirements() here to declare subsystem dependencies.
		addRequirements(drive);
	}

	/**
	 * Moves the robot at a given angle at a given speed (angle is driver relative)
	 * Command will end after specified distance
	 * @param drive The swerve drive subsystem
	 * @param angle The angle to drive at
	 * @param speed The speed to drive at
	 * @param setStateCommands The commands to set module states
	 * @param optimize If true will run optimize
	 * @param distanceMeters If this parameter exists the command will end after traveling the specified distance
	 */
	public NFRSwerveMoveAtAngle(NFRSwerveDrive drive, Rotation2d angle, double speed, NFRSwerveModuleSetState[] setStateCommands, boolean optimize, double distanceMeters) {
		this.drive = drive;
		this.angle = angle;
		this.speed = speed;
		this.setStateCommands = setStateCommands;
		this.optimize = optimize;
		this.distanceMeters = distanceMeters;
		endWithDistance = true;
		// Use addRequirements() here to declare subsystem dependencies.
		addRequirements(drive);
	}

	// Called when the command is initially scheduled.
	@Override
	public void initialize() {
		initialPos = drive.getOdometryPose();
		for (NFRSwerveModuleSetState command : setStateCommands) {
			command.schedule();
		}
	}

	// Called every time the scheduler runs while the command is scheduled.
	@Override
	public void execute() {
		ChassisSpeeds speeds = ChassisSpeeds.fromFieldRelativeSpeeds(angle.getCos()*speed, angle.getSin()*speed, 0, drive.getRotation()); 
		SwerveModuleState[] states = drive.toModuleStates(speeds);
		for (int i = 0; i < states.length; i++){
			setStateCommands[i].setTargetState(optimize ? SwerveModuleState.optimize(states[i],
				drive.getModules()[i].getRotation()) : states[i]);
		}
	}

	// Called once the command ends or is interrupted.
	@Override
	public void end(boolean interrupted) {}

	// Returns true when the command should end.
	@Override
	public boolean isFinished() {
		if (endWithDistance) {
			return initialPos.getTranslation().getDistance(drive.getOdometryPose().getTranslation()) >= distanceMeters;
		} else return false;
	}
}