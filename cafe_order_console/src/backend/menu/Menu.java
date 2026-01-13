package backend.menu;

import java.util.UUID;

// Menu 기능
public class Menu {
    private UUID id;
    private String name;
    private int price;
    private MenuCategory option;
    private String recommend;

    // 신규 메뉴 생성 시 사용하는 생성자
    public Menu(String name, int price, MenuCategory option) {
        this(UUID.randomUUID(), name, price, option);
    }

    // 파일에서 기존 메뉴를 로드할 때 사용할 생성자
    public Menu(UUID id, String name, int price, MenuCategory option) {
        // 가격 음수 검증
        if (price < 0) {
            return;
        }
        this.id = id;
        this.name = name;
        this.price = price;
        this.option = option;
    }

    public Menu(String recomReason) {
        this.recommend = recomReason;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public MenuCategory getOption() {
        return option;
    }

    public void setOption(MenuCategory option) {
        this.option = option;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    @Override
    public String toString() {
        return "[1.메뉴 이름 = " + name + ", 2.가격 = " + price + '원' + ", 3.옵션 = " + option + ", 4.추천 여부 = " + recommend + ']';
    }
}
