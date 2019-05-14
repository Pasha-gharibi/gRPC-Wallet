package pasha.grpc.wallet.cache.server.enm;

import java.util.Arrays;
import java.util.Optional;

public enum Currency {
    USD(0),
    EUR(1),
    GBP(2),
    UNRECOGNIZED(-1);

    private int value;
    Currency(int value){
        this.value=value;
    }

    public static Optional<Currency> valueOf(int i) {
        return Arrays.stream(values())
                .filter(legNo -> legNo.value == i)
                .findFirst();
    }

    public int getValue() {
        return value;
    }
}

