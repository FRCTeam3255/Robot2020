/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Drivetrain;

import java.io.IOException;

import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frcteam3255.robotbase.SN_MotionProfile;

public class DriveMotionProfile extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })

    /**
     * Creates a new DriveMotionProfile.
     */
    BufferedTrajectoryPointStream pointsLeft;
    BufferedTrajectoryPointStream pointsRight;

    String leftFilename;
    String rightFilename;

    /*
     TODO: A SN_MotionProfile constructor should take two filenames, and then internally, it can deal with initBuffer, etc.
     This class will take the two filenames and create the SN_MotionProfile internally.
    */
    public DriveMotionProfile(String a_leftName, String a_rightName) {
        pointsLeft = new BufferedTrajectoryPointStream();
        pointsRight = new BufferedTrajectoryPointStream();
        leftFilename = a_leftName;
        rightFilename = a_rightName;

        addRequirements(RobotContainer.drivetrain);

        // TODO: The SN_MotionProfile constructor, will internally call it's reload method, so not needed here
        reload();
    }

    /*
        TODO: Once the SN_MotionProfile objects have their own reload methods, this method can just call the reload method
        of the SN_MotionProfile class.
    */
    public void reload() {
        try {
            SN_MotionProfile.initBuffer(pointsLeft, SN_MotionProfile.reader(leftFilename),
                    SN_MotionProfile.count(leftFilename));
        } catch (IOException e) {
            System.out.println("initBuffer failed :(. Is your file in deploy?");
            e.printStackTrace();
        }
        try {
            SN_MotionProfile.initBuffer(pointsRight, SN_MotionProfile.reader(rightFilename),
                    SN_MotionProfile.count(rightFilename));
        } catch (IOException e) {
            System.out.println("initBuffer failed :(. Is your file in deploy?");
            e.printStackTrace();
        }

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {

        RobotContainer.drivetrain.resetEncoderCounts();
        // TODO: startMotionProfile should take an SN_MotionProfile object, and internally get the left and right points
        RobotContainer.drivetrain.startMotionProfile(pointsLeft, pointsRight);
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