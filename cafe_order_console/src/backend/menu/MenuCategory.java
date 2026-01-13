package backend.menu;

public enum MenuCategory {
    COFFEE(1),
    LATTE(2),
    TEA(3);

    private final int value;

    MenuCategory(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


    public static MenuCategory fromValue(int value) {
        for (MenuCategory category : MenuCategory.values()) {
            if (category.getValue() == value) {
                return category;
            }
        }
        return null;
    }
}
