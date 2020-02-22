/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
// import edu.wpi.first.wpilibj.DoubleSolenoid;
// import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;

public class Intake extends SubsystemBase {
  /**
   * Creates a new Intake.
   */

  private TalonFX collectorTalon;
  private TalonSRX turretGateTalon;
  private TalonSRX initialShooterGateTalon;
  private DigitalInput collectionSwitch;
  private DigitalInput bottomSwitch;
  private DigitalInput stagedSwitch;
  private double collectorSpeed;
  private Servo stepServo;
  // private DoubleSolenoid collectorSolenoid;

  // private static final Value intakeDeployedValue = Value.kReverse;
  // private static final Value intakeRetractedValue = Value.kForward;

  public Intake() {
    collectorTalon = new TalonFX(RobotMap.COLLECTOR_TALON);
    turretGateTalon = new TalonSRX(RobotMap.TURRET_GATE_TALON);
    initialShooterGateTalon = new TalonSRX(RobotMap.INITIAL_SHOOTER_GATE_TALON);
    stepServo = new Servo(RobotMap.STEP_SERVO);
    collectorTalon.configFactoryDefault();
    turretGateTalon.configFactoryDefault();
    initialShooterGateTalon.configFactoryDefault();

    collectionSwitch = new DigitalInput(RobotMap.COLLECTOR_SWITCH);
    bottomSwitch = new DigitalInput(RobotMap.BOTTOM_SWITCH);
    stagedSwitch = new DigitalInput(RobotMap.STAGED_SWITCH);

    // collectorSolenoid = new DoubleSolenoid(RobotMap.COLLECTOR_SOLENOID_A,
    // RobotMap.COLLECTOR_SOLENOID_B);

  }

  public boolean getCollectionSwitch() {
    /*
     * TODO: wiring polarity of each switch (normally opened/normally closed) will
     * determine whether or not to negate the .get() value. Need to determine for
     * each switch whether we want it to fail open or fail closed if a wire gets
     * disconnected. Probably safest to have a switch wiring failure mean the switch
     * is open (so wire normally opened?).
     */
    return !collectionSwitch.get();
  }

  public boolean getBottomSwitch() {
    return !bottomSwitch.get();
  }

  public void setServoPos(double pos) {
    stepServo.setPosition(pos);
  }

  public boolean getStagedSwitch() {
    return !stagedSwitch.get();
  }

  public void collectorSetSpeed(double speed) {
    collectorSpeed = speed;
    collectorTalon.set(ControlMode.PercentOutput, speed);
  }

  public boolean isCollectorRunning() {
    return Math.abs(collectorSpeed) > 0.01;
  }

  public void turretGateSetSpeed(double speed) {
    turretGateTalon.set(ControlMode.PercentOutput, speed);
  }

  public void initialShooterGateSetSpeed(double speed) {
    initialShooterGateTalon.set(ControlMode.PercentOutput, speed);
  }

  // public void deployCollector() {
  // collectorSolenoid.set(intakeDeployedValue);
  // }

  // public void retractCollector() {
  // collectorSolenoid.set(intakeRetractedValue);

  // }

  // public boolean getCollectorDeployed() {
  // return (collectorSolenoid.get() == intakeDeployedValue);
  // }

  @Override
  public void periodic() {
    // TODO: consider adding all other paramaters (speeds and deployment)
    SmartDashboard.putBoolean("Collection Switch", getCollectionSwitch());
    SmartDashboard.putBoolean("Bottom Switch", getBottomSwitch());
    SmartDashboard.putBoolean("Staged Switch", getStagedSwitch());
    // This method will be called once per scheduler run
  }
}
