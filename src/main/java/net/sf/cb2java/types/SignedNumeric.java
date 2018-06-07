package net.sf.cb2java.types;

public abstract class SignedNumeric extends Numeric {

    private SignPosition position;
    
    protected SignedNumeric(String name, int level, int occurs, final String picture, final SignPosition signPosition) {
        super(name, level, occurs, picture);
        this.position = signPosition;
    }

    protected SignPosition getSignPosition() {
        return position;
    }
    
}
