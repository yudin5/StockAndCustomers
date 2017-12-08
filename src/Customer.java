import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Customer implements Runnable {

    static CyclicBarrier barrier = new CyclicBarrier(SalesActivity.getNumberOfCustomers());
    private static int customersCount = 1; // Счетчик покупателей
    private int id; // Обозначение покупателя
    private int productPurchased; // Количество купленного товара
    private int purchasesQuantity; // Количество сделанных покупок
    private Stock stock; // Магазин для покупок

    Customer(Stock stock) {
        id = customersCount++;
        this.stock = stock;
        productPurchased = 0;
        purchasesQuantity = 0;
    }

    int getProductPurchased() {
        return productPurchased;
    }

    int getPurchasesQuantity() {
        return purchasesQuantity;
    }

    void buy(int goodsToBuy) { // Передаем, сколько покупатель хочет купить
        System.out.println("Покупатель #" + id + " хочет купить " + goodsToBuy + " единиц товара.");
        synchronized (stock) {
            int realPurchased = stock.withdraw(goodsToBuy); // Сколько действительно куплено
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
            }
        }
    }

    public String toString() {
        return "Покупатель #" + id + "\tПокупок: " + getPurchasesQuantity() + "\tТоваров: " + getProductPurchased();
    }

    @Override
    public void run() {
        Random rand = new Random();
        while (!stock.isEmpty()) {
            buy(rand.nextInt(10) + 1); // от 1 до 10 включительно
            try {
                //barrier.await(500, TimeUnit.MILLISECONDS);
                barrier.await();
            } catch (BrokenBarrierException | InterruptedException ex) {
                System.out.println(this);
                return;
            }
        }
        barrier.reset();
        System.out.println(this);
    }
}
