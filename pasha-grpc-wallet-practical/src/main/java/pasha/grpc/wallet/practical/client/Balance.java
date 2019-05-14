package pasha.grpc.wallet.practical.client;

/**
 * Created by p.gharibi on 5/11/2019 at 15.
 */
public class Balance implements Caller {
    private Operation operation;

    public Balance(Operation operation) {
        this.operation = operation;
    }

    @Override
    public void callServer(Integer userId) {
        operation.balance(userId);
    }
}
