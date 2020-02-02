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
import frc.robot.subsystems.MotionProfile;
import frcteam3255.robotbase.SN_MotionProfile;

public class DriveMotionProfile extends CommandBase {
    @SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.SingularField" })
    private final Drivetrain m_drivetrain;
    private MotionProfile m_motion;
    /**
     * Creates a new DriveMotionProfile.
     */

    public DriveMotionProfile(Drivetrain subsystem, MotionProfile motion) {
        m_motion = motion;
        m_drivetrain = subsystem;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(subsystem);
    }
    // Called when the command is initially scheduled.
    @Override
    public void initialize() {

        m_drivetrain.resetEncoderCounts();
        m_drivetrain.startMotionProfile(m_motion);
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