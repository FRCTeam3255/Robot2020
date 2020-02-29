/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;
import frc.robot.RobotPreferences;

public class Climber extends SubsystemBase {
  /**
   * Creates a new Climber. climbs with pid, climbs manually
   */
  private TalonSRX climberTalon;
  private TalonFX winchTalon;
  private DigitalInput climberSwitch;

  public Climber() {
    winchTalon = new TalonFX(RobotMap.WINCH_TALON);
    climberTalon = new TalonSRX(RobotMap.CLIMBER_TALON);
    climberTalon.setNeutralMode(NeutralMode.Brake);
    climberTalon.configFactoryDefault();
    winchTalon.configFactoryDefault();
    climberSwitch = new DigitalInput(RobotMap.CLIMBER_SWITCH);

  }

  public boolean getClimberSwitch() {
    return !climberSwitch.get();
  }

  public void setSpeed(double speed) {
    if (getClimberSwitch() && speed > 0) {
      climberTalon.set(ControlMode.PercentOutput, 0);
    }

    climberTalon.set(ControlMode.PercentOutput, speed);
  }

  public void setWinchSpeed(double speed) {
    winchTalon.set(ControlMode.PercentOutput, speed);
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Climber Stop Switch", getClimberSwitch());
  }
}
