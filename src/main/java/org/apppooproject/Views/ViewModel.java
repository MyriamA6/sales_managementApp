package org.apppooproject.Views;

public class ViewModel {
    private static ViewModel instance;
    private final ViewFactory viewFactory;

    private ViewModel() {
        viewFactory = new ViewFactory();
    }
    public static ViewModel getInstance() {
        if (instance == null) {
            instance = new ViewModel();
        }
        return instance;
    }
}
