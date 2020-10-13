package Demo1;

import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        MyHashMap<String, String> myHashMap = new MyHashMap<>();
        myHashMap.put("123", "456");
        myHashMap.put("1234", "4567");
        myHashMap.put("1233", "4568");
        myHashMap.put("1232", "4569");
        myHashMap.put("1231", "4561");
        for (String s : myHashMap.keySet()
        ) {
            System.out.println(s);

        }


    }
}
