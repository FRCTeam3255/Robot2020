/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

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
  /**
   * Creates a new Turret.
   */

  private TalonSRX hoodTalon;

  public Hood() {
    hoodTalon = new TalonSRX(RobotMap.HOOD_TALON);
    hoodTalon.setNeutralMode(NeutralMode.Brake);

    hoodTalon.configFactoryDefault();
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

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Hood Encoder", getHoodEncoder());
    SmartDashboard.putBoolean("Hood Finished", hoodFinished());
    SmartDashboard.putNumber("Hood Err", getHoodError());
    SmartDashboard.putNumber("Hood Position", getHoodPosition());

  }
}
