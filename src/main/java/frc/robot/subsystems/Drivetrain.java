/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;
import frc.robot.RobotPreferences;

public class Drivetrain extends SubsystemBase {
  // Creates a new ExampleSubsystem
  private TalonFX leftMaster;
  private TalonFX leftSlave;
  private TalonFX rightMaster;
  private TalonFX rightSlave;

  private TalonFXConfiguration config;

  public Drivetrain() {
    leftMaster = new TalonFX(RobotMap.DRIVETRAIN_LEFT_MASTER);
    leftSlave = new TalonFX(RobotMap.DRIVETRAIN_LEFT_SLAVE);
    rightMaster = new TalonFX(RobotMap.DRIVETRAIN_RIGHT_MASTER);
    rightSlave = new TalonFX(RobotMap.DRIVETRAIN_RIGHT_SLAVE);
    config = new TalonFXConfiguration();

    config.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
    /* rest of the configs */
    config.neutralDeadband = RobotPreferences.motProfNeutralDeadband
        .getValue(); /* 0.1 % super small for best low-speed control */

    // TODO: These preferences only get called at robot power up. Should have a
    // reload method and/or reload at PID start

    config.slot0.kF = RobotPreferences.motProfF.getValue();
    config.slot0.kP = RobotPreferences.motProfP.getValue();
    config.slot0.kI = RobotPreferences.motProfI.getValue();
    config.slot0.kD = RobotPreferences.motProfD.getValue();
    config.peakOutputForward = 1;
    config.peakOutputReverse = -1;
    config.slot0.integralZone = (int) RobotPreferences.motProfIz.getValue();
    config.slot0.closedLoopPeakOutput = RobotPreferences.motProfPeakOut.getValue();
    rightMaster.configAllSettings(config);
    leftMaster.configAllSettings(config);
    leftSlave.configFactoryDefault();
    rightSlave.configFactoryDefault();
    leftSlave.follow(leftMaster);
    rightSlave.follow(rightMaster);

    /* pick the sensor phase and desired direction */
    rightMaster.setSensorPhase(false);
    leftMaster.setSensorPhase(false);
    rightMaster.setInverted(false); /* right side has to apply +V to M-, to go forward */
    rightSlave.setInverted(false);
    leftMaster.setInverted(true);
    leftSlave.setInverted(true);

  }

  public void arcadeDrive(double speed, double turn) {
    // TODO: Don't use constants for a deadband. Use prefs with a reload.
    if ((speed > -.2 && speed < .2)) {
      speed = 0;
    }
    if ((turn > -.2 && turn < .2)) {
      turn = 0;
    }
    // TODO: Don't use constants for motor tuning. Use prefs with a reload. And our
    // motors max at 20% power?!?
    leftMaster.set(ControlMode.PercentOutput, .2 * speed, DemandType.ArbitraryFeedForward, -.2 * turn);
    rightMaster.set(ControlMode.PercentOutput, .2 * speed, DemandType.ArbitraryFeedForward, .2 * turn);
  }

  public void driveDistance(double distance) {
    // TODO: Do you really want distance in feet? Decimal feet can be hard which is
    // why we usually use inches
    leftMaster.set(ControlMode.Position, distance * RobotPreferences.motProfSensorUnitsPerFt.getValue());
    leftSlave.follow(leftMaster);
    rightMaster.set(ControlMode.Position, distance * RobotPreferences.motProfSensorUnitsPerFt.getValue());
    rightSlave.follow(rightMaster);

  }

  public int getPositionError() {
    return leftMaster.getClosedLoopError();
  }

  /*
   * TODO: It appears resetPositionPID is how you stop driveDistance, and
   * resetMotionProfile is how you stop startMotionProfile. Could improve names to
   * make that more clear. Since it would probably be ok to clearMotionProfiles
   * inside of resetPositionPID, why not just rename resetMotionProfile to
   * startArcadeDrive and delete resetPositionPID.
   */
  public void resetPositionPID() {
    rightMaster.set(ControlMode.PercentOutput, 0.0);
    leftMaster.set(ControlMode.PercentOutput, 0.0);
    resetEncoderCounts();
  }

  public void resetMotionProfile() {
    rightMaster.set(ControlMode.PercentOutput, 0.0);
    leftMaster.set(ControlMode.PercentOutput, 0.0);
    rightMaster.clearMotionProfileTrajectories();
    leftMaster.clearMotionProfileTrajectories();
    resetEncoderCounts();
  }

  public double getLeftEncoderCount() {
    return leftMaster.getSelectedSensorPosition();
  }

  public double getRightEncoderCount() {
    return rightMaster.getSelectedSensorPosition();
  }

  public void resetEncoderCounts() {
    // TODO: Did this come from a recipe? See method comment. Why not use
    // leftMaster.setSelectedSensorPosition(0)
    leftMaster.getSensorCollection().setIntegratedSensorPosition(0, 100);
    rightMaster.getSensorCollection().setIntegratedSensorPosition(0, 100);
  }

  public void startMotionProfile(BufferedTrajectoryPointStream pointsLeft, BufferedTrajectoryPointStream pointsRight) {
    leftMaster.startMotionProfile(pointsLeft, 10, ControlMode.MotionProfile);
    leftSlave.follow(leftMaster);
    rightMaster.startMotionProfile(pointsRight, 10, ControlMode.MotionProfile);
    rightSlave.follow(rightMaster);
  }

  public boolean isMotionProfileFinished() {
    return (rightMaster.isMotionProfileFinished() && leftMaster.isMotionProfileFinished());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    SmartDashboard.putNumber("Drivetrain Left Encoder", getLeftEncoderCount());
    SmartDashboard.putNumber("Drivetrain Right Encoder", getRightEncoderCount());
    SmartDashboard.putNumber("Drivetrain Error", getPositionError());
    SmartDashboard.putBoolean("Motion Finished", isMotionProfileFinished());
  }
}