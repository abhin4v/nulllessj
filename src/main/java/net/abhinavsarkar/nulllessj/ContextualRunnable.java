package net.abhinavsarkar.nulllessj;

/**
 * A {@link Runnable} class with context.
 * 
 * @author Abhinav Sarkar <abhinav@abhinavsarkar.net>
 *
 * @param <T>   type of the context
 */
public abstract class ContextualRunnable<T> implements Runnable {

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
