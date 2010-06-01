package net.abhinavsarkar.nulllessj;

import java.util.concurrent.Callable;

import static net.abhinavsarkar.nulllessj.Just.just;

/**
 * A container class for representing a value which 'maybe' present.
 * This is an abstract class with two concrete subclasses: {@link Just}
 * and {@link Nothing} which represent a present and non-present value
 * respectively.
 * 
 * @author Abhinav Sarkar <abhinav@abhinavsarkar.net>
 *
 * @param <T>   Type of the object being wrapped.
 */
public abstract class Maybe<T> {

    /**
     * Returns if the class is {@link Nothing}
     */
    abstract public boolean isNothing();
    
    /**
     * Returns if the class is {@link Just}
     */
    public boolean isSomething() {
        return !isNothing();
    }

    /**
     * A convenience shortcut for {@link Maybe#get()}
     */
    public T $() {
        return get();
    }

    /**
     * Returns the original wrapped value if the class is {@link Just}.
     * Throws {@link NothingException} if the class is {@link Nothing}.
     */
    abstract public T get();

    /**
     * Returns the original wrapped value if the class is {@link Just}.
     * Returns the other parameter if the class is {@link Nothing}.
     * 
     * @param other    The value to return if the class is {@link Nothing}.
     */
    public T getOrElse(final T other) {
        if (isNothing()) {
            return other;
        } else {
            return get();
        }
    }

