package net.abhinavsarkar.nulllessj;

public final class Just<T> extends Maybe<T> {

    private final T value;

    public Just(final T value) {
        if (value == null) {
            throw new NothingException();
        }
        this.value = value;
    }

    public static <T> Just<T> just(final T value) {
        return new Just<T>(value);
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public boolean isNothing() {
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @SuppressWarnings("unchecked")
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
        Just<T> other = (Just<T>) obj;
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return value.toString();
    }
    
}
