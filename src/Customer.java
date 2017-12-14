import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

public class Customer implements Runnable, Comparable<Customer> {

    //private static volatile ArrayList<String> listOfCustomers = new ArrayList<>();
    //private static CyclicBarrier barrier = new CyclicBarrier(SalesActivity.getNumberOfCustomers()); // Количество покупателей
    private static int customersCount = 1; // Счетчик покупателей, нужен только для создания уникального имени
    private int id; // Обозначение покупателя
    private int productPurchased; // Количество купленного товара
    private int purchasesQuantity; // Количество сделанных покупок
    //private Stock stock; // Магазин для покупок

    //static ArrayList<String> getListOfCustomers() {return listOfCustomers;}

    Customer() {
        id = customersCount++;
        //this.stock = Stock.getInstance();
        productPurchased = 0;
        purchasesQuantity = 0;
    }

    private int getProductPurchased() {
        return productPurchased;
    }

    private int getPurchasesQuantity() {
        return purchasesQuantity;
    }

    private void buy(int goodsToBuy) { // Передаем, сколько покупатель хочет купить
        System.out.println("Покупатель #" + id + " хочет купить " + goodsToBuy + " единиц товара.");
        int realPurchased = Stock.getInstance().withdraw(goodsToBuy); // Сколько действительно куплено
        if (realPurchased > 0) {
            if (realPurchased == goodsToBuy) {
                purchasesQuantity++;
                System.out.println("Покупатель #" + id + " купил " + realPurchased + " единиц товара.\r\n");
                productPurchased += realPurchased;
            } else {
                purchasesQuantity++;
                System.out.println("Покупателю #" + id + " удалось купить лишь " + realPurchased + " единиц товара.\r\n");
                productPurchased += realPurchased;
            }
        } else {
            Stock.getBarrier().reset();
        }
    }

    public String toString() {
        return "Покупатель #" + id + "\tПокупок: " + getPurchasesQuantity() + "\t Товаров: " + getProductPurchased();
    }

    @Override
    public void run() {
        Random rand = new Random();
        while (!Stock.getInstance().isEmpty()) {
            buy(rand.nextInt(10) + 1); // от 1 до 10 включительно
            try {
                Stock.getBarrier().await();
            } catch (BrokenBarrierException | InterruptedException ex) {
                break;
            }
        }
        synchronized (Stock.getInstance().getListOfCustomers()) {
            Stock.getInstance().getListOfCustomers().add(this);
        }
        Stock.getBarrier().reset();
    }

    @Override
    public int compareTo(Customer o) {
        return id - o.id;
    }
}
