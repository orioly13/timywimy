package timywimy.util;

public class PairFieldName<F, Stirng> extends Pair<F, String> {

    public PairFieldName(F field, String name) {
        super(field, name);
    }

    @Override
    public F getFirst() {
        return super.getFirst();
    }

    @Override
    public void setFirst(F first) {
        super.setFirst(first);
    }

    @Override
    public String getSecond() {
        return super.getSecond();
    }

    @Override
    public void setSecond(String second) {
        super.setSecond(second);
    }
}
