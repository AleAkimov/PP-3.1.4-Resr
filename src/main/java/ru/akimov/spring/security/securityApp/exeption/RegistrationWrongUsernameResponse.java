package ru.akimov.spring.security.securityApp.exeption;

public class RegistrationWrongUsernameResponse {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RegistrationWrongUsernameResponse(String message) {
        this.message = message;
    }
}