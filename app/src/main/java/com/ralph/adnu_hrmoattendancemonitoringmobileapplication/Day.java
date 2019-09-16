/*  Day Parsing Algorithm

*   Created by Ralph San Jose Eufracio
*   BSIT Student
*   Ateneo de Naga University
* */

package com.ralph.adnu_hrmoattendancemonitoringmobileapplication;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class Day {
    private String inputDay;

    private Dictionary dayPool = new Hashtable();

    public Day(String inputDay) {
        this.inputDay = inputDay;
        dayPool.put("M","MON");
        dayPool.put("T","TUE");
        dayPool.put("W","WED");
        dayPool.put("TH","THU");
        dayPool.put("F","FRI");
        dayPool.put("S","SAT");
    }

    public String parseDay(){
        StringBuilder parsedDay = new StringBuilder();
        inputDay.toUpperCase();

        /*  Examples:
        *   MON TUE WED THU FRI SAT
        *   MW TTH
        *   M-F M-TH
        *   FSA??
        *
        * */
        if (inputDay.contains("-")){
            ArrayList<String> dayDashPool = new ArrayList<>();
            dayDashPool.add("M");
            dayDashPool.add("T");
            dayDashPool.add("W");
            dayDashPool.add("TH");
            dayDashPool.add("F");
            dayDashPool.add("SA");

            int dashIndex;

            dashIndex = inputDay.indexOf("-");

            int prevIndex = dashIndex - 1;
            int nextIndex = dashIndex + 1;

            Character charPrev = inputDay.charAt(prevIndex);
            String stringPrev = charPrev.toString();

            Character charNext = inputDay.charAt(nextIndex);
            String stringNext = charNext.toString();

            for(int i = dayDashPool.indexOf(stringPrev); i <= dayDashPool.indexOf(stringNext); i++){
                parsedDay.append(dayPool.get(dayDashPool.get(i)));
            }

        }else if(inputDay.contains("MON") || inputDay.contains("TUE") || inputDay.contains("WED") || inputDay.contains("THU") || inputDay.contains("FRI") || inputDay.contains("SAT")){

            return inputDay;
        }else{
            if(inputDay.contains("M")){
                inputDay.replace("M", "");
                parsedDay.append("MON");
            }
            if(inputDay.contains("T")){
                inputDay.replace("M", "");
                parsedDay.append("TUE");
            }
            if(inputDay.contains("W")){
                inputDay.replace("M", "");
                parsedDay.append("WED");
            }
            if(inputDay.contains("TH")){
                inputDay.replace("M", "");
                parsedDay.append("THU");
            }
            if(inputDay.contains("F")){
                inputDay.replace("M", "");
                parsedDay.append("FRI");
            }
            if(inputDay.contains("SA")){
                inputDay.replace("M", "");
                parsedDay.append("SAT");
            }
        }


        return parsedDay.toString();
    }
}
