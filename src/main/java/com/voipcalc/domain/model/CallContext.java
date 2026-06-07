package com.voipcalc.domain.model;

import java.time.LocalDateTime;

public class CallContext {

    private final String caller;

    private final String callee;

    private final UserType userType;

    private final LocalDateTime startTime;

    public CallContext(
            String caller,
            String callee,
            UserType userType,
            LocalDateTime startTime) {

        this.caller = caller;
        this.callee = callee;
        this.userType = userType;
        this.startTime = startTime;
    }

    public String getCaller() {
        return caller;
    }

    public String getCallee() {
        return callee;
    }

    public UserType getUserType() {
        return userType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}