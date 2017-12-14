import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SalesActivity {
    private static int numberOfCustomers; // Передается в качестве параметра
    static synchronized int getNumberOfCustomers() {return numberOfCustomers;}

    public static void main(String[] args) {
        try {
            // Проверяем аргументы
            if (args.length != 1) throw new IllegalArgumentException();
            numberOfCustomers = Integer.parseInt(args[0]);
            if (numberOfCustomers <= 0) throw new NumberFormatException();

            // Начинаем работу
            //Stock stock = Stock.getInstance(); // Создаем склад
            ExecutorService exec = Executors.newCachedThreadPool(); // Создаем пул для покупателей

            for (int i = 0; i < numberOfCustomers; i++) {
                exec.execute(new Customer()); // Покупатели начинают покупать со склада
            }
            exec.shutdown();

                // Ждем выполнения всех потоков
                while (!exec.isTerminated()) {}
                //exec.awaitTermination(2, TimeUnit.SECONDS);

            // Выводим итоговую таблицу на экран
            showResult();

        } catch (NumberFormatException ex) {
            System.out.println("Количество покупателей должно быть целым положительным числом.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введите корректные аргументы. Одно положительное целое число.");
        }
    }

    private static void showResult() {

        // Выводим всех покупателей
        ArrayList<Customer> list = Stock.getInstance().getListOfCustomers(); // Берем список покупателей
        Collections.sort(list); // Сортируем список
        System.out.println("=========================================");
        System.out.println("ID      \t\t" + "ПОКУПОК\t\t" + " ТОВАРА\t\t");
        for (Customer customer : list) {
            System.out.println(customer);
        }
    }
}
