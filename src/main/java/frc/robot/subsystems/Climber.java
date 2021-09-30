// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;
import frc.robot.RobotPreferences;

public class Climber extends SubsystemBase {

  private TalonFX climbTalon;
  private DoubleSolenoid brakeSolenoid;
  private DigitalInput loweredSwitch;

  private static final Value brakeDeployedValue = Value.kForward;
  private static final Value brakeRetractedValue = Value.kReverse;

  /** Creates a new Climber. */
  public Climber() {
    climbTalon = new TalonFX(RobotMap.CLIMBER_TALON);
    brakeSolenoid = new DoubleSolenoid(RobotMap.CLIMBER_SOLENOID_A, RobotMap.CLIMBER_SOLENOID_B);
    loweredSwitch = new DigitalInput(RobotMap.CLIMBER_SWITCH);

    climbTalon.configFactoryDefault();
    climbTalon.setNeutralMode(NeutralMode.Brake);
    climbTalon.setInverted(false);
  }

  // positive on climbTalon pulls climber down (subject to change)
  // setInverted(true) instead of changing this setClimbTalon() if it is flipped
  public void setClimbTalon(double a_speed) {
    double speed = a_speed;

    // if climber is lowered and input tells it to go lower (positive input)
    if (isClimberLowered() && a_speed > 0) {
      speed = 0;
    }
    // if climber is beyond max height and input tells it to go heigher (negative
    // input)
    if (getClimbTalon() > RobotPreferences.climberMaxHeight.getValue() && a_speed < 0) {
      speed = 0;
    }

    climbTalon.set(ControlMode.PercentOutput, speed);
  }

  public void deployBrake() {
    brakeSolenoid.set(brakeDeployedValue);
  }

  public void retractBrake() {
    brakeSolenoid.set(brakeRetractedValue);
  }

  public void toggleBrake() {
    if (isBrake()) {
      retractBrake();
    } else {
      deployBrake();
    }
  }

  public double getClimbTalon() {
    return climbTalon.getSelectedSensorPosition();
  }

  public boolean isBrake() {
    return (brakeSolenoid.get() == brakeDeployedValue);
  }

  public boolean isClimberLowered() {
    return loweredSwitch.get();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Climb Talon Encoder", getClimbTalon());
    SmartDashboard.putBoolean("Climber Brake", isBrake());
    SmartDashboard.putBoolean("Climber Lowered", isClimberLowered());
  }
}
