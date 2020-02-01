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
import frc.robot.commands.MotionProfileReload;

public class Drivetrain extends SubsystemBase
{
  // Creates a new ExampleSubsystem
  private TalonFX leftMaster;
  private TalonFX leftSlave;
  private TalonFX rightMaster;
  private TalonFX rightSlave;

  private TalonFXConfiguration _config;

  public Drivetrain()
  {
    TalonFX leftMaster = new TalonFX(RobotMap.DRIVETRAIN_LEFT_FRONT_TALON);
    TalonFX leftSlave = new TalonFX(RobotMap.DRIVETRAIN_LEFT_BACK_TALON);
    TalonFX rightMaster = new TalonFX(RobotMap.DRIVETRAIN_RIGHT_FRONT_TALON);
    TalonFX rightSlave = new TalonFX(RobotMap.DRIVETRAIN_RIGHT_BACK_TALON);

    
    SmartDashboard.putData("Reload Profiles", new MotionProfileReload());

    _config.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
    /* rest of the configs */
    _config.neutralDeadband = RobotPreferences.motProfNeutralDeadband
        .getValue(); /* 0.1 % super small for best low-speed control */
    _config.slot0.kF = RobotPreferences.motProfF.getValue();
    _config.slot0.kP = RobotPreferences.motProfP.getValue();
    _config.slot0.kI = RobotPreferences.motProfI.getValue();
    _config.slot0.kD = RobotPreferences.motProfD.getValue();
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
    rightMaster.setInverted(true); /* right side has to apply +V to M-, to go forward */
    rightSlave.setInverted(true);
  }
  
  public void arcadeDrive(double speed, double turn)
  {
    leftMaster.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward,-turn);
    leftSlave.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward,-turn);
    rightMaster.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, turn);
    rightSlave.set(ControlMode.PercentOutput, speed, DemandType.ArbitraryFeedForward, turn);
  }

  public void resetMotionProfile(){
    rightMaster.set(ControlMode.PercentOutput, 0.0);
    leftMaster.set(ControlMode.PercentOutput, 0.0);
    rightMaster.clearMotionProfileTrajectories();
    leftMaster.clearMotionProfileTrajectories();
    resetEncoderCounts();
  }
  
  public double getLeftEncoderCount(){
    return leftMaster.getSelectedSensorPosition();
  }

  public double getRightEncoderCount(){
    return rightMaster.getSelectedSensorPosition();
  }

  public void resetEncoderCounts(){
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
      point.position = direction * positionRot * RobotPreferences.motProfSensorUnitsPerRot.getValue(); // Rotations =>
                                                                                                       // sensor units
      point.velocity = direction * velocityRPM * RobotPreferences.motProfSensorUnitsPerRot.getValue() / 600.0; // RPM =>
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

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    SmartDashboard.putNumber("Drivetrain Left Encoder", getLeftEncoderCount());
    SmartDashboard.putNumber("Drivetrain Right Encoder", getRightEncoderCount());
    SmartDashboard.putBoolean("Motion Finished", isMotionProfileFinished());
  }
}