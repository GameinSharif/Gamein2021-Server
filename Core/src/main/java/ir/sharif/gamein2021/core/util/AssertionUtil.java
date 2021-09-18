package ir.sharif.gamein2021.core.util;

import ir.sharif.gamein2021.core.domain.dto.BaseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AssertionUtil {

    public static void assertIdNotNull(Integer id, String entity) {
        Assert.notNull(id, "The " + entity + " id must not be null!");
    }

    public static void assertDtoNotNull(BaseDto baseDto, String entity) {
        Assert.notNull(baseDto, "The " + entity + " request must not be null!");
    }
}