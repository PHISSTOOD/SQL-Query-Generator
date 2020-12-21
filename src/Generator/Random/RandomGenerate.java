package Generator.Random;

import java.util.Random;

public class RandomGenerate {

    public static int randomPick50(){
        Random random = new Random();
        return random.nextInt(50);
    }

    public static int randomPick5(){
        Random random = new Random();
        return random.nextInt(5);
    }

    public static int randomPick8(){
        Random random = new Random();
        return random.nextInt(8);
    }

    public static int randomPick10(){
        Random random = new Random();
        return random.nextInt(10);
    }

    public static int randomPick100(){
        Random random = new Random();
        return random.nextInt(100)+1;
    }

    public static int randomPickSpecific(int num){
        Random random = new Random();
        return random.nextInt(num);
    }

    public static String randomString(){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        int length = random.nextInt(10)+1;
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
