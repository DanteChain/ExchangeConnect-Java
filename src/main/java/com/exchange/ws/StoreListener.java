package com.exchange.ws;

import com.exchange.domain.UserAsset;
import com.exchange.domain.UserFill;
import com.exchange.domain.UserOrder;
import com.exchange.domain.UserPosition;

public interface StoreListener {


    default void userAsset(UserAsset userAsset){

    }
    default void userFill(UserFill fill){

    }
    default void userPosition(UserPosition position){

    }

    default void userOrder(UserOrder order){

    }
}
