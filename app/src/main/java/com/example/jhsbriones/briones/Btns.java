package com.example.jhsbriones.briones;

public class Btns {
    private int btnID;
//    private int index;
    private Status btnStatus;

    public Btns(int id) {
        this.btnID = id;
//        this.index = index;
        this.btnStatus = Status.NOT_PICKED;
    }

    public int getButtonID() {
        return this.btnID;
    }

//    public int getBtnIndex() {
//        return this.index;
//    }

    public boolean isCheckStatus(Status status) {
        if (this.btnStatus == status) {
            return true;
        } else {
            return false;
        }
    }

    public void updateStatus(Status status) {
        this.btnStatus = status;
    }
}

enum Status {
    MATCHED, WRONG, PENDING, NOT_PICKED
}
