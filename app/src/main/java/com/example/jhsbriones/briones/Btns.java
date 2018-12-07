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

//    public int getBtnIndex() {
//        return this.index;
//    }

    public boolean isFirst() {
        if(this.btnStatus == Status.FIRST) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isSecond() {
        if(this.btnStatus == Status.SECOND) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isMatched() {
        if (this.btnStatus == Status.MATCHED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNotPicked() {
        if(this.btnStatus == Status.NOT_PICKED) {
            return true;
        }
        else {
            return false;
        }
    }

    public void updateStatus(Status status) {
        this.btnStatus = status;
    }
}

enum Status {
    MATCHED, FIRST, SECOND, NOT_PICKED
}
