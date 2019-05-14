package pasha.grpc.wallet.cache.client;

import pasha.grpc.wallet.cache.client.Round;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by p.gharibi on 5/12/2019 at 08.
 */
public class RoundBunch implements Runnable {

    List<Round> roundList = new ArrayList<>();
    Integer userId;

    @Override
    public void run() {
        for (Round round : roundList) {
            round.send(this.userId);
        }
    }

    public List<Round> getRoundList() {
        return roundList;
    }

    public void setRoundList(List<Round> roundList) {
        this.roundList = roundList;
    }


    public void addRound(Round round){
        roundList.add(round);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
