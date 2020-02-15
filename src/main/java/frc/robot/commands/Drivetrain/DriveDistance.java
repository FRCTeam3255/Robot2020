/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class DriveDistance extends CommandBase {
  private final Drivetrain drivetrain;
  private SN_DoublePreference distance;

  /**
   * Creates a new DriveDistance.
   **/

  public DriveDistance(Drivetrain a_drivetrain, SN_DoublePreference a_distance) {
    drivetrain = a_drivetrain;
    distance = a_distance;
    addRequirements(a_drivetrain);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrain.resetEncoderCounts();
    drivetrain.driveDistance(distance.getValue());
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
    return false;
  }
}