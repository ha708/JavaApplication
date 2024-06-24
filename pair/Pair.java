package pair;

public class Pair<T> {
    public T first;
    public T second;

    public Pair(T first,T second){
        this.first =first;
        this.second = second;
    }

    @Override
    public String toString(){
        return"(" + this.first +
                "," + this.second + ")";
    }
}
