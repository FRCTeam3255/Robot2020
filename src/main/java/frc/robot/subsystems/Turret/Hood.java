// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Turret;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.RobotMap;
import frc.robot.RobotPreferences;

public class Hood extends SubsystemBase {
  /** Creates a new Hood. */

  private TalonSRX hoodTalon;
  private currentHoodPreset currentPreset;

  public Hood() {

    hoodTalon = new TalonSRX(RobotMap.HOOD_TALON);
    currentPreset = currentHoodPreset.NONE;
    hoodTalon.configFactoryDefault();
    hoodTalon.setNeutralMode(NeutralMode.Brake);
    hoodTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
    configureHood();
  }

  public void configureHood() {
    hoodTalon.config_kP(0, RobotPreferences.hoodP.getValue());
    hoodTalon.config_kI(0, RobotPreferences.hoodI.getValue());
    hoodTalon.config_kD(0, RobotPreferences.hoodD.getValue());
    hoodTalon.configPeakOutputForward(RobotPreferences.hoodMaxSpeed.getValue());
    hoodTalon.configPeakOutputReverse(-RobotPreferences.hoodMaxSpeed.getValue());
  }

  public void moveHoodToDegree(double a_degree) {
    if ((Math.abs(RobotContainer.susan.getSusanPosition()) < RobotPreferences.susanSafteyTol.getValue())
        || (a_degree > RobotPreferences.hoodSaftey.getValue())) {
      configureHood();
      double degree = a_degree;
      if (degree < RobotPreferences.hoodMinDegree.getValue()) {
        degree = RobotPreferences.hoodMinDegree.getValue();
      } else if (degree > RobotPreferences.hoodMaxDegree.getValue()) {
        degree = RobotPreferences.hoodMaxDegree.getValue();
      }
      hoodTalon.set(ControlMode.Position, (degree * RobotPreferences.hoodCountsPerDegree.getValue()));
    } else {
      setHoodSpeed(0);
    }
  }

  public boolean hoodFinished() {
    return Math.abs(getHoodError()) <= RobotPreferences.hoodTol.getValue();
  }

  public void setHoodSpeed(double a_speed) {
    double speed = a_speed;
    if ((getHoodPosition() < RobotPreferences.hoodMinDegree.getValue()) && speed < 0) {
      speed = 0;
    } else if ((getHoodPosition() > RobotPreferences.hoodMaxDegree.getValue()) && speed > 0) {
      speed = 0;
    }
    hoodTalon.set(ControlMode.PercentOutput, speed);
  }

  public void resetHoodEncoder() {
    hoodTalon.setSelectedSensorPosition(0);
  }

  public double getHoodEncoder() {
    return hoodTalon.getSelectedSensorPosition();
  }

  public double getHoodError() {
    return hoodTalon.getClosedLoopError() / RobotPreferences.hoodCountsPerDegree.getValue();
  }

  public double getHoodPosition() {
    return hoodTalon.getSelectedSensorPosition() / RobotPreferences.hoodCountsPerDegree.getValue();
  }

  public enum Direction {
    NORTH, EAST, SOUTH, WEST
  }

  public enum currentHoodPreset {
    MIDDLE_TRENCH, FRONT_TRENCH, INITIALIZATION, CLOSE, WALL_LOW, WALL_HIGH, NONE
  }

  public void setCurrentPresetE(currentHoodPreset a_preset) {
    currentPreset = a_preset;
  }

  public String getCurrentHoodPreset() {
    String presetString;
    switch (currentPreset) {

      case MIDDLE_TRENCH:
        presetString = "Middle Trench";
        break;
      case CLOSE:
        presetString = "Close";
        break;
      case FRONT_TRENCH:
        presetString = "Front Trench";
        break;
      case INITIALIZATION:
        presetString = "Init Line";
        break;
      case NONE:
        presetString = "None";
        break;
      case WALL_HIGH:
        presetString = "Wall High";
        break;
      case WALL_LOW:
        presetString = "Wall Low";
        break;
      default:
        presetString = "Default (something is broken)";
        break;
    }
    return presetString;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Hood Encoder", getHoodEncoder());
    SmartDashboard.putBoolean("Hood Finished", hoodFinished());
    SmartDashboard.putNumber("Hood Err", getHoodError());
    SmartDashboard.putNumber("Hood Position", getHoodPosition());
    SmartDashboard.putString("Hood Preset", getCurrentHoodPreset());
  }
}