    /**
     * Calls the callable provided if the class is {@link Nothing} and returns
     * the result of the call wrapped in {@link Maybe}.
     * If {@link NothingException} or {@link NullPointerException} is thrown
     * while calling the callable, then returns an object of type
     * {@link Nothing}.
     * If any other exception is thrown while calling the callable, then the
     * exception is caught and rethrown wrapped in a {@link RuntimeException}.
     * 
     * @param <V>      Return type of the callable provided.
     * @param callable The callable to call
     * 
     * @throws RuntimeException See the description.
     * 
     * @see    Maybe#maybe
     */
    public <V> Maybe<V> callIfNothing(final Callable<V> callable) {
        if (isNothing()) {
            try {
                return maybe(callable.call());
            } catch (NothingException e) {
                return new Nothing<V>();
            } catch (NullPointerException e) {
                return new Nothing<V>();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return new Nothing<V>();
        }
    }

    /**
     * Runs the runnable provided if the class is {@link Nothing}.
     * If {@link NothingException} or {@link NullPointerException} is thrown
     * while running the runnable, then the execution of the runnable terminates
     * silently and this method returns.
     * If any other exception is thrown while running the runnable, then the
     * exception is caught and rethrown wrapped in a {@link RuntimeException}.
     * 
     * @param runnable
     * 
     * @throws RuntimeException See the description.
     */
    public void runIfNothing(final Runnable runnable) {
        if (isNothing()) {
            try {
                runnable.run();
            } catch (NothingException e) {
                return;
            } catch (NullPointerException e) {
                return;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Calls the callable provided if the class is {@link Just} and returns
     * the result of the call wrapped in {@link Maybe}.
     * The callable is provided the wrapped value as the context.
     * If {@link NothingException} or {@link NullPointerException} is thrown
     * while calling the callable, then returns an object of type
     * {@link Nothing}.
     * If any other exception is thrown while calling the callable, then the
     * exception is caught and rethrown wrapped in a {@link RuntimeException}.
     * 
     * @param <V>      Return type of the callable provided.
     * @param callable The callable to call
     * 
     * @throws RuntimeException See the description.
     * 
     * @see    Maybe#maybe
     */
    public <V> Maybe<V> callIfSomething(
            final ContextualCallable<? super T,V> callable) {
        if (isSomething()) {
            callable.setContext(get());
            try {
                return maybe(callable.call());
            } catch (NothingException e) {
                return new Nothing<V>();
            } catch (NullPointerException e) {
                return new Nothing<V>();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return new Nothing<V>();
        }
    }

    /**
     * Calls the <code>somethingCallable</code> callable or the
     * <code>nothingCallable</code> callable if the class is {@link Just}
     * or {@link Nothing} respectively.
     * Returns the result of the call wrapped in {@link Maybe}.
     * The <code>somethingCallable</code> is provided the wrapped value as the context.
     * If {@link NothingException} or {@link NullPointerException} is thrown
     * while calling the callables, then returns an object of type
     * {@link Nothing}.
     * If any other exception is thrown while calling the callables, then the
     * exception is caught and rethrown wrapped in a {@link RuntimeException}.
     * 
     * @param <V>               Return type of the callables provided.
     * @param somethingCallable The callable to call if the class in {@link Just}
     * @param nothingCallable   The callable to call if the class in {@link Nothing}
     * 
     * @throws RuntimeException See the description.
     * 
     * @see    Maybe#maybe
     */
    public <V> Maybe<V> callIfSomethingOrElse(
            final ContextualCallable<T,V> somethingCallable,
            final Callable<V> nothingCallable) {
        if (isNothing()) {
            return callIfNothing(nothingCallable);
        } else {
            return callIfSomething(somethingCallable);
        }
    }

    /**
     * Runs the <code>runnable</code> provided if the class is {@link Just}.
     * The <code>runnable</code> is provided the wrapped value as the context.
     * If {@link NothingException} or {@link NullPointerException} is thrown
     * while running the runnable, then the execution of the runnable terminates
     * silently and this method returns.
     * If any other exception is thrown while running the runnable, then the
     * exception is caught and rethrown wrapped in a {@link RuntimeException}.
     * 
     * @param runnable  The runnable to run
     * 
     * @throws RuntimeException See the description.
     */
    public void runIfSomething(
            final ContextualRunnable<? super T> runnable) {
        if (isSomething()) {
            runnable.setContext(get());
            try {
                runnable.run();
            } catch (NothingException e) {
                return;
            } catch (NullPointerException e) {
                return;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Runs the <code>somethingRunnable</code> runnable or the
     * <code>nothingRunnable</code> runnable if the class is {@link Just} or
     * {@link Nothing} respectively.
     * The <code>somethingRunnable</code> is provided the wrapped value as the context.
     * If {@link NothingException} or {@link NullPointerException} is thrown
     * while running the runnable, then the execution of the runnable terminates
     * silently and this method returns.
     * If any other exception is thrown while running the runnable, then the
     * exception is caught and rethrown wrapped in a {@link RuntimeException}.
     * 
     * @param somethingRunnable The runnable to run if the class in {@link Just}
     * @param nothingRunnable   The runnable to run if the class in {@link Nothing}
     * 
     * @throws RuntimeException See the description.
     */
    public void runIfSomethingOrElse(
            final ContextualRunnable<T> somethingRunnable,
            final Runnable nothingRunnable) {
        if (isNothing()) {
            runIfNothing(nothingRunnable);
        } else {
            runIfSomething(somethingRunnable);
        }
    }
    
    /**
     * The kind of the {@link Maybe} value.
     */
    public static enum MaybeKind {
        SOME, NONE;
    }
    
    /**
     * Returns the kind of the {@link Maybe} value
     * @return  the kind of the {@link Maybe} value
     */
    public MaybeKind kind() {
        return isNothing() ? MaybeKind.NONE : MaybeKind.SOME;
    }
    
    /**
     * Runs the <code>runnable</code> provided.
     * If {@link NothingException} or {@link NullPointerException} is thrown
     * while running the <code>runnable</code>, then the execution of the
     * <code>runnable</code> terminates silently and this method returns.
     * If any other exception is thrown while running the <code>runnable</code>,
     * then the exception is caught and rethrown wrapped in a {@link RuntimeException}.
     * 
     * @param runnable  The runnable to run
     * 
     * @throws RuntimeException See the description.
     */
    public static void run(final Runnable runnable) {
        try {
            runnable.run();
        } catch (NothingException e) {
            return;
        } catch (NullPointerException e) {
            return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Calls the callable provided and returns the result of the call wrapped
     * in {@link Maybe}.
     * If {@link NothingException} or {@link NullPointerException} is thrown
     * while calling the callable, then returns an object of type
     * {@link Nothing}.
     * If any other exception is thrown while calling the callable, then the
     * exception is caught and rethrown wrapped in a {@link RuntimeException}.
     * 
     * @param <V>      Return type of the callable provided.
     * @param callable The callable to call
     * 
     * @throws RuntimeException See the description.
     * 
     * @see    Maybe#maybe
     */
    public static <V> Maybe<V> call(final Callable<V> callable) {
        try {
            return maybe(callable.call());
        } catch (NothingException e) {
            return new Nothing<V>();
        } catch (NullPointerException e) {
            return new Nothing<V>();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Wraps an Object in {@link Maybe} and returns the wrapped Object.
     * If obj is null then returns an instance of {@link Nothing}.
     * Otherwise returns an instance of {@link Just} wrapped over obj.
     * 
     * @param <V>  The type of the object to wrap
     * @param obj  The object to wrap.
     */
    public static <V> Maybe<V> maybe(final V obj) {
        if (obj == null) {
            return new Nothing<V>();
        } else {
            return just(obj);
        }
    }
    
}
