/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import java.io.IOException;

import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frcteam3255.robotbase.SN_MotionProfile;

public class MotionProfile extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Drivetrain m_drivetrain;

    /**
     * Creates a new MotionProfile.
     */
    BufferedTrajectoryPointStream pointsLeft;
    BufferedTrajectoryPointStream pointsRight;

    String leftFilename;
    String rightFilename;


    public MotionProfile(Drivetrain subsystem, String leftName, String rightName) {
        pointsLeft = new BufferedTrajectoryPointStream();
        pointsRight = new BufferedTrajectoryPointStream();
        leftFilename = leftName;
        rightFilename = rightName;

        m_drivetrain = subsystem;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(subsystem);
        reload();


    }
    

    public void reload() {

        try {
            m_drivetrain.initBuffer(pointsLeft, SN_MotionProfile.reader(leftFilename),
                    SN_MotionProfile.count(leftFilename));
        } catch (IOException e) {
            System.out.println("initBuffer failed :(. Is your file in deploy?");
            e.printStackTrace();
        }
        try {
            m_drivetrain.initBuffer(pointsRight, SN_MotionProfile.reader(rightFilename),
                    SN_MotionProfile.count(rightFilename));
        } catch (IOException e) {
            System.out.println("initBuffer failed :(. Is your file in deploy?");
            e.printStackTrace();
        }

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {

        m_drivetrain.resetEncoderCounts();
        m_drivetrain.startMotionProfile(pointsLeft, pointsRight);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_drivetrain.resetMotionProfile();

    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return m_drivetrain.isMotionProfileFinished();
    }
}