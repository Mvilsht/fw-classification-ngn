

public enum RULE_ACTION {

    ALLOW(true), DENY(false);

    private boolean isAllowed;

    RULE_ACTION(boolean isAllowed) {
        this.isAllowed = isAllowed;
    }

    public boolean isAllowed() {
        return isAllowed;
    }

}
