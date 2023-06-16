/* Garret Duo
AP Computer Science A
Period 4
Compiler Project */

import java.util.*;

class Storage {
    private final Storage enclosing; // handles local vars
    private final Map<String, Object> vars = new HashMap<String, Object>(); // HashMaps are useful

    public Storage() { // global vars
        enclosing = null;
    }

    public Storage(Storage enclosing) { // local vars
        this.enclosing = enclosing;
    }

    public void define(String name, Object val) { // defines a var based on its key (var name)
        vars.put(name, val);
    }

    public Object grab(String name, int line) {
        if (vars.containsKey(name)) {
            return vars.get(name);
        }
        else if (enclosing != null) { // search further up in scope
            return enclosing.grab(name, line); 
        }
        else {
            throw new RuntimeError(name, "Undefined variable '" + name + "'.", line); // error checking
        }
    }

    @SuppressWarnings("unchecked") // because Java likes to be safe but I don't
    public Object grabArr(String name, int index, int line) {
        if (vars.containsKey(name)) {
            ArrayList<Object> list = (ArrayList<Object>) vars.get(name);
            try {
                return list.get(index); // returns array value at specified index
            } catch (IndexOutOfBoundsException ex) {
                throw new RuntimeError(name, "Index " + index + " out of bounds for array '" + name + "' with length " + list.size() + ".", line); // error checking
            }
        }
        else if (enclosing != null) { // search further up in scope
            return enclosing.grabArr(name, index, line);
        }
        else {
            throw new RuntimeError(name, "Undefined array '" + name + "'.", line); // error checking
        }
    }

    public void assignVar(String name, Object val, int line) {
        if (vars.containsKey(name)) { // if already contains var name just replace
            vars.put(name, val);
        }
        else if (enclosing != null) { // search further up in scope
            enclosing.assignVar(name, val, line);
        }
        else {
            throw new RuntimeError(name, "Undefined variable '" + name + "'.", line); // error checking
        }
    }

    @SuppressWarnings("unchecked") // because Java likes to be safe but I don't
    public void assignArr(String name, Object val, int index, int line) {
        if (vars.containsKey(name)) {
            ArrayList<Object> list = (ArrayList<Object>) vars.get(name); // easy access to list
            try {
                int pointer = list.size();
                while (pointer <= index) { // while past initial size, keep adding zeroes
                    list.add(0);
                    pointer++;
                }
                list.set(index, val); // once specified index to set val to reached, set val there
            } catch (IndexOutOfBoundsException ex) {
                throw new RuntimeError(name, "Index " + index + " out of bounds for array '" + name + "' with length " + list.size() + ".", line); // error checking
            }
        }
        else if (enclosing != null) { // search further up in scope
            enclosing.assignArr(name, val, index, line);
        }
        else {
            throw new RuntimeError(name, "Undefined array '" + name + "'.", line); // error checking
        }
    }
}