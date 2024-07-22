package Interface;

class Hero implements Drawable {
    @Override
    public void draw() {
        System.out.println("勇者を描きました。");
    }
}