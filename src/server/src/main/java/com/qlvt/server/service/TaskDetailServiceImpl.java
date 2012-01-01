/*
 * Copyright (C) 2011 - 2012 SMVP4G.COM
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.qlvt.server.service;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.qlvt.client.client.service.TaskDetailService;
import com.qlvt.core.client.model.TaskDetail;
import com.qlvt.server.dao.TaskDetailDao;
import com.qlvt.server.service.core.AbstractService;

import java.util.List;

/**
 * The Class TaskDetailServiceImpl.
 *
 * @author Nguyen Duc Dung
 * @since 1/1/12, 3:53 PM
 */
@Singleton
public class TaskDetailServiceImpl extends AbstractService implements TaskDetailService {

    @Inject
    private TaskDetailDao taskDetailDao;

    @Override
    public BasePagingLoadResult<List<TaskDetail>> getTaskDetailsForGrid(BasePagingLoadConfig loadConfig) {
        return new BasePagingLoadResult(taskDetailDao.getByBeanConfig(TaskDetail.class, loadConfig),
                loadConfig.getOffset(), taskDetailDao.count(TaskDetail.class));
    }

    @Override
    public void deleteTaskDetail(long taskDetailId) {
        taskDetailDao.deleteById(TaskDetail.class, taskDetailId);
    }

    @Override
    public void deleteTaskDetails(List<Long> taskDetailIds) {
        taskDetailDao.deleteByIds(TaskDetail.class, taskDetailIds);
    }
}
