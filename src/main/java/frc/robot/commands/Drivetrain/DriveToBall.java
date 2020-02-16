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
  private SN_IntPreference numBalls;
  private int counted;
  private boolean priorSwitch;
  private boolean autoEnabled;

  public enum FinishReason {
    SUCCESS, TIMED_OUT, NOT_FINISHED
  };

  public FinishReason finishReason = FinishReason.NOT_FINISHED;

  /**
   * Creates a new DriveToBall.
   **/

  public DriveToBall(boolean a_autoEnabled, SN_IntPreference a_numBalls) {
    autoEnabled = a_autoEnabled;
    numBalls = a_numBalls;
    addRequirements(RobotContainer.drivetrain);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    priorSwitch = RobotContainer.intake.getCollectionSwitch();
    counted = 0;
  }

  // Called every time the scheduler runs while the command is scheduled.

  @Override

  public void execute() {
    RobotContainer.drivetrain.arcadeDrive(RobotPreferences.ballSpeed.getValue(),
        RobotContainer.vision.getX() * RobotPreferences.ballP.getValue());

    if (RobotContainer.intake.getCollectionSwitch() != priorSwitch) {

      counted++;
      priorSwitch = RobotContainer.intake.getCollectionSwitch();
    }
  }

  // Called once the command ends or is interrupted.

  @Override

  public void end(boolean interrupted) {
    // only way to tell if .withtimeout() is timed out is through interrupted
    if (interrupted) {

      finishReason = FinishReason.TIMED_OUT;
    }
  }

  // Returns true when the command should end.

  @Override

  public boolean isFinished() {
    if (autoEnabled && (counted >= numBalls.getValue())) {

      finishReason = FinishReason.SUCCESS;
      return true;
    }

    return false;
  }
}