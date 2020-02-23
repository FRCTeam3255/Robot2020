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

  private boolean isUsingVision = false;
  double xPosition = 0;
  int camWidth = 320;
  int camHeight = 240;

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
    sourceStream = CameraServer.getInstance().putVideo("src", camWidth, camHeight);
    outputStream = CameraServer.getInstance().putVideo("filter", camWidth, camHeight);

    source = new Mat();
    output = new Mat();
    circles = new Mat();
    heirarchy = new Mat();
    binary = new Mat();

  }

  public void setUsingVision(boolean a_vision) {
    isUsingVision = a_vision;
  }

  public double getX() {
    return (xPosition - (80));
  }

  // limelight
  public boolean visionHasTarget() {
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

    if (isUsingVision) {
      try {
        cvSink.grabFrame(source);

        Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2HSV); // ! [houghcircles]
        // Core.inRange(source, lower, upper, output);

        Core.inRange(output,
            new Scalar(RobotPreferences.hLow.getValue(), RobotPreferences.sLow.getValue(),
                RobotPreferences.vLow.getValue()),
            new Scalar(RobotPreferences.hHigh.getValue(), RobotPreferences.sHigh.getValue(),
                RobotPreferences.vHigh.getValue()),
            circles);

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
        // System.out.println("something went wrong! Is your camera plugged in?");
      }
    }
    SmartDashboard.putNumber("LL X Error", getVisionXError());
    SmartDashboard.putNumber("LL Area Error", getVisionArea());
    SmartDashboard.putBoolean("LL Has Target", visionHasTarget());
    SmartDashboard.putBoolean("LL X Finished", isXFinished());


  }
}
