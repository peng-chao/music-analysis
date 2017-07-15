package me.vcode.music.pool;

import java.util.List;
import java.util.concurrent.*;

/**
 * Created by peng_chao_b on 7/7/17.
 */
public final class BoundedBlockingPool<T> extends AbstractPool<T> implements BlockingPool<T> {

    private BlockingQueue<T> objects;
    private Validator<T> validator;
    private ObjectFactory<T> objectFactory;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private ScheduledExecutorService checkoutObjects = Executors.newScheduledThreadPool(1);
    private volatile boolean shutdownCalled;

    public BoundedBlockingPool(
            Validator validator,
            ObjectFactory objectFactory) {
        super();
        this.objectFactory = objectFactory;
        this.validator = validator;
        objects = new LinkedBlockingQueue();
        initializeObjects();
        checkoutObjects.scheduleAtFixedRate(runnable, 10, 10, TimeUnit.MINUTES);
        shutdownCalled = false;
    }


    private Runnable runnable = () -> {
        try {
            initializeObjects();
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    public T get(long timeOut, TimeUnit unit) {
        if (!shutdownCalled) {
            T t = null;
            try {
                t = objects.poll(timeOut, unit);
                return t;
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            return t;
        }
        throw new IllegalStateException("Object pool is already shutdown");
    }

    public T get() {
        if (!shutdownCalled) {
            T t = null;
            try {
                t = objects.take();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            return t;
        }

        throw new IllegalStateException("Object pool is already shutdown");
    }

    public void shutdown() {
        shutdownCalled = true;
        executor.shutdownNow();
        clearResources();
    }

    private void clearResources() {
        for (T t : objects) {
            validator.invalidate(t);
        }
    }

    @Override
    protected void returnToPool(T t) {
        if (validator.isValid(t)) {
            executor.submit(new ObjectReturner(objects, t));
        }
    }

    @Override
    protected void handleInvalidReturn(T t) {
    }

    @Override
    protected boolean isValid(T t) {
        return validator.isValid(t);
    }

    private void initializeObjects() {
        List<T> t = objectFactory.createNew();
        for (T t1 : t) {
            try {
                objects.put(t1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private class ObjectReturner<E> implements Callable<E> {
        private BlockingQueue<E> queue;
        private E e;

        public ObjectReturner(BlockingQueue<E> queue, E e) {
            this.queue = queue;
            this.e = e;
        }

        public E call() {
            while (true) {
                try {
                    queue.put(e);
                    break;
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
            return null;
        }

    }
}
