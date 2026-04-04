package runtime;

import java.util.Optional;

public interface VariableStore {
    void set(String name, Object value);

    Optional<Object> get(String name);

    boolean has(String name);
}