/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.ControlPanel;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ControlPanel;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class RotateControlPanel extends CommandBase {
  /**
   * Creates a new ControlPanelRotate.
   */
  private SN_DoublePreference speed;
  private final ControlPanel controlPanel;

  public RotateControlPanel(ControlPanel a_controlPanel, SN_DoublePreference a_speed) {
    // Use addRequirements() here to declare subsystem dependencies.
    speed = a_speed;
    controlPanel = a_controlPanel;
    addRequirements(a_controlPanel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    controlPanel.setSpeed(speed.getValue());
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
