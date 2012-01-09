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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.qlvt.client.client.service.StationService;
import com.qlvt.core.client.exception.DeleteException;
import com.qlvt.core.client.model.Branch;
import com.qlvt.core.client.model.Station;
import com.qlvt.core.client.model.User;
import com.qlvt.server.dao.BranchDao;
import com.qlvt.server.dao.StationDao;
import com.qlvt.server.dao.UserDao;
import com.qlvt.server.service.core.AbstractService;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * The Class StationServiceImpl.
 *
 * @author Nguyen Duc Dung
 * @since 12/29/11, 1:36 PM
 */
@Singleton
public class StationServiceImpl extends AbstractService implements StationService {

    @Inject
    private StationDao stationDao;

    @Inject
    private UserDao userDao;

    @Inject
    private BranchDao branchDao;

    @Override
    public List<Station> getAllStation() {
        return stationDao.getAll(Station.class);
    }

    @Override
    public PagingLoadResult<List<Station>> getStationsForGrid(PagingLoadConfig config) {
        List<Station> stations = stationDao.getByBeanConfig(Station.class, config);
        return new PagingLoadResultBean(stations, config.getOffset(), stationDao.count(Station.class));
    }

    @Override
    public void updateStations(List<Station> stations) {
        stationDao.saveOrUpdate(stations);
    }

    @Override
    public void deleteStationById(long stationId) {
        if (CollectionUtils.isEmpty(userDao.findByStationId(stationId))) {
            stationDao.deleteById(Station.class, stationId);
        } else {
            throw new DeleteException();
        }
    }

    @Override
    public void deleteStationByIds(List<Long> stationIds) {
        if (CollectionUtils.isEmpty(userDao.findByStationIds(stationIds))) {
            stationDao.deleteByIds(Station.class, stationIds);
        } else {
            throw new DeleteException();
        }
    }

    @Override
    public Station getStationAndBranchByUserName(String userName) {
        User user = userDao.findByUserName(userName);
        if (user != null) {
            Station station = user.getStation();
            List<Branch> branches = branchDao.getBranchsByStationId(station.getId());
            station.setBranches(branches);
            return station;
        }
        return null;
    }
}
