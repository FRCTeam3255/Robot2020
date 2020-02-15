/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
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
  private TalonFXConfiguration _config;

  public Climber() {
    winchTalon = new TalonFX(RobotMap.WINCH_TALON);
    climberTalon = new TalonSRX(RobotMap.CLIMBER_TALON);
    _config = new TalonFXConfiguration();

    _config.primaryPID.selectedFeedbackSensor = FeedbackDevice.QuadEncoder;
    /* rest of the configs */
    _config.neutralDeadband = RobotPreferences.motProfNeutralDeadband
        .getValue(); /* 0.1 % super small for best low-speed control */
    _config.slot0.kF = RobotPreferences.climberF.getValue();
    _config.slot0.kP = RobotPreferences.climberP.getValue();
    _config.slot0.kI = RobotPreferences.climberI.getValue();
    _config.slot0.kD = RobotPreferences.climberD.getValue();
    _config.slot0.integralZone = (int) RobotPreferences.climberIz.getValue();

  }

  // methods

  // speed can be -1 to +1
  public void setSpeed(double speed) {
    climberTalon.set(ControlMode.PercentOutput, speed);
  }

  public void setWinchSpeed(double speed) {
    winchTalon.set(ControlMode.PercentOutput, speed);
  }

  // extend to specific height (in inches)
  public void extendToHeight(double height) {
    climberTalon.set(ControlMode.Position, RobotPreferences.climberCountsPerInches.getValue() * height);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
