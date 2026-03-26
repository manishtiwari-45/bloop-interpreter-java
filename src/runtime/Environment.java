package runtime;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

// Stores variables (name → value)
public class Environment<V> {

    // Internal storage
    private final Map<String, V> store = new HashMap<>();

    // Add or update variable
    public void set(String name, V value) {
        store.put(name, value);
    }

    // Get variable value (may be empty)
    public Optional<V> get(String name) {
        return Optional.ofNullable(store.get(name));
    }

    // Check if variable exists
    public boolean has(String name) {
        return store.containsKey(name);
    }

    // Get all variables (read-only)
    public Map<String, V> getAll() {
        return Collections.unmodifiableMap(store);
    }

    // Get all variable names (read-only)
    public Set<String> variableNames() {
        return Collections.unmodifiableSet(store.keySet());
    }

    // String representation of environment
    @Override
    public String toString() {
        return "Environment" + store.toString();
    }
}