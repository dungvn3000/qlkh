/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.backup.processor;

import com.qlkh.backup.worker.Worker;
import com.qlkh.core.configuration.ConfigurationServerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * The Class BackProcessor.
 *
 * @author Nguyen Duc Dung
 * @since 6/23/12, 4:06 PM
 */
public class BackupProcessor implements Processor {

    @Autowired
    private Worker backupWorker;

    //one time a day.
    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
    @Override
    public void process() {
        if (ConfigurationServerUtil.isProductionMode()) {
            backupWorker.workForMe();
        }
    }
}