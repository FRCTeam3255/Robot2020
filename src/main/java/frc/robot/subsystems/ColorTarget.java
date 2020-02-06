package frc.robot.subsystems;

import edu.wpi.first.wpilibj.util.Color;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class ColorTarget {
    //TODO: fix naming conventions
    double lowRed;
    double highRed;
    double lowGreen;
    double highGreen;
    double lowBlue;
    double highBlue;
    
    SN_DoublePreference m_lowRedPref;
    SN_DoublePreference m_highRedPref;
    SN_DoublePreference m_lowGreenPref;
    SN_DoublePreference m_highGreenPref;
    SN_DoublePreference m_lowBluePref;
    SN_DoublePreference m_highBluePref;
  
    ColorTarget(SN_DoublePreference lowRedPref, SN_DoublePreference highRedPref, SN_DoublePreference lowGreenPref, SN_DoublePreference highGreenPref, SN_DoublePreference lowBluePref, SN_DoublePreference highBluePref) {
      m_lowRedPref = lowRedPref;
      m_highRedPref = highRedPref;
      m_lowGreenPref = lowGreenPref;
      m_highGreenPref = highGreenPref;
      m_lowBluePref = lowBluePref;
      m_highBluePref = highBluePref;
      reloadValues();

    }
  
    void reloadValues() {
        lowRed = m_lowRedPref.getValue()/255;
        highRed = m_highRedPref.getValue()/255;
        lowGreen = m_lowGreenPref.getValue()/255;
        highGreen = m_highGreenPref.getValue()/255;
        lowBlue = m_lowBluePref.getValue()/255;
        highBlue = m_highBluePref.getValue()/255;
    }
  
    boolean matchesColor(Color color) {
        if(((lowRed<color.red)&&(color.red<highRed))&&((lowGreen<color.green)&&(color.green<highGreen))&&((lowBlue<color.blue)&&(color.blue<highBlue))){
            return true;
        }
        return false;
    }
  }