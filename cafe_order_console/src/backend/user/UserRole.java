package backend.user;

public enum UserRole {
    ADMIN(1, "관리자"),
    SELLER(2, "판매자"),
    CUSTOMER(3, "구매자"),
    UNROLE(4, "UNROLE"),
    ;

    private final int value;
    private final String role;

    UserRole(int value, String korean) {
        this.value = value;
        this.role = korean;
    }

    public int getValue() {
        return value;
    }

    public String getRole() {
        return role;
    }

    public static UserRole fromValue(int value) {
        for (UserRole role : UserRole.values()) {
            if (role.getValue() == value) {
                return role;
            }
        }
        return UNROLE;
    }

}
