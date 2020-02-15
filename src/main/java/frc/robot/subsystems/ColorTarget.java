package frc.robot.subsystems;

import edu.wpi.first.wpilibj.util.Color;
import frcteam3255.robotbase.Preferences.SN_DoublePreference;

public class ColorTarget {
    double lowRed;
    double highRed;
    double lowGreen;
    double highGreen;
    double lowBlue;
    double highBlue;

    SN_DoublePreference lowRedPref;
    SN_DoublePreference highRedPref;
    SN_DoublePreference lowGreenPref;
    SN_DoublePreference highGreenPref;
    SN_DoublePreference lowBluePref;
    SN_DoublePreference highBluePref;

    ColorTarget(SN_DoublePreference a_lowRedPref, SN_DoublePreference a_highRedPref, SN_DoublePreference a_lowGreenPref,
            SN_DoublePreference a_highGreenPref, SN_DoublePreference a_lowBluePref,
            SN_DoublePreference a_highBluePref) {
        lowRedPref = a_lowRedPref;
        highRedPref = a_highRedPref;
        lowGreenPref = a_lowGreenPref;
        highGreenPref = a_highGreenPref;
        lowBluePref = a_lowBluePref;
        highBluePref = a_highBluePref;
        reloadValues();

    }

    void reloadValues() {
        lowRed = lowRedPref.getValue();
        highRed = highRedPref.getValue();
        lowGreen = lowGreenPref.getValue();
        highGreen = highGreenPref.getValue();
        lowBlue = lowBluePref.getValue();
        highBlue = highBluePref.getValue();
    }

    boolean matchesColor(Color color) {
        boolean redTarget = (lowRed < color.red) && (color.red < highRed);
        boolean greenTarget = (lowGreen < color.green) && (color.green < highGreen);
        boolean blueTarget = (lowBlue < color.blue) && (color.blue < highBlue);
        if (redTarget && greenTarget && blueTarget) {
            return true;
        }
        return false;
    }
}