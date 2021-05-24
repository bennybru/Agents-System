package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.application.subscribers.Intelligence;

public class Services {
    private int M;
    private int Moneypenny;
    private Intelligence[] intelligence;

    public int getM() {
        return M;
    }

    public void setM(int m) {
        M = m;
    }

    public int getMoneypenny() {
        return Moneypenny;
    }

    public void setMoneypenny(int moneypenny) {
        Moneypenny = moneypenny;
    }

    public Intelligence[] getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Intelligence[] intelligence) {
        this.intelligence = intelligence;
    }
}