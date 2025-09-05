import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        System.out.println("Введите число a: ");
        int a = new Scanner(System.in).nextInt();

        System.out.println("Введите число b: ");
        int b = new Scanner(System.in).nextInt();

        System.out.println("Сумма "+ (int)a + " + " + (int)b + " = " + (int)(a+b));
        System.out.println("Разность "+ (int)a + " - " + (int)b + " = " + (int)(a-b));
        System.out.println("Произведение "+ (int)a + " * " + (int)b + " = " + (int)(a*b));
        System.out.println("Частное "+ (int)a + " / " + (int)b + " = " + (double)a/b);

    }
}
