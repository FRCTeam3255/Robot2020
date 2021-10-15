/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Turret.Hood;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Turret.Hood;
import frc.robot.subsystems.Turret.Hood.currentHoodPreset;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class SetHoodPosition extends CommandBase {
  /**
   * Creates a new SetHoodPosition.
   */
  private SN_DoublePreference degrees;
  private SN_DoublePreference velocity;
  private boolean zero;
  private Timer timer = new Timer();
  private currentHoodPreset preset;

  public SetHoodPosition(SN_DoublePreference a_degrees, SN_DoublePreference a_velocity, boolean a_zero,
      currentHoodPreset a_preset) {
    // Use addRequirements() here to declare subsystem dependencies.
    degrees = a_degrees;
    velocity = a_velocity;
    zero = a_zero;
    preset = a_preset;

    addRequirements(RobotContainer.hood);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.hood.setCurrentPresetE(preset);
    if (zero) {
      RobotContainer.susan.turnSusanToDegree(0);
      // RobotContainer.controlPanel.retractControlPanel();
    }

    timer.reset();
    timer.start();
    RobotContainer.hood.moveHoodToDegree(degrees.getValue());
    RobotContainer.shooter.setShooterSetpoint(velocity.getValue());

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    RobotContainer.hood.moveHoodToDegree(degrees.getValue());
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
