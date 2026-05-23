package org.msominj.patientservice.exception;

public class PatientNoFoundException extends RuntimeException {
    public PatientNoFoundException(String message) {
        super(message);
    }
}
