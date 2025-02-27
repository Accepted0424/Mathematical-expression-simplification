package expr;

public class Mono {
    private int coe;
    private final int varsPow;

    public Mono(int coe, int varsPow) {
        this.coe = coe;
        this.varsPow = varsPow;
    }

    public int getCoe() {
        return coe;
    }

    public int getVarsPow() {
        return varsPow;
    }

    public void inverseCoe() {
        this.coe = -coe;
    }

    @Override
    public String toString() {
        String powString = "";
        String coeString = "";
        if (varsPow == 0) {
            powString = "";
        } else if (varsPow == 1) {
            powString = "x";
        } else {
            powString = "x^" + varsPow;
        }

        if (coe == 0) {
            coeString = "";
            powString = "";
        } else if (coe == 1 || coe == -1) {
            coeString = "";
            if (coe == -1) {
                coeString = "-";
            }
            if (varsPow == 0) {
                coeString = String.valueOf(coe);
            }
        } else {
            coeString = coe + "*";
            if (varsPow == 0) {
                coeString = coe + "";
            }
        }
        return coeString + powString;
    }

}
