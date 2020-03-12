/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.Turret;

import com.ctre.phoenix.motorcontrol.ControlMode;
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

public class Shooter extends SubsystemBase {

  private CANSparkMax shooterMaster;
  private CANSparkMax shooterSlave;
  private CANEncoder shooterEnocder;
  private CANEncoder shooterBEnocder;
  private double goalVelocity;
  private CANPIDController shooterPIDController;
  private TalonSRX finalShooterGateTalon;

  /**
   * Creates a new Shooter.
   */
  public Shooter() {

    shooterMaster = new CANSparkMax(RobotMap.SHOOTER_FRONT_SPARK, MotorType.kBrushless);
    shooterSlave = new CANSparkMax(RobotMap.SHOOTER_BACK_SPARK, MotorType.kBrushless);
    shooterPIDController = shooterMaster.getPIDController();
    finalShooterGateTalon = new TalonSRX(RobotMap.FINAL_SHOOTER_GATE_TALON);
    goalVelocity = RobotPreferences.shooterMaxRPM.getValue();

    shooterMaster.restoreFactoryDefaults();
    shooterSlave.restoreFactoryDefaults();
    shooterSlave.follow(shooterMaster);
    shooterEnocder = shooterMaster.getEncoder();
    shooterBEnocder = shooterSlave.getEncoder();
    
    configureShooter();

  }

  public void configureShooter() {

    shooterPIDController.setP(RobotPreferences.shooterP.getValue());
    shooterPIDController.setI(RobotPreferences.shooterI.getValue());
    shooterPIDController.setD(RobotPreferences.shooterD.getValue());
    shooterPIDController.setFF(RobotPreferences.shooterFF.getValue());
    shooterPIDController.setOutputRange(-1.0, 1.0);

  }

  public void finalShooterGateSetSpeed(double speed) {
    finalShooterGateTalon.set(ControlMode.PercentOutput, speed);
  }

  public void setShooterSetpoint(double setpoint) {
    goalVelocity = setpoint;
  }

  public void setShooterVelocity() {
    configureShooter();
    if (goalVelocity < 3000) {
      setShooterSpeed(RobotPreferences.shooterLowSpeedCoefficient.getValue()
          * (goalVelocity / RobotPreferences.shooterMaxRPM.getValue()));
    } else {
      shooterPIDController.setReference(goalVelocity, ControlType.kVelocity);
    }

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
    // return (Math.abs(getShooterSpeed() - goalVelocity) <
    // RobotPreferences.shooterTolerance.getValue());
    return (Math.abs(getShooterSpeed()) > goalVelocity);
    // return getShooterSpeed() > goalVelocity;
  }

  public double getShooterError(double goalRPM) {
    return Math.abs(getShooterSpeed() - goalRPM);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    SmartDashboard.putNumber("Shooter A Velocity", getShooterSpeed());
    SmartDashboard.putNumber("Shooter B Velocity", getShooter2Speed());
    SmartDashboard.putBoolean("Shooter finished", isShooterSpedUp());
  }
}
