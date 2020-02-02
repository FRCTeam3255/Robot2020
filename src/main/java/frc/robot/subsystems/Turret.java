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
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;
import frc.robot.RobotPreferences;

public class Turret extends SubsystemBase {
  /**
   * Creates a new Turret.
   */

  CANSparkMax shooterMaster;
  CANSparkMax shooterSlave;
  CANEncoder shooterEnocder;
  TalonFX lazySusanTalon;
  TalonFX hoodTalon;

  private CANPIDController shooterPIDController;
  

  public Turret() {
    shooterMaster = new CANSparkMax(RobotMap.SHOOTER_FRONT_SPARK, MotorType.kBrushless);
    shooterSlave = new CANSparkMax(RobotMap.SHOOTER_BACK_SPARK, MotorType.kBrushless);
    shooterPIDController = shooterMaster.getPIDController();
    

    shooterMaster.restoreFactoryDefaults();
    shooterSlave.restoreFactoryDefaults();
    shooterSlave.follow(shooterMaster);
    shooterEnocder = shooterMaster.getEncoder();
    shooterPIDController.setP(RobotPreferences.shooterP.getValue());
    shooterPIDController.setI(RobotPreferences.shooterI.getValue());
    shooterPIDController.setD(RobotPreferences.shooterD.getValue());
    shooterPIDController.setFF(RobotPreferences.shooterFF.getValue());
    shooterPIDController.setOutputRange(-1.0, 1.0);

    lazySusanTalon = new TalonFX(RobotMap.LAZY_SUSAN);
    lazySusanTalon.configFactoryDefault();
    lazySusanTalon.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 30);
    lazySusanTalon.config_kP(0, RobotPreferences.susanP.getValue());
    lazySusanTalon.config_kI(0, RobotPreferences.susanI.getValue());
    lazySusanTalon.config_kD(0, RobotPreferences.susanD.getValue());

    hoodTalon = new TalonFX(RobotMap.LAZY_SUSAN);
    hoodTalon.configFactoryDefault();
    hoodTalon.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 30);
    hoodTalon.config_kP(0, RobotPreferences.hoodP.getValue());
    hoodTalon.config_kI(0, RobotPreferences.hoodI.getValue());
    hoodTalon.config_kD(0, RobotPreferences.hoodD.getValue());
  }

  public void susanTurnToDegree(double degree){
    lazySusanTalon.set(ControlMode.Position, (degree*RobotPreferences.susanCountsPerDegree.getValue()));
  }
  public void setSusanSpeed(double speed){
    lazySusanTalon.set(ControlMode.Position, speed);
  }

  public void hoodMoveToDegree(double degree){
    lazySusanTalon.set(ControlMode.Position, (degree*RobotPreferences.hoodCountsPerDegree.getValue()));
  }
  public void setHoodSpeed(double speed){
    lazySusanTalon.set(ControlMode.Position, speed);
  }

  public void setShooterSpeed(double speed){
    shooterPIDController.setReference(speed*RobotPreferences.shooterMaxRPM.getValue(), ControlType.kVelocity);
  }
  
  public double getShooterSpeed(){
    return shooterEnocder.getVelocity();
  }
  public boolean isShooterSpedUp(double speed){
    return (Math.abs(getShooterSpeed()-speed)<RobotPreferences.shooterTolerance.getValue());
  }

  public void resetSusanEncoder(){
    lazySusanTalon.getSensorCollection().setIntegratedSensorPosition(0, 100);

  }
  public void resetHoodEncoder(){
    hoodTalon.getSensorCollection().setIntegratedSensorPosition(0, 100);
  }
  public double getHoodEncoder(){
    return hoodTalon.getSelectedSensorPosition();
  }

  public double getSusanEncoder(){
    return lazySusanTalon.getSelectedSensorPosition();
  }


  public double getSusanPosition(){
    return lazySusanTalon.getSelectedSensorPosition()/RobotPreferences.susanCountsPerDegree.getValue();
  }


  public double getHoodPosition(){
    return hoodTalon.getSelectedSensorPosition()/RobotPreferences.hoodCountsPerDegree.getValue();
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
