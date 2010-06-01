package net.abhinavsarkar.nulllessj;

import java.util.concurrent.Callable;

/**
 * A {@link Callable} class with context.
 * 
 * @author Abhinav Sarkar <abhinav@abhinavsarkar.net>
 *
 * @param <T>   type of the context
 * @param <V>   type of the return value of the callable
 */
public abstract class ContextualCallable<T, V> implements Callable<V> {

    protected T context;

    /**
     * Set the context.
     * 
     * @param context  the context
     */
    public void setContext(final T context) {
        this.context = context;
    }
    
}
