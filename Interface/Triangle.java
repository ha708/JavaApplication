package Interface;

import java.awt.*;

class Triangle extends Shape implements Drawable, Measurable, Movable {

    Point p1;
    Point p2;
    Point p3;

    Triangle(Point p1, Point p2, Point p3) {
        this.size = 1;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    @Override
    public void draw() {
        System.out.println("三角を描きました。");
    }

    @Override
    public int getArea() {
        //外積を用いる(pointクラスのdistanceを求めるメソッドを使ってもOK)
        return ((p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x)) / 2;
    }

    @Override
    public int getPerimeter() {
        //少数以下は切り捨てている。
        return (int)(p1.distance(p2) + p2.distance(p3) + p3.distance(p1));
    }

    @Override
    public void parallelMove() {
        System.out.println("平行移動しました。");
    }

    @Override
    public void rotate() {
        System.out.println("回転しました。");
    }

}
