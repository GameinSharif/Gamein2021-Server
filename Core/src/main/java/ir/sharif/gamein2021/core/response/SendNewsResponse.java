package ir.sharif.gamein2021.core.response;

import ir.sharif.gamein2021.core.domain.dto.NewsDto;
import ir.sharif.gamein2021.core.util.ResponseTypeConstant;
import ir.sharif.gamein2021.core.view.ResponseObject;

import java.io.Serializable;
import java.util.List;

public class SendNewsResponse extends ResponseObject implements Serializable {
    List<NewsDto> news;

    public SendNewsResponse(ResponseTypeConstant responseTypeConstant, List<NewsDto> news)
    {
        this.responseTypeConstant = responseTypeConstant.ordinal();
        this.news = news;
    }
}
