package backend.order;

import backend.menu.Menu;

public class OrderItem {
    private Menu menu;
    private int finalPrice;
    private String finalOptions;
    private String finalCup;
    private String finalTemp;

    public OrderItem(Menu menu) {
        this.menu = menu;
    }

    public OrderItem(Menu menu, int finalPrice, String finalOptions, String finalCup, String finalTemp) {
        this.menu = menu;
        this.finalPrice = finalPrice;
        this.finalOptions = finalOptions;
        this.finalCup = finalCup;
        this.finalTemp = finalTemp;
    }

    public Menu getMenu() {
        return menu;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public String getFinalOptions() {
        return finalOptions;
    }

    public String getFinalCup() {
        return finalCup;
    }

    public String getFinalTemp() {
        return finalTemp;
    }
}
