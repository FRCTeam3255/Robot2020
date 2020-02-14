/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotPreferences;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Vision;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class AlignTurretVision extends CommandBase {
  /**
   * Creates a new AlignTurretVision.
   */
  private final Turret m_turret;
  private final Vision m_vision;
  private SN_DoublePreference m_speed;

  public AlignTurretVision(Turret turretSubsystem, Vision visionSubsystem, SN_DoublePreference speed) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_turret = turretSubsystem;
    m_vision = visionSubsystem;
    m_speed = speed;
    addRequirements(turretSubsystem);
    addRequirements(visionSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_vision.visionHasTarget()) {

      m_turret
          .setSusanSpeed(m_speed.getValue() * m_vision.getVisionXError() * RobotPreferences.susanVisionP.getValue());
      m_turret.hoodMoveToDegree(m_vision.getHoodVisionPosition());
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_turret.setSusanSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
