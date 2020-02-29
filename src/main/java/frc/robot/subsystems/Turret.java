/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.RobotMap;
import frc.robot.RobotPreferences;

public class Turret extends SubsystemBase {
  /**
   * Creates a new Turret.
   */

  private CANSparkMax shooterMaster;
  private CANSparkMax shooterSlave;
  private CANEncoder shooterEnocder;
  private CANEncoder shooterBEnocder;
  private TalonSRX finalShooterGateTalon;
  private TalonSRX lazySusanTalon;
  private TalonSRX hoodTalon;
  private CANPIDController shooterPIDController;
  private double goalVelocity;

  public Turret() {
    shooterMaster = new CANSparkMax(RobotMap.SHOOTER_FRONT_SPARK, MotorType.kBrushless);
    shooterSlave = new CANSparkMax(RobotMap.SHOOTER_BACK_SPARK, MotorType.kBrushless);
    shooterPIDController = shooterMaster.getPIDController();
    finalShooterGateTalon = new TalonSRX(RobotMap.FINAL_SHOOTER_GATE_TALON);
    lazySusanTalon = new TalonSRX(RobotMap.LAZY_SUSAN_TALON);
    hoodTalon = new TalonSRX(RobotMap.HOOD_TALON);
    goalVelocity = RobotPreferences.shooterMaxRPM.getValue();
    lazySusanTalon.setNeutralMode(NeutralMode.Brake);
    hoodTalon.setNeutralMode(NeutralMode.Brake);

    shooterMaster.restoreFactoryDefaults();
    shooterSlave.restoreFactoryDefaults();
    shooterSlave.follow(shooterMaster);
    shooterEnocder = shooterMaster.getEncoder();
    shooterBEnocder = shooterSlave.getEncoder();
    lazySusanTalon.configFactoryDefault();
    lazySusanTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);

    hoodTalon.configFactoryDefault();
    hoodTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
    configureShooter();

    configureLazySusan();
    configureHood();
  }

  public void configureShooter() {

    shooterPIDController.setP(RobotPreferences.shooterP.getValue());
    shooterPIDController.setI(RobotPreferences.shooterI.getValue());
    shooterPIDController.setD(RobotPreferences.shooterD.getValue());
    shooterPIDController.setFF(RobotPreferences.shooterFF.getValue());
    shooterPIDController.setOutputRange(-1.0, 1.0);
  }

  public void configureLazySusan() {
    lazySusanTalon.config_kP(0, RobotPreferences.susanP.getValue());
    lazySusanTalon.config_kI(0, RobotPreferences.susanI.getValue());
    lazySusanTalon.config_kD(0, RobotPreferences.susanD.getValue());
    lazySusanTalon.config_kF(0, RobotPreferences.susanF.getValue());
    lazySusanTalon.configPeakOutputForward(RobotPreferences.susanMaxSpeed.getValue());
    lazySusanTalon.configPeakOutputReverse(-RobotPreferences.susanMaxSpeed.getValue());

  }

  public void configureHood() {
    hoodTalon.config_kP(0, RobotPreferences.hoodP.getValue());
    hoodTalon.config_kI(0, RobotPreferences.hoodI.getValue());
    hoodTalon.config_kD(0, RobotPreferences.hoodD.getValue());
    hoodTalon.configPeakOutputForward(RobotPreferences.hoodMaxSpeed.getValue());
    hoodTalon.configPeakOutputReverse(-RobotPreferences.hoodMaxSpeed.getValue());
  }

  public void finalShooterGateSetSpeed(double speed) {
    finalShooterGateTalon.set(ControlMode.PercentOutput, speed);
  }

  public void turnSusanToDegree(double a_degree) {
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
  }

  public void setSusanSpeed(double a_speed) {
    double speed = a_speed;
    if (((getSusanPosition() + RobotPreferences.susanHardstopTol.getValue()) < RobotPreferences.susanMinDegree
        .getValue()) && speed < 0) {
      speed = 0;
    } else if (((getSusanPosition() - RobotPreferences.susanHardstopTol.getValue()) > RobotPreferences.susanMaxDegree
        .getValue()) && speed > 0) {
      speed = 0;
    }
    lazySusanTalon.set(ControlMode.PercentOutput, speed);
  }

  public void moveHoodToDegree(double a_degree) {
    configureHood();
    double degree = a_degree;
    if (degree < RobotPreferences.hoodMinDegree.getValue()) {
      degree = RobotPreferences.hoodMinDegree.getValue();
    } else if (degree > RobotPreferences.hoodMaxDegree.getValue()) {
      degree = RobotPreferences.hoodMaxDegree.getValue();
    }
    hoodTalon.set(ControlMode.Position, (degree * RobotPreferences.hoodCountsPerDegree.getValue()));
  }

  public void setShooterSetpoint(double setpoint) {
    goalVelocity = setpoint;

  }

  public void setShooterVelocity() {
    configureShooter();
    shooterPIDController.setReference(goalVelocity, ControlType.kVelocity);
  }

  public boolean hoodFinished() {
    return Math.abs(getHoodError()) <= RobotPreferences.hoodTol.getValue();
  }

  public boolean susanFinished() {
    return Math.abs(getSusanError()) <= RobotPreferences.susanTol.getValue();

  }

  public void setHoodSpeed(double a_speed) {
    double speed = a_speed;
    // if ((getHoodPosition() - hardstopTol < hoodMinDegree) && speed < 0) {
    // speed = 0;
    // } else if ((getHoodPosition() + hardstopTol > hoodMaxDegree) && speed > 0) {
    // speed = 0;
    // }
    hoodTalon.set(ControlMode.PercentOutput, speed);
  }

  public void setShooterSpeed(double speed) {
    shooterMaster.set(speed);
  }

  public double getShooterSpeed() {
    return shooterEnocder.getVelocity();
  }

  public double getShooter2Speed() {
    return shooterBEnocder.getVelocity();
  }

  public void setGoalVelocity(double a_goal) {
    goalVelocity = a_goal;
  }

  public boolean isShooterSpedUp() {
    return (Math.abs(getShooterSpeed() - goalVelocity) < RobotPreferences.shooterTolerance.getValue());
    // return getShooterSpeed() > goalVelocity;
  }

  public double getShooterError(double goalRPM) {
    return Math.abs(getShooterSpeed() - goalRPM);
  }

  public void resetSusanEncoder() {
    lazySusanTalon.setSelectedSensorPosition(0);

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

  public double getSusanError() {
    return lazySusanTalon.getClosedLoopError() / RobotPreferences.susanCountsPerDegree.getValue();
  }

  public double getSusanEncoder() {
    return lazySusanTalon.getSelectedSensorPosition();
  }

  public double getSusanPosition() {
    return lazySusanTalon.getSelectedSensorPosition() / RobotPreferences.susanCountsPerDegree.getValue();
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
    SmartDashboard.putNumber("Shooter A Velocity", getShooterSpeed());
    SmartDashboard.putNumber("Shooter B Velocity", getShooter2Speed());
    SmartDashboard.putNumber("Susan Position", getSusanPosition());
    SmartDashboard.putNumber("Hood Position", getHoodPosition());
    SmartDashboard.putBoolean("Shooter finished", isShooterSpedUp());
    SmartDashboard.putNumber("Given Pos", RobotContainer.vision.getVisionInnerOffset()
        + RobotContainer.turret.getSusanPosition() + RobotContainer.vision.getVisionXError());

  }
}
