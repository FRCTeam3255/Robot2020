/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frcteam3255.robotbase.SN_MotionProfile;

public class DriveMotionProfile extends CommandBase {

    /**
     * Creates a new DriveMotionProfile.
     */

    SN_MotionProfile profile;

    /*
     * TODO: A SN_MotionProfile constructor should take two filenames, and then
     * internally, it can deal with initBuffer, etc. This class will take the two
     * filenames and create the SN_MotionProfile internally.
     */
    public DriveMotionProfile(String a_leftName, String a_rightName) {

        profile = new SN_MotionProfile(a_leftName, a_rightName);
        addRequirements(RobotContainer.drivetrain);

        // TODO: The SN_MotionProfile constructor, will internally call it's reload
        // method, so not needed here
        reload();
    }

    /*
     * TODO: Once the SN_MotionProfile objects have their own reload methods, this
     * method can just call the reload method of the SN_MotionProfile class.
     */
    public void reload() {
        profile.reload();
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {

        RobotContainer.drivetrain.resetEncoderCounts();
        RobotContainer.drivetrain.startMotionProfile(profile.pointsLeft, profile.pointsRight);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        RobotContainer.drivetrain.resetMotionProfile();

    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return RobotContainer.drivetrain.isMotionProfileFinished();
    }
}