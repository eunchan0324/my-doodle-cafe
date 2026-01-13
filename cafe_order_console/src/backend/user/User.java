package backend.user;

public class User {
    private String id;
    private String password;
    private UserRole role;
    private int storeId; // for seller role


    // ADMIN, CUSTOMER 회원 생성시 사용하는 생성자
    public User(String id, String password, UserRole role) {
        this.id = id;
        this.password = password;
        this.role = role;
    }

    // SELLER 회원생성 시 사용하는 생성자
    public User(String id, String password, UserRole role, int storeId) {
        this.id = id;
        this.password = password;
        this.role = role;
        this.storeId = storeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "User{" + "id='" + id + '\'' + ", password='" + password + '\'' + ", role='" + role + '\'' + '}';
    }
}
