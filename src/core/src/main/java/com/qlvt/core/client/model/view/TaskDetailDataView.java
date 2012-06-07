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

package com.qlvt.core.client.model.view;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigInteger;

/**
 * The Class TaskDetailView.
 *
 * @author Nguyen Duc Dung
 * @since 6/7/12, 8:59 AM
 */
public class TaskDetailDataView implements IsSerializable {
    private long taskId;
    private long taskDetailId;
    private long stationId;
    private boolean annual;
    private int year;

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(BigInteger taskId) {
        this.taskId = taskId.longValue();
    }

    public long getTaskDetailId() {
        return taskDetailId;
    }

    public void setTaskDetailId(BigInteger taskDetailId) {
        this.taskDetailId = taskDetailId.longValue();
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(BigInteger stationId) {
        this.stationId = stationId.longValue();
    }

    public boolean isAnnual() {
        return annual;
    }

    public void setAnnual(boolean annual) {
        this.annual = annual;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
