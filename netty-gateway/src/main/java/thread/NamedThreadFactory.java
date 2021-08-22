package thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
    private String prefix;
    private AtomicInteger threadNum;

    public NamedThreadFactory(String prefix) {
        this.prefix = prefix;
        this.threadNum = new AtomicInteger(0);
    }
    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setName(prefix + "-" + threadNum.getAndIncrement());
        t.setDaemon(true);
        return t;
    }
}
