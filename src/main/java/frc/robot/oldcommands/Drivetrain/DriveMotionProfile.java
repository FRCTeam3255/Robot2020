/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.RobotPreferences;
import frcteam3255.robotbase.SN_MotionProfile;

public class DriveMotionProfile extends CommandBase {

    /**
     * Creates a new DriveMotionProfile.
     */

    SN_MotionProfile profile;

    public DriveMotionProfile(String a_leftName, String a_rightName) {

        profile = new SN_MotionProfile(a_leftName, a_rightName);
        addRequirements(RobotContainer.drivetrain);
        reload();
    }

    public void reload() {
        profile.reload();
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        SN_MotionProfile.setSensorUnitsPerTick(RobotPreferences.motProfSensorUnitsPerFt.getValue());
        profile.reload();
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