package com.ideal2s.achatmarket;

public class Observation {


    String Observation;
    String QT;

    public Observation(String observation, String QT) {
        Observation = observation;
        this.QT = QT;
    }

    public String getObservation() {
        return Observation;
    }

    public void setObservation(String observation) {
        Observation = observation;
    }

    public String getQT() {
        return QT;
    }

    public void setQT(String QT) {
        this.QT = QT;
    }
}
