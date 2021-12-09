package ir.sharif.gamein2021.ClientHandler.domain.Leaderboard;

import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.util.models.Ranking;
import ir.sharif.gamein2021.core.view.ResponseObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
public class GetLeaderboardResponse extends ResponseObject implements Serializable
{
    public List<Ranking> rankings;
    public Integer yourRanking;
    public Float yourWealth;

    public GetLeaderboardResponse(ResponseTypeConstant responseTypeConstant, List<Ranking> rankings, Integer yourRanking, Float yourWealth)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.rankings = rankings;
        this.yourRanking = yourRanking;
        this.yourWealth = yourWealth;
    }
}
