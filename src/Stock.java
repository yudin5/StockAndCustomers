class Stock {
    private volatile int balance = 1000;

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
            return 0;
        }

        if (getBalance() > goodsToWithdraw) {
            balance -= goodsToWithdraw;
            System.out.println("Со склада успешно выдано " + goodsToWithdraw + " единиц товара.");
            System.out.println("Остаток товара на складе: " + getBalance());
            return goodsToWithdraw;
        } else { // Выдать остатки
            int remainder = balance;
            setBalance(0);
            System.out.println("Выдано " + remainder + " единиц товара");
            System.out.println("Товар закончился!");
            return remainder;
        }
    }
}
