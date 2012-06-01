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
import com.qlvt.core.client.action.subtaskannualdetail.LoadSubTaskAnnualAction;
import com.qlvt.core.client.action.subtaskannualdetail.LoadSubTaskAnnualResult;
import com.qlvt.core.client.model.Branch;
import com.qlvt.core.client.model.SubTaskAnnualDetail;
import com.qlvt.core.client.model.TaskDetail;
import com.qlvt.server.dao.BranchDao;
import com.qlvt.server.dao.SubTaskAnnualDetailDao;
import com.qlvt.server.dao.core.GeneralDao;
import com.qlvt.server.service.core.AbstractHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class LoadSubTaskAnnualHandler.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 9:10 PM
 */
public class LoadSubTaskAnnualHandler extends AbstractHandler<LoadSubTaskAnnualAction, LoadSubTaskAnnualResult> {

    @Autowired
    private GeneralDao generalDao;

    @Autowired
    private BranchDao branchDao;

    @Autowired
    private SubTaskAnnualDetailDao subTaskAnnualDetailDao;

    @Override
    public Class<LoadSubTaskAnnualAction> getActionType() {
        return LoadSubTaskAnnualAction.class;
    }

    @Override
    public LoadSubTaskAnnualResult execute(LoadSubTaskAnnualAction action, ExecutionContext context) throws DispatchException {
        return new LoadSubTaskAnnualResult(getSubTaskAnnualDetails(action.getLoadConfig(), action.getTaskDetailId()));
    }

    public BasePagingLoadResult<SubTaskAnnualDetail> getSubTaskAnnualDetails(BasePagingLoadConfig loadConfig,
                                                                             long taskDetailId) {
        List<SubTaskAnnualDetail> subTaskAnnualDetails = new ArrayList<SubTaskAnnualDetail>();
        TaskDetail taskDetail = generalDao.findById(TaskDetail.class, taskDetailId);
        if (taskDetail != null) {
            List<Branch> branches = branchDao.findByStationId(taskDetail.getStation().getId());
            if (CollectionUtils.isNotEmpty(branches)) {
                for (Branch branch : branches) {
                    SubTaskAnnualDetail subTaskAnnualDetail = subTaskAnnualDetailDao.
                            findByTaskDetaiIdAndBranchId(taskDetail.getId(), branch.getId());
                    if (subTaskAnnualDetail == null) {
                        subTaskAnnualDetail = new SubTaskAnnualDetail();
                        subTaskAnnualDetail.setTaskDetail(taskDetail);
                        subTaskAnnualDetail.setBranch(branch);
                        subTaskAnnualDetail.setCreateBy(1l);
                        subTaskAnnualDetail.setUpdateBy(1l);
                    }
                    subTaskAnnualDetails.add(subTaskAnnualDetail);
                }
            }
        }
        return new BasePagingLoadResult<SubTaskAnnualDetail>(subTaskAnnualDetails, loadConfig.getOffset(),
                subTaskAnnualDetails.size());
    }
}
