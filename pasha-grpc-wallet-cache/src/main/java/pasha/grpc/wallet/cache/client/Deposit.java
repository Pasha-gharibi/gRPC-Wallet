package pasha.grpc.wallet.cache.client;

/**
 * Created by p.gharibi on 5/11/2019 at 15.
 */
public class Deposit implements Caller {

    private Operation operation;

    public Deposit(Operation operation) {
        this.operation = operation;
    }

    @Override
    public void callServer(Integer userId) {
        operation.deposit(userId);
    }
}
