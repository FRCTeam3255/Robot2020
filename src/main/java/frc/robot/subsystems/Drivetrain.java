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
import frc.robot.RobotContainer;
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
    configure();
    configureRamps();
  }

  public void configure() {

    rightMaster.configFactoryDefault();
    leftMaster.configFactoryDefault();
    leftSlave.configFactoryDefault();
    rightSlave.configFactoryDefault();
    config.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
    /* rest of the configs */
    config.neutralDeadband = RobotPreferences.motProfNeutralDeadband
        .getValue(); /* 0.1 % super small for best low-speed control */
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

  public void configureRamps() {

    leftMaster.configOpenloopRamp(RobotPreferences.drivetrainRampTime.getValue());
    leftSlave.configOpenloopRamp(RobotPreferences.drivetrainRampTime.getValue());
    rightMaster.configOpenloopRamp(RobotPreferences.drivetrainRampTime.getValue());
    rightSlave.configOpenloopRamp(RobotPreferences.drivetrainRampTime.getValue());
  }

  public void arcadeDriveInit() {
    configureRamps();
  }

  public void arcadeDrive(double a_speed, double a_turn) {
    double speed = a_speed;
    double turn = a_turn;
    if ((speed > -RobotPreferences.drivetrainDeadband.getValue()
        && speed < RobotPreferences.drivetrainDeadband.getValue())) {
      speed = 0;
    }
    if ((turn > -RobotPreferences.drivetrainTurnDeadband.getValue()
        && turn < RobotPreferences.drivetrainTurnDeadband.getValue())) {
      turn = 0;
    }
    if (RobotContainer.drive.getRawButton(6)) {
      speed = speed * RobotPreferences.drivetrainHighSpeed.getValue();
      turn = turn * RobotPreferences.drivetrainHighTurnSpeed.getValue();
    } else {
      speed = speed * RobotPreferences.drivetrainLowSpeed.getValue();
      turn = turn * RobotPreferences.drivetrainLowTurnSpeed.getValue();

    }
    leftMaster.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, -turn);
    rightMaster.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, turn);
  }

  public void driveDistance(double distance) {
    configure();
    leftMaster.set(ControlMode.Position, distance * RobotPreferences.motProfSensorUnitsPerFt.getValue() / 12);
    rightMaster.set(ControlMode.Position, distance * RobotPreferences.motProfSensorUnitsPerFt.getValue() / 12);

  }

  public int getPositionError() {
    return leftMaster.getClosedLoopError();
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
    leftMaster.setSelectedSensorPosition(0);
    rightMaster.setSelectedSensorPosition(0);
  }

  public void startMotionProfile(BufferedTrajectoryPointStream pointsLeft, BufferedTrajectoryPointStream pointsRight) {
    configure();

    leftMaster.startMotionProfile(pointsLeft, 10, ControlMode.MotionProfile);
    rightMaster.startMotionProfile(pointsRight, 10, ControlMode.MotionProfile);
  }

  public boolean isMotionProfileFinished() {
    return (rightMaster.isMotionProfileFinished() && leftMaster.isMotionProfileFinished());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    SmartDashboard.putNumber("Drivetrain Left Encoder", getLeftEncoderCount());
    SmartDashboard.putNumber("Drivetrain Right Encoder", getRightEncoderCount());
    SmartDashboard.putBoolean("Motion Finished", isMotionProfileFinished());
  }
}