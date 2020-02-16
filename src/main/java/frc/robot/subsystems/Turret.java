/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;
import frc.robot.RobotPreferences;

public class Turret extends SubsystemBase {
  /**
   * Creates a new Turret.
   */

  private CANSparkMax shooterMaster;
  private CANSparkMax shooterSlave;
  private CANEncoder shooterEnocder;
  private TalonSRX finalShooterGateTalon;
  private TalonSRX lazySusanTalon;
  private TalonSRX hoodTalon;
  private double hardstopTol = 3;
  private double hoodMaxDegree = 90;
  private double hoodMinDegree = 0;
  private double susanMinDegree = -90;
  private double susanMaxDegree = 90;

  private CANPIDController shooterPIDController;

  public Turret() {
    shooterMaster = new CANSparkMax(RobotMap.SHOOTER_FRONT_SPARK, MotorType.kBrushless);
    shooterSlave = new CANSparkMax(RobotMap.SHOOTER_BACK_SPARK, MotorType.kBrushless);
    shooterPIDController = shooterMaster.getPIDController();
    finalShooterGateTalon = new TalonSRX(RobotMap.FINAL_SHOOTER_GATE_TALON);
    lazySusanTalon = new TalonSRX(RobotMap.LAZY_SUSAN_TALON);
    hoodTalon = new TalonSRX(RobotMap.HOOD_TALON);

    configure();
  }

  public void configure() {

    shooterMaster.restoreFactoryDefaults();
    shooterSlave.restoreFactoryDefaults();
    shooterSlave.follow(shooterMaster);
    shooterEnocder = shooterMaster.getEncoder();

    shooterPIDController.setP(RobotPreferences.shooterP.getValue());
    shooterPIDController.setI(RobotPreferences.shooterI.getValue());
    shooterPIDController.setD(RobotPreferences.shooterD.getValue());
    shooterPIDController.setFF(RobotPreferences.shooterFF.getValue());
    shooterPIDController.setOutputRange(-1.0, 1.0);

    lazySusanTalon.configFactoryDefault();
    hoodTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
    hoodTalon.config_kP(0, RobotPreferences.susanP.getValue());
    hoodTalon.config_kI(0, RobotPreferences.susanI.getValue());
    hoodTalon.config_kD(0, RobotPreferences.susanD.getValue());

    hoodTalon.configFactoryDefault();
    hoodTalon.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
    hoodTalon.config_kP(0, RobotPreferences.hoodP.getValue());
    hoodTalon.config_kI(0, RobotPreferences.hoodI.getValue());
    hoodTalon.config_kD(0, RobotPreferences.hoodD.getValue());
  }

  public void finalShooterGateSetSpeed(double speed) {
    finalShooterGateTalon.set(ControlMode.PercentOutput, speed);
  }

  public void susanTurnToDegree(double degree) {
    lazySusanTalon.set(ControlMode.Position, (degree * RobotPreferences.susanCountsPerDegree.getValue()));
  }

  public void setSusanSpeed(double a_speed) {
    double speed = a_speed;
    if ((getSusanPosition() - hardstopTol < susanMinDegree) && speed < 0) {
      speed = 0;
    } else if ((getSusanPosition() + hardstopTol > susanMaxDegree) && speed > 0) {
      speed = 0;
    }
    lazySusanTalon.set(ControlMode.PercentOutput, speed);
  }

  public void hoodMoveToDegree(double a_degree) {
    double degree = a_degree;
    if (degree < hoodMinDegree) {
      degree = hoodMinDegree;
    } else if (degree > hoodMaxDegree) {
      degree = hoodMaxDegree;
    }
    hoodTalon.set(ControlMode.Position, (degree * RobotPreferences.hoodCountsPerDegree.getValue()));
  }

  public boolean hoodFinished() {
    return Math.abs(hoodTalon.getClosedLoopError()) <= RobotPreferences.hoodTol.getValue();
  }

  public boolean susanFinished() {
    return Math.abs(lazySusanTalon.getClosedLoopError()) <= RobotPreferences.susanTol.getValue();

  }

  public void setHoodSpeed(double a_speed) {
    double speed = a_speed;
    if ((getHoodPosition() - hardstopTol < hoodMinDegree) && speed < 0) {
      speed = 0;
    } else if ((getHoodPosition() + hardstopTol > hoodMaxDegree) && speed > 0) {
      speed = 0;
    }
    hoodTalon.set(ControlMode.Position, speed);
  }

  public void setShooterSpeed(double speed) {
    shooterMaster.set(speed);
  }

  public void setShooterVelocity(double velocity) {
    shooterPIDController.setReference(velocity, ControlType.kVelocity);
  }

  public double getShooterSpeed() {
    return shooterEnocder.getVelocity();
  }

  public boolean isShooterSpedUp(double goalRPM) {
    return (Math.abs(getShooterSpeed() - goalRPM) < RobotPreferences.shooterTolerance.getValue());
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
    SmartDashboard.putNumber("Hood Err", hoodTalon.getClosedLoopError());
    SmartDashboard.putNumber("Shooter Velocity", getShooterSpeed());
    SmartDashboard.putNumber("Susan Position", getSusanPosition());
    SmartDashboard.putNumber("Hood Position", getHoodPosition());
    SmartDashboard.putBoolean("Shooter finished", isShooterSpedUp(RobotPreferences.shooterMaxRPM.getValue()));

  }
}
