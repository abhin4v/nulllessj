package net.abhinavsarkar.nulllessj;

public final class Nothing<T> extends Maybe<T> {

    public Nothing() {}

    @Override
    public T get() {
        throw new NothingException();
    }
    
    @Override
    public boolean isNothing() {
        return true;
    }
    
    @Override
    public int hashCode() {
        return -999999;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "<Nothing>";
    }
    
}
