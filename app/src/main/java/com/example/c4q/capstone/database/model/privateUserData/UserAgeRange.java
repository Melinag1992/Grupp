package com.example.c4q.capstone.database.model.privateUserData;


class UserAgeRange {
    private boolean over_18;
    private boolean over_21;

    public boolean isOver_18() {
        return over_18;
    }

    public void setOver_18(boolean over_18) {
        this.over_18 = over_18;
    }

    public boolean isOver_21() {
        return over_21;
    }

    public void setOver_21(boolean over_21) {
        this.over_21 = over_21;
    }
}