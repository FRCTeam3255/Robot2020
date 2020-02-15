/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.ControlPanel;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ControlPanel;

public class ToggleControlPanel extends CommandBase {
  /**
   * Creates a new ToggleControlPanel.
   */
  private final ControlPanel controlPanel;

  public ToggleControlPanel(ControlPanel a_controlPanel) {
    // Use addRequirements() here to declare subsystem dependencies.
    controlPanel = a_controlPanel;
    addRequirements(a_controlPanel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (controlPanel.getControlPanelDeployed()) {
      controlPanel.retractControlPanel();
    } else {
      controlPanel.deployControlPanel();
    }
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
    return true;
  }
}
