/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;
import frc.robot.RobotPreferences;

public class Climber extends SubsystemBase {
  /**
   * Creates a new Climber.
   */
  private CANSparkMax climberSpark;
  

  public Climber()
  {
    climberSpark = new CANSparkMax(RobotMap.CLIMBER_TALON, MotorType.kBrushless);

  }


  // methods

  // speed can be -1 to +1
  public void setSpeed(double speed)
  {
    climberSpark.set(speed);
  }

  // extend to specific height (in inches)
  public void extendToHeight(double height)
  {
    //currently under debate whether or not we're using sparks for climber, waiting for verdict before writing this function.
    // climberTalon.set(ControlMode.Position, RobotPreferences.climberCountsPerInches.getValue()*height);
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
