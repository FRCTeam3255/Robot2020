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
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Vision;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;
import frcteam3255.robotbase.Preferences.SN_IntPreference;

public class DriveToBall extends CommandBase {
  private final Drivetrain m_drivetrain;
  private final Vision m_vision;
  private final Intake m_intake;
  private boolean m_timeout;
  private int timer;
  private boolean timedOut = false;
  private SN_IntPreference m_numTimeout;
  private SN_IntPreference m_numBalls;
  private boolean success;
  private int counted = 0;
  private boolean justCounted = false;

  public enum FinishReason {
    SUCCESS, TIMED_OUT, NOT_FINISHED
  };

  public FinishReason finishReason = FinishReason.NOT_FINISHED;

  /**
   * Creates a new DriveToBall.
   **/

  public DriveToBall(Drivetrain drivetrain, Vision vision, Intake intake, boolean isTimeout,
      SN_IntPreference numTimeout, SN_IntPreference numBalls) {
    m_drivetrain = drivetrain;
    m_vision = vision;
    m_timeout = isTimeout;
    m_numTimeout = numTimeout;
    m_numBalls = numBalls;
    m_intake = intake;
    addRequirements(drivetrain);
    addRequirements(vision);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.

  @Override

  public void execute() {
    m_drivetrain.arcadeDrive(.75, m_vision.getX() * RobotPreferences.ballP.getValue());
    if (m_timeout) {
      timer++;

      if (timer >= m_numTimeout.getValue()) {
        timedOut = true;
      } else if (counted >= m_numBalls.getValue()) {
        success = true;
      }
    }
    if (m_intake.getCollectionSwitch()) {
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
    timedOut = false;
    success = false;
    timer = 0;
    counted = 0;
    justCounted = false;
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