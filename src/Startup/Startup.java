package Startup;

/*
 * TODO: 1. Update JLabel after song is finished - code is reached, text supposedly set, but JLabel empty
 *       2. Allow xx:xx as entry -> How to calculate the time till then???
 */

import FrontEnd.AlarmClock.AlarmClock;

public class Startup
{
    public static void main(String[] args) {
        new AlarmClock();
    }
}
