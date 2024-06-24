package pair2;

public class Pair2<T,S> {
    public T first;
    public S second;

    Pair2(T first,S second){
        this.first =first;
        this.second = second;
    }

    @Override
    public String toString(){
        return"(" + this.first +
                "," + this.second + ")";
    }
}
