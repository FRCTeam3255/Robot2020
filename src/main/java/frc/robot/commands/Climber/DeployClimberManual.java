/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class DeployClimberManual extends CommandBase {
  /**
   * Creates a new DeployClimberManual.
   */
  private final Climber climber;
  private SN_DoublePreference speed;

  public DeployClimberManual(Climber a_climber, SN_DoublePreference a_speed) {
    // Use addRequirements() here to declare subsystem dependencies.
    climber = a_climber;
    speed = a_speed;
    addRequirements(a_climber);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    climber.setSpeed(speed.getValue());

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    climber.setSpeed(0);

  }

  // Returns false when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
