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
import frcteam3255.robotbase.Preferences.SN_IntPreference;

public class DriveToBall extends CommandBase {
  private boolean timeoutsEnabled;
  private int timer;
  private boolean timedOut;
  private SN_IntPreference numTimeout;
  private SN_IntPreference numBalls;
  private boolean success;
  private int counted;
  private boolean justCounted;

  public enum FinishReason {
    SUCCESS, TIMED_OUT, NOT_FINISHED
  };

  public FinishReason finishReason = FinishReason.NOT_FINISHED;

  /**
   * Creates a new DriveToBall.
   **/

  public DriveToBall(boolean a_timeoutsEnabled, SN_IntPreference a_numTimeout, SN_IntPreference a_numBalls) {
    timeoutsEnabled = a_timeoutsEnabled;
    numTimeout = a_numTimeout;
    numBalls = a_numBalls;
    addRequirements(RobotContainer.drivetrain);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    timedOut = false;
    success = false;
    timer = 0;
    counted = 0;
    justCounted = false;
  }

  // Called every time the scheduler runs while the command is scheduled.

  @Override

  public void execute() {
    RobotContainer.drivetrain.arcadeDrive(.75, RobotContainer.vision.getX() * RobotPreferences.ballP.getValue());
    if (timeoutsEnabled) {
      timer++;

      if (timer >= numTimeout.getValue()) {
        timedOut = true;
      } else if (counted >= numBalls.getValue()) {
        success = true;
      }
    }
    if (RobotContainer.intake.getCollectionSwitch()) {
      if (!justCounted) {
        counted++;
        justCounted = true;
      }
    } else {
      justCounted = false;
    }
  }

  // Called once the command ends or is interrupted.

  @Override

  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.

  @Override

  public boolean isFinished() {
    // // return true if X error and hood are within tolerance
    if (timedOut) {

      finishReason = FinishReason.TIMED_OUT;

      return true;
    }

    // return true if no vision for timeout
    if (success) {

      finishReason = FinishReason.SUCCESS;
      return true;
    }

    return false;
  }
}