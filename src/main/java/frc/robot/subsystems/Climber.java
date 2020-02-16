/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;
import frc.robot.RobotPreferences;

public class Climber extends SubsystemBase {
  /**
   * Creates a new Climber. climbs with pid, climbs manually
   */
  private TalonSRX climberTalon;
  private TalonFX winchTalon;

  public Climber() {
    winchTalon = new TalonFX(RobotMap.WINCH_TALON);
    climberTalon = new TalonSRX(RobotMap.CLIMBER_TALON);
    climberTalon.configFactoryDefault();
    winchTalon.configFactoryDefault();

  }

  public void setSpeed(double speed) {
    climberTalon.set(ControlMode.PercentOutput, speed);
  }

  public void setWinchSpeed(double speed) {
    winchTalon.set(ControlMode.PercentOutput, speed);
  }

  public void extendToHeight(double height) {
    climberTalon.set(ControlMode.Position, RobotPreferences.climberCountsPerInches.getValue() * height);
  }

  @Override
  public void periodic() {
  }
}
