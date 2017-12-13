import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

class Stock {
    private volatile int balance = 1000;
    private volatile ArrayList<Customer> listOfCustomers;
    private static Stock instance;
    private static CyclicBarrier barrier; //= new CyclicBarrier(SalesActivity.getNumberOfCustomers()); // Количество покупателей

    private Stock() {}

    public static synchronized Stock getInstance() {
        if (instance == null) {
            instance = new Stock();
        }
        return instance;
    }

    public static synchronized CyclicBarrier getBarrier() {
        if (barrier == null) {
            barrier = new CyclicBarrier(SalesActivity.getNumberOfCustomers()); // Количество покупателей
        }
        return barrier;
    }

    public synchronized ArrayList<Customer> getListOfCustomers() {
        if (listOfCustomers == null) {
            listOfCustomers = new ArrayList<>();
        }
        return listOfCustomers;
    }

    private synchronized int getBalance() {
        return balance;
    }

    private synchronized void setBalance(int amount) {
        balance = amount;
    }

    synchronized boolean isEmpty() {
        return (balance == 0);
    }

    synchronized int withdraw(int goodsToWithdraw) {
        if (isEmpty()) {
            return 0; // Если склад пустой
        }

        if (getBalance() > goodsToWithdraw) { // Если на складе больше товара, чем требует покупатель
            setBalance(getBalance() - goodsToWithdraw);
            System.out.println("Со склада успешно выдано " + goodsToWithdraw + " единиц товара.");
            System.out.println("Остаток товара на складе: " + getBalance());
            return goodsToWithdraw;
        } else { // Товара недостаточно для запроса, выдаем остатки
            int remainder = getBalance(); // Остаток
            setBalance(0); // Устанавливаем баланс в ноль
            System.out.println("Выдано " + remainder + " единиц товара");
            System.out.println("Товар закончился!");
            return remainder; // Выдаем остаток
        }
    }
}
