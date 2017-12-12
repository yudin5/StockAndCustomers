import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Customer implements Runnable {

    private static volatile ArrayList<String> listOfCustomers = new ArrayList<>();
    private static CyclicBarrier barrier = new CyclicBarrier(SalesActivity.getNumberOfCustomers()); // Количество покупателей
    private static int customersCount = 1; // Счетчик покупателей
    private int id; // Обозначение покупателя
    private int productPurchased; // Количество купленного товара
    private int purchasesQuantity; // Количество сделанных покупок
    private Stock stock; // Магазин для покупок

    static ArrayList<String> getListOfCustomers() {return listOfCustomers;}

    Customer(Stock stock) {
        id = customersCount++;
        this.stock = stock;
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
        //synchronized (stock) {
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
            } else {
                barrier.reset();
            }
        //}
    }

    public String toString() {
        return "Покупатель #" + id + "\tПокупок: " + getPurchasesQuantity() + "\t Товаров: " + getProductPurchased();
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
                //System.out.println(this);
                break;
                //return;
            }
        }
        barrier.reset();
        //System.out.println(this);
        synchronized (listOfCustomers) {
            listOfCustomers.add(this.toString());
        }
    }
}
