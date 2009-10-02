package org.openstreetmap.gui.jmapviewer;

//License: GPL. Copyright 2008 by Jan Peter Stotz

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A generic class that processes a list of {@link Runnable} one-by-one using
 * one or more {@link Thread}-instances. The number of instances varies between
 * 1 and {@link #WORKER_THREAD_MAX_COUNT} (default: 8). If an instance is idle
 * more than {@link #WORKER_THREAD_TIMEOUT} seconds (default: 30), the instance
 * ends itself.
 * 
 * @author Jan Peter Stotz
 * @author Ilja Booij (Changed to using an {@link ExecutorService})
 */
public class JobDispatcher {

    private final static JobDispatcher INSTANCE = new JobDispatcher();
    
    private final BlockingQueue<Runnable> jobQueue = new LinkedBlockingQueue<Runnable>(100);
    private final ExecutorService executor;

    /**
     * @return the singleton instance of the {@link JobDispatcher}
     */
    public static JobDispatcher getInstance() {
    	return INSTANCE;
    }
     
    private JobDispatcher() {
    	executor = new ThreadPoolExecutor(1, 8, 30, TimeUnit.SECONDS, jobQueue);
    }

    /**
     * Removes all jobs from the queue that are currently not being processed.
     */
    public void cancelOutstandingJobs() {
        jobQueue.clear();
    }

    public void addJob(final Runnable job) {
    	executor.execute(job);
    }
}
