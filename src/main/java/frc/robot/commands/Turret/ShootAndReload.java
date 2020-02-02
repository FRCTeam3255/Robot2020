/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.Turret;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.RobotPreferences;
import frc.robot.commands.Intake.Shoot;
import frc.robot.commands.Intake.LoadStage1;
import frc.robot.commands.Intake.LoadStage2;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Turret;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class ShootAndReload extends SequentialCommandGroup {
  /**
   * Creates a new ShootAndReload.
   */

  public ShootAndReload() {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super();
//    addCommands(new SetShooterSpeed(RobotContainer.m_turret, RobotPreferences.shooterFullSpeed));
    addCommands(new Shoot(RobotContainer.m_intake));
    addCommands(new LoadStage2(RobotContainer.m_intake));
    addCommands(new LoadStage1(RobotContainer.m_intake));
//    addCommands(new SetShooterSpeed(RobotContainer.m_turret, RobotPreferences.shooterNoSpeed));
  }
}
