package ir.sharif.gamein2021.core.domain.dto;

public interface BaseDto<T> {
    void setId(T id);
    T getId();
}
