import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SalesActivity {
    static int numberOfCustomers;
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

        } catch (NumberFormatException ex) {
            System.out.println("Количество покупателей должно быть целым положительным числом.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Введите корректные аргументы. Одно положительно число.");
        }
    }
}
