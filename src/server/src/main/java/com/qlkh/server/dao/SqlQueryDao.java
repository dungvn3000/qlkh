/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao;

import com.qlkh.core.client.model.Task;
import com.qlkh.core.client.model.view.TaskDetailDKDataView;
import com.qlkh.core.client.model.view.TaskDetailKDKDataView;
import com.qlkh.core.client.model.view.TaskDetailNamDataView;
import com.qlkh.server.dao.core.Dao;

import java.util.List;

/**
 * The Class SqlQueryDao.
 *
 * @author Nguyen Duc Dung
 * @since 6/6/12, 9:58 PM
 */
public interface SqlQueryDao extends Dao {
    List<TaskDetailKDKDataView> getTaskDetailKDK(List<Long> stationIds, int year);
    List<TaskDetailDKDataView> getTaskDetailDK(List<Long> stationIds, int year);
    List<TaskDetailNamDataView> getTaskDetailNam(List<Long> stationIds, int year);
    List<Task> getTasksHasLimit();
}
