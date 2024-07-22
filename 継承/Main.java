package fruits;

public class Main {
    public static void main(String[] args){
        Apple apple = new Apple(100, "Japan", "red", "甘い");
        Fruits f =new Fruits(150,"Canada","Gold");

        System.out.println(apple.isMadeInJapan());
        System.out.println(apple.price);
        System.out.println(apple.madeIn);
        System.out.println(apple.isMadeInJapan());
        System.out.println(f.getDetail());
        System.out.println(apple.getDetail());
        System.out.println(f);
        System.out.println(apple);

    }
}
