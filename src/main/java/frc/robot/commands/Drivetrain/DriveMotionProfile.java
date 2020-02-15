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
import frc.robot.subsystems.Drivetrain;
import frcteam3255.robotbase.SN_MotionProfile;

public class DriveMotionProfile extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Drivetrain drivetrain;

    /**
     * Creates a new DriveMotionProfile.
     */
    BufferedTrajectoryPointStream pointsLeft;
    BufferedTrajectoryPointStream pointsRight;

    String leftFilename;
    String rightFilename;

    public DriveMotionProfile(Drivetrain a_drivetrain, String a_leftName, String a_rightName) {
        pointsLeft = new BufferedTrajectoryPointStream();
        pointsRight = new BufferedTrajectoryPointStream();
        leftFilename = a_leftName;
        rightFilename = a_rightName;

        drivetrain = a_drivetrain;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(a_drivetrain);
        reload();

    }

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

        drivetrain.resetEncoderCounts();
        drivetrain.startMotionProfile(pointsLeft, pointsRight);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        drivetrain.resetMotionProfile();

    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return drivetrain.isMotionProfileFinished();
    }
}