/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
  /**
   * Creates a new Climber.
   */

  public Climber()
  {
  }


  // methods

  // speed can be -1 to +1
  public void extend(double speed)
  {
  }

  // extend to specific height (in inches)
  public void extendToHeight(int height)
  {
  }

  public void clamp(boolean clamped)
  {
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
