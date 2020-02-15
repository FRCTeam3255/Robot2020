/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motion.BufferedTrajectoryPointStream;
import com.ctre.phoenix.motion.TrajectoryPoint;
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

  private TalonFXConfiguration _config;

  public Drivetrain() {
    leftMaster = new TalonFX(RobotMap.DRIVETRAIN_LEFT_FRONT_TALON);
    leftSlave = new TalonFX(RobotMap.DRIVETRAIN_LEFT_BACK_TALON);
    rightMaster = new TalonFX(RobotMap.DRIVETRAIN_RIGHT_FRONT_TALON);
    rightSlave = new TalonFX(RobotMap.DRIVETRAIN_RIGHT_BACK_TALON);
    _config = new TalonFXConfiguration();

    _config.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
    /* rest of the configs */
    _config.neutralDeadband = RobotPreferences.motProfNeutralDeadband
        .getValue(); /* 0.1 % super small for best low-speed control */
    // TODO: These preferences only get called at robot power up. Should have a reload method and/or reload at PID start
    _config.slot0.kF = RobotPreferences.motProfF.getValue();
    _config.slot0.kP = RobotPreferences.motProfP.getValue();
    _config.slot0.kI = RobotPreferences.motProfI.getValue();
    _config.slot0.kD = RobotPreferences.motProfD.getValue();
    _config.peakOutputForward = 1;
    _config.peakOutputReverse = -1;
    _config.slot0.integralZone = (int) RobotPreferences.motProfIz.getValue();
    _config.slot0.closedLoopPeakOutput = RobotPreferences.motProfPeakOut.getValue();
    rightMaster.configAllSettings(_config);
    leftMaster.configAllSettings(_config);
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
    // TODO: Don't use constants for motor tuning. Use prefs with a reload. And our motors max at 20% power?!?
    leftMaster.set(ControlMode.PercentOutput, .2 * speed, DemandType.ArbitraryFeedForward, -.2 * turn);
    rightMaster.set(ControlMode.PercentOutput, .2 * speed, DemandType.ArbitraryFeedForward, .2 * turn);
  }

  public void driveDistance(double distance) {
    // TODO: Do you really want distance in feet? Decimal feet can be hard which is why we usually use inches
    leftMaster.set(ControlMode.Position, distance * RobotPreferences.motProfSensorUnitsPerFt.getValue());
    leftSlave.follow(leftMaster);
    rightMaster.set(ControlMode.Position, distance * RobotPreferences.motProfSensorUnitsPerFt.getValue());
    rightSlave.follow(rightMaster);

  }

  public int getPositionError() {
    return leftMaster.getClosedLoopError();
  }

  /*
    TODO: It appears resetPositionPID is how you stop driveDistance, and resetMotionProfile is how you stop startMotionProfile.
    Could improve names to make that more clear. Since it would probably be ok to clearMotionProfiles inside of resetPositionPID,
    why not just rename resetMotionProfile to startArcadeDrive and delete resetPositionPID.
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
    // TODO: Did this come from a recipe? See method comment. Why not use leftMaster.setSelectedSensorPosition(0)
    leftMaster.getSensorCollection().setIntegratedSensorPosition(0, 100);
    rightMaster.getSensorCollection().setIntegratedSensorPosition(0, 100);
  }

  /*
    TODO: None of the code in this method deals with drivetrain. This should be part of SN_MotionProfile class.
    However, if you move it, you can't call RobotPreferences inside it. But you could have a SN_MotionProfile take an SN_Pref
  */
  public void initBuffer(final BufferedTrajectoryPointStream bufferedStream, final double[][] profile,
      final int totalCnt) {

    final boolean forward = true; // set to false to drive in opposite direction of profile (not really needed
    // since you can use negative numbers in profile).

    final TrajectoryPoint point = new TrajectoryPoint(); // temp for for loop, since unused params are initialized
    // automatically, you can alloc just one

    /* clear the buffer, in case it was used elsewhere */
    bufferedStream.Clear();

    /* Insert every point into buffer, no limit on size */
    for (int i = 0; i < totalCnt; ++i) {

      final double direction = forward ? +1 : -1;
      final double positionRot = profile[i][0];
      final double velocityRPM = profile[i][1];
      final int durationMilliseconds = (int) profile[i][2];

      /* for each point, fill our structure and pass it to API */
      point.timeDur = durationMilliseconds;

      /* drive part */
      point.position = direction * positionRot * RobotPreferences.motProfSensorUnitsPerFt.getValue(); // Rotations =>
                                                                                                      // sensor units
      point.velocity = direction * velocityRPM * RobotPreferences.motProfSensorUnitsPerFt.getValue() / 600.0; // RPM =>
                                                                                                              // units
                                                                                                              // per
                                                                                                              // 100ms
      point.arbFeedFwd = 0; // good place for kS, kV, kA, etc...

      point.profileSlotSelect0 = 0; /* which set of gains would you like to use [0,3]? */
      point.zeroPos = false; /* don't reset sensor, this is done elsewhere since we have multiple sensors */
      point.isLastPoint = ((i + 1) == totalCnt); /* set this to true on the last point */
      point.useAuxPID = false; /* tell MPB that we aren't using both pids */

      bufferedStream.Write(point);
    }
  }

  // TODO: Feels like this should take a MotionProfile class and then access the left and right points
  // TODO: This is where you can reset PIDs
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