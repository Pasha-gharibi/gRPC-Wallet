package pasha.grpc.wallet.practical.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by p.gharibi on 5/11/2019 at 15.
 */
public class Round {
    private List<Caller> round = new ArrayList<>();
    private Integer userId;

    public void takeRound(Caller caller) {
        round.add(caller);
    }

    public void send(Integer userId) {
        for (Caller caller : round) {
            caller.callServer(userId);
        }
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }


}
