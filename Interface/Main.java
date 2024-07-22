package Interface;

import java.awt.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Triangle t1 = new Triangle(
                new Point(3,2),
                new Point(1,3),
                new Point(1,1));
        t1.parallelMove();
        t1.rotate();
        System.out.println(t1.getPerimeter());
        Drawable h = new Hero();
        List<Drawable> list = List.of(t1, h);
        for(Drawable e: list) {
            e.draw();
        }
    }
}
