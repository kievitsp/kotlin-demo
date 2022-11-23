package java8;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;

public interface TradeRepo {

    Maybe<Trade> getTradeById(String id);

    Flowable<Trade> getBulk();


}