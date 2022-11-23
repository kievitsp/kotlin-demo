package java8;

import io.reactivex.rxjava3.disposables.Disposable;

public class JavaApplication {
    public static TradeRepo getTradeRepo() {
        throw new RuntimeException();
    }



    public static void main(String[] args) {
        TradeRepo repo = getTradeRepo();

        // either
        Trade syncTrade = repo
                .getTradeById("123")
                // no asynchronous
                .blockingGet();

        Disposable disposable = repo.getTradeById("123")
                .subscribe(
                        asyncTrade -> System.out.println("Received trade " + asyncTrade),
                        Throwable::printStackTrace
                );

        // have to remember to cancel
        disposable.dispose();
    }
}
