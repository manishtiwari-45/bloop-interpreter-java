package runtime;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

// Stores variables (name → value)
public class Environment implements VariableStore {

    // Internal storage
    private final Map<String, Object> store = new HashMap<>();

    // Add or update variable
    // @Override
    public void set(String name, Object value) { store.put(name, value); }
    

    // Get variable value (may be empty)
    // @Override
    public Optional<Object> get(String name) {
         return Optional.ofNullable(store.get(name));
    }

    // Check if variable exists
    // @Override
    public boolean has(String name) { return store.containsKey(name); }

    // Get all variables (read-only)
    public Map<String, Object> getAll() {
        return Collections.unmodifiableMap(store);
    }
    

    // Get all variable names (read-only)
    public Set<String> variableNames() {
        return Collections.unmodifiableSet(store.keySet());
    }

    // String representation of environment
    @Override
    public String toString() { return "Environment" + store; }
}