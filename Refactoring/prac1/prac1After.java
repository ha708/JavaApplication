package n1_a;

import java.util.List;

public class prac1After {
    public static void main(String[] args){
        List<Integer> list1 = List.of(3,2,1);
        addAndPrint(list1,"Sum1:");

        List<Integer> list2 = List.of(3,2,11);
        addAndPrint(list2,"Sum2:");

        List<Integer> list3 = List.of(3,21,11);
        addAndPrint(list3,"Sum3");

    }

    public static int addAndPrint(List<Integer>list,String message){
        int sum = 0;
        for(Integer e : list){
            sum += e;
        }
        System.out.println(message + sum);
        return sum;
    }
}
