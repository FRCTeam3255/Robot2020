/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotPreferences;

public class Vision extends SubsystemBase {
  /**
   * Creates a new ShooterSubsystem.
   */

  UsbCamera camera;
  CvSink cvSink;
  CvSource outputStream;
  CvSource sourceStream;
  Mat source;
  Mat output;
  Mat circles;
  Mat binary;
  Mat mask;
  Mat heirarchy;
  List<MatOfPoint> contours;

  double xPosition;
  int camWidth = 160;
  int camHeight = 120;

  Scalar lower = new Scalar(35, 140, 60);
  Scalar upper = new Scalar(35, 140, 60);

  public Vision() {
    Thread thread = new Thread() {
      public void run() {
        startStreams();
      }
    };
    thread.start();

  }

  // Color sensing for balls
  public void startStreams() {

    camera = CameraServer.getInstance().startAutomaticCapture();

    cvSink = CameraServer.getInstance().getVideo();
    // TODO: consider 320x240 to reduce bandwidth for FMS. Don't need quality for
    // this
    // TODO: consider class constants for 640x480
    // id actually rather do 160 120, as thats what the feed seems to be from the
    // camera into the sink
    sourceStream = CameraServer.getInstance().putVideo("src", camWidth, camHeight);
    outputStream = CameraServer.getInstance().putVideo("filter", camWidth, camHeight);

    source = new Mat();
    output = new Mat();
    circles = new Mat();
    heirarchy = new Mat();
    binary = new Mat();

  }

  public double getX() {
    // TODO: Don't hardcode 80. Even if not a pref, use a variable because it's
    // probably a function of 640x480
    return (xPosition - (camWidth / 2));
  }

  // limelight
  public boolean visionHasTarget() {
    // TODO: Is < 1 always the right check?
    // No, but .9 is
    if ((NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0)) < .9) {
      return false;
    } else {
      return true;
    }
  }

  public double getVisionXError() {
    // TODO: Likely that scaling by innerHoleScalar is more complex than this logic
    // yeah, still wrestling with this one. pretty sure that its gonna end up being
    // err + skew times scalar times whatever side youre on, gonna need to do a grip
    // pipeline for this
    return (NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0));
  }

  public double getVisionArea() {
    return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0);
  }

  public boolean isXFinished() {
    return ((Math.abs(getVisionXError()) < RobotPreferences.visionXTol.getValue()));
  }

  public double getHoodVisionPosition() {
    // TODO: Let's review this math together
    // ok, this one is solid imm pretty sure

    // position is inverse to the area
    double position = (1 / (RobotPreferences.hoodVisionP.getValue() * 90 * getVisionArea()));

    // hard stop at 90 deg
    position = position * 90;
    if (position > 90) {
      position = 90;

    }
    // hard stop at 0 deg
    else if (position < 0) {
      position = 0;
    }
    return position;

  }

  @Override
  public void periodic() {

    cvSink.grabFrame(source);
    try {

      Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2HSV); // ! [houghcircles]
      // Core.inRange(source, lower, upper, output);

      Core.inRange(output,
          new Scalar(RobotPreferences.hLow.getValue(), RobotPreferences.sLow.getValue(),
              RobotPreferences.vLow.getValue()),
          new Scalar(RobotPreferences.hHigh.getValue(), RobotPreferences.sHigh.getValue(),
              RobotPreferences.vHigh.getValue()),
          circles);
      // TODO: Should the threshold values (100,255) be preferences?
      // no
      Imgproc.threshold(circles, binary, 100, 255, Imgproc.THRESH_BINARY);

      contours = new ArrayList<MatOfPoint>();
      Imgproc.findContours(binary, contours, heirarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

      double maxVal = 0;
      int maxValIdx = 0;

      for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
        double contourArea = Imgproc.contourArea(contours.get(contourIdx));
        if (maxVal < contourArea) {
          maxVal = contourArea;
          maxValIdx = contourIdx;
        }
      }

      mask = new Mat(source.rows(), source.cols(), CvType.CV_8U, Scalar.all(0));
      Imgproc.drawContours(mask, contours, maxValIdx, new Scalar(255), -1);

      // TODO: Let's review this logic together
      // yeah its yikes
      Moments m = Imgproc.moments(mask, true);
      double x = m.m10 / m.m00;
      xPosition = x;
      double y = m.m01 / m.m00;
      Point point = new Point(x, y);
      Imgproc.circle(source, point, 5, new Scalar(255, 0, 255), 3, 8, 0);
      SmartDashboard.putNumber("x", getX());
      sourceStream.putFrame(source);
      outputStream.putFrame(mask);
    } catch (Exception e) {
      System.out.println("something went wrong! Is your camera plugged in?");
    }
    // TODO: Let's review the meaning of each of these
    // we need to redo all of smartdashboard
    SmartDashboard.putNumber("xerrll", getVisionXError());
    SmartDashboard.putNumber("area err", getVisionArea());
    SmartDashboard.putNumber("given position",
        RobotPreferences.hoodCountsPerDegree.getValue() * RobotPreferences.hoodVisionP.getValue() * getVisionArea());
    SmartDashboard.putNumber("one over given position", getHoodVisionPosition());
    SmartDashboard.putBoolean("Vision Has Target", visionHasTarget());
    SmartDashboard.putBoolean("Vision X Finished", isXFinished());

    /*
      TODO: Idea for three SmartDashbord booleans:
        visionHasTarget DONE
        isXFinished DONE
        isHoodAligned (would this go here or elsewhere?)

        Dashboard buttons can be used as "borders" for the camera view so they light up when aligned
    */
  }
}
