/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.processor;

import com.qlkh.backup.processor.Processor;
import com.qlkh.backup.worker.Worker;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * The Class CopyTaskDataProcessor.
 *
 * @author Nguyen Duc Dung
 * @since 12/3/13 9:09 AM
 */
public class CopyTaskDataProcessor implements Processor {

    private Worker copyTaskDataWorker;

    //one time a day.
    @Scheduled(fixedDelay = 1000 * 2)
    @Override
    public void process() {
        System.out.println("Hello");
    }

    public void setCopyTaskDataWorker(Worker copyTaskDataWorker) {
        this.copyTaskDataWorker = copyTaskDataWorker;
    }
}