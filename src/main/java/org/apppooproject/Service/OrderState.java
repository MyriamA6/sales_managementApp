package org.apppooproject.Service;

public enum OrderState {IN_PROGRESS("in progress"), PAID("paid"), DELIVERED("delivered");
    private final String state;
    private OrderState(String state) {
        this.state = state;
    }
    public String getState() {
        return state;
    }

    public boolean equalState(String state) {
        return this.state.equals(state);
    }

    public static OrderState giveCorrespondingState(String state) {
        for (OrderState stateOption : OrderState.values()) {
            if (stateOption.getState().equals(state)) {
                return stateOption;
            }
        }
        return null;
    }
}
