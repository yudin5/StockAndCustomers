import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SalesActivity {
    private static int numberOfCustomers; // Передается в качестве параметра
    static int getNumberOfCustomers() {return numberOfCustomers;}

    public static void main(String[] args) {
        try {
            // Проверяем аргументы
            if (args.length != 1) throw new IllegalArgumentException();
            numberOfCustomers = Integer.parseInt(args[0]);
            if (numberOfCustomers <= 0) throw new NumberFormatException();

            // Начинаем работу
            Stock stock = new Stock(); // Создаем склад
            ExecutorService exec = Executors.newCachedThreadPool(); // Создаем пул для покупателей

            for (int i = 0; i < numberOfCustomers; i++) {
                exec.execute(new Customer(stock)); // Покупатели начинают покупать со склада
            }
            exec.shutdown();

            try {
                // Ждем выполнения всех потоков
                exec.awaitTermination(2, TimeUnit.SECONDS);
                // while (exec.awaitTermination(1, TimeUnit.SECONDS)) { }
            } catch (InterruptedException ex) { System.out.println("Ошбика прерывания"); }

            // Выводим итоговую таблицу на экран
            showResult();

        } catch (NumberFormatException ex) {
            System.out.println("Количество покупателей должно быть целым положительным числом.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введите корректные аргументы. Одно положительно число.");
        }
    }

    private static void showResult() {
        /*try {
            TimeUnit.MILLISECONDS.sleep(5);
        } catch (InterruptedException ex) {
            System.out.println("Ошибка прерывания при показе результатов");
        }*/

        // Выводим всех покупателей
        ArrayList<String> list = Customer.getListOfCustomers(); // Берем список покупателей
        Collections.sort(list); // Сортируем список
        System.out.println("==========================================");
        System.out.println("ID      \t\t" + "ПОКУПОК\t\t" + " ТОВАРА\t\t");
        for (String line : list) {
            System.out.println(line);
        }
    }
}
