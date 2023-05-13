package TeamDustKGU.dustbackend.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleResponseWrapper<T> {
    private T result;
}