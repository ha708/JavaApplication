package fruits;

public class Apple extends Fruits {
    String taste;
    Apple(int price, String madeIn, String color, String taste) {
        super(price, madeIn, color);
        this.taste = taste;
    }

    @Override
    String getDetail(){
        return"これはリンゴで生産地は" + this.madeIn + "色:" + this.color;
    }

    @Override
    public String toString() {
        return "りんごの値段は" + this.price + "円です。";
    }
}