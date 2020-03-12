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

public class Susan extends SubsystemBase {
  
  private TalonSRX lazySusanTalon;
  
  /**
   * Creates a new Susan.
   */
  public Susan() {

    lazySusanTalon = new TalonSRX(RobotMap.LAZY_SUSAN_TALON);
    lazySusanTalon.setNeutralMode(NeutralMode.Brake);

    lazySusanTalon.configFactoryDefault();
    lazySusanTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);

    configureLazySusan();

  }

  public void configureLazySusan() {
    lazySusanTalon.config_kP(0, RobotPreferences.susanP.getValue());
    lazySusanTalon.config_kI(0, RobotPreferences.susanI.getValue());
    lazySusanTalon.config_kD(0, RobotPreferences.susanD.getValue());
    lazySusanTalon.config_kF(0, RobotPreferences.susanF.getValue());
    lazySusanTalon.configPeakOutputForward(RobotPreferences.susanMaxSpeed.getValue());
    lazySusanTalon.configPeakOutputReverse(-RobotPreferences.susanMaxSpeed.getValue());

  }

  public void turnSusanToDegree(double a_degree) {
    if (RobotContainer.hood.getHoodPosition() > RobotPreferences.hoodSaftey.getValue()) {
      configureLazySusan();
      double degree;
      if (a_degree < RobotPreferences.susanMinDegree.getValue()) {
        degree = RobotPreferences.susanMinDegree.getValue();
      } else if (a_degree > RobotPreferences.susanMaxDegree.getValue()) {
        degree = RobotPreferences.susanMaxDegree.getValue();
      } else {
        degree = a_degree;
      }
      lazySusanTalon.set(ControlMode.Position, (degree * RobotPreferences.susanCountsPerDegree.getValue()));
    } else {
      lazySusanTalon.set(ControlMode.PercentOutput, 0);
    }
  }

  public void setSusanSpeed(double a_speed) {
    if (RobotContainer.hood.getHoodPosition() > RobotPreferences.hoodSaftey.getValue()) {
      double speed = a_speed;
      if (((getSusanPosition() + RobotPreferences.susanHardstopTol.getValue()) < RobotPreferences.susanMinDegree
          .getValue()) && speed < 0) {
        speed = 0;
      } else if (((getSusanPosition() - RobotPreferences.susanHardstopTol.getValue()) > RobotPreferences.susanMaxDegree
          .getValue()) && speed > 0) {
        speed = 0;
      }
      lazySusanTalon.set(ControlMode.PercentOutput, speed);
    } else {
      lazySusanTalon.set(ControlMode.PercentOutput, 0);
    }
  }

  public void resetSusanEncoder() {
    lazySusanTalon.setSelectedSensorPosition(0);

  }

  public boolean susanFinished() {
    return Math.abs(getSusanError()) <= RobotPreferences.susanTol.getValue();

  }  

  public double getSusanError() {
    return lazySusanTalon.getClosedLoopError() / RobotPreferences.susanCountsPerDegree.getValue();
  }

  public double getSusanEncoder() {
    return lazySusanTalon.getSelectedSensorPosition();
  }

  public double getSusanPosition() {
    return lazySusanTalon.getSelectedSensorPosition() / RobotPreferences.susanCountsPerDegree.getValue();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("Susan Finished", susanFinished());
    SmartDashboard.putNumber("Susan Position", getSusanPosition());
    SmartDashboard.putNumber("Given Pos", RobotContainer.vision.getVisionInnerOffset()
        + getSusanPosition() + RobotContainer.vision.getVisionXError());
  }
}
