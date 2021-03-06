/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao.impl;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.constant.QuarterEnum;
import com.qlkh.core.client.constant.TaskTypeEnum;
import com.qlkh.core.client.model.Material;
import com.qlkh.core.client.model.Task;
import com.qlkh.core.client.model.view.TaskDetailDKDataView;
import com.qlkh.core.client.model.view.TaskDetailKDKDataView;
import com.qlkh.core.client.model.view.TaskDetailNamDataView;
import com.qlkh.core.client.model.view.TaskMaterialDataView;
import com.qlkh.core.client.report.MaterialReportBean;
import com.qlkh.server.dao.SqlQueryDao;
import com.qlkh.server.dao.core.AbstractDao;
import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * The Class SqlQueryDaoImpl.
 *
 * @author Nguyen Duc Dung
 * @since 6/6/12, 9:59 PM
 */
public class SqlQueryDaoImpl extends AbstractDao implements SqlQueryDao {

    @Override
    public List<TaskDetailNamDataView> getTaskDetailNam(final List<Long> stationIds, final int year) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<TaskDetailNamDataView>>() {
            @Override
            public List<TaskDetailNamDataView> doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM taskdetail_nam_view " +
                        "WHERE year = :year AND stationId IN (:stationIds)");
                sqlQuery.setParameter("year", year);
                sqlQuery.setParameterList("stationIds", stationIds);
                sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(TaskDetailNamDataView.class));
                return sqlQuery.list();
            }
        });
    }

    @Override
    public List<TaskDetailDKDataView> getTaskDetailDK(final List<Long> stationIds, final int year) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<TaskDetailDKDataView>>() {
            @Override
            public List<TaskDetailDKDataView> doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM taskdetail_dk_view " +
                        "WHERE year = :year AND stationId IN (:stationIds)");
                sqlQuery.setParameter("year", year);
                sqlQuery.setParameterList("stationIds", stationIds);
                sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(TaskDetailDKDataView.class));
                return sqlQuery.list();
            }
        });
    }

    @Override
    public List<TaskDetailKDKDataView> getTaskDetailKDK(final List<Long> stationIds, final int year) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<TaskDetailKDKDataView>>() {
            @Override
            public List<TaskDetailKDKDataView> doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery("SELECT * FROM taskdetail_kdk_view " +
                        "WHERE year = :year AND stationId IN (:stationIds)");
                sqlQuery.setParameter("year", year);
                sqlQuery.setParameterList("stationIds", stationIds);
                sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(TaskDetailKDKDataView.class));
                return sqlQuery.list();
            }
        });
    }

    @Override
    public List<TaskMaterialDataView> getTaskMaterial(final int year, final int quarter) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<TaskMaterialDataView>>() {
            @Override
            public List<TaskMaterialDataView> doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery("SELECT  * FROM `task_material` " +
                        "WHERE year = :year AND quarter = :quarter");
                sqlQuery.setParameter("year", year);
                sqlQuery.setParameter("quarter", quarter);
                sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(TaskMaterialDataView.class));
                return sqlQuery.list();
            }
        });
    }

    @Override
    public List<TaskMaterialDataView> getTaskMaterial(final int year) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<TaskMaterialDataView>>() {
            @Override
            public List<TaskMaterialDataView> doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery("SELECT  * FROM `task_material` " +
                        "WHERE year = :year");
                sqlQuery.setParameter("year", year);
                sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(TaskMaterialDataView.class));
                return sqlQuery.list();
            }
        });
    }

    @Override
    public List<TaskMaterialDataView> getTaskMaterialByMaterialId(final long materialId, final int year, final int quarter) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<TaskMaterialDataView>>() {
            @Override
            public List<TaskMaterialDataView> doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery("SELECT  * FROM `task_material` " +
                        "WHERE year = :year AND quarter = :quarter AND materialId = :materialId");
                sqlQuery.setParameter("year", year);
                sqlQuery.setParameter("quarter", quarter);
                sqlQuery.setParameter("materialId", materialId);
                sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(TaskMaterialDataView.class));
                return sqlQuery.list();
            }
        });
    }


    @Override
    public List<Material> getMaterialPrices(final int year, final int quarter) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<Material>>() {
            @Override
            public List<Material> doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery("SELECT  `material`.`id` AS `bigId`, `name`, `code`, `material_price`.`price` AS `currentPriceValue` FROM `material` " +
                        "INNER JOIN `material_price` WHERE `material_price`.`materialId` = `material`.`id` AND `material_price`.`year` = :year AND `material_price`.`quarter` = :quarter");
                sqlQuery.setParameter("year", year);
                sqlQuery.setParameter("quarter", quarter);
                sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(Material.class));
                return sqlQuery.list();
            }
        });
    }

    @Override
    public List<Material> getMaterialsMissingPrice(final int year, final int quarter) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<Material>>() {
            @Override
            public List<Material> doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery sqlQuery = session.createSQLQuery("SELECT  `id` AS `bigId`, `name`, `code` FROM `material` WHERE `material`.`id` NOT IN " +
                        "(SELECT `material_price`.`materialId` FROM `material_price` " +
                        "WHERE `material_price`.`year` = :year AND `material_price`.`quarter` = :quarter) AND `material`.`id` IN " +
                        "(SELECT `material_limit`.`materialId` FROM `material_limit`)");
                sqlQuery.setParameter("year", year);
                sqlQuery.setParameter("quarter", quarter);
                sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(Material.class));
                return sqlQuery.list();
            }
        });
    }

    @Override
    public BasePagingLoadResult<Task> getTasks(final boolean hasLimit, final boolean hasNoLimit, final BasePagingLoadConfig config) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<BasePagingLoadResult<Task>>() {
            @Override
            public BasePagingLoadResult<Task> doInHibernate(Session session) throws HibernateException, SQLException {
                String select = "SELECT `task`.`id` AS `bigId`," +
                        "`task`.`name` ," +
                        "`task`.`code`, " +
                        "`task`.`defaultValue`," +
                        "`task`.`unit`," +
                        "`task`.`quota`," +
                        "`task`.`dynamicQuota`," +
                        "`task`.`taskTypeCode`," +
                        "`task`.`childTasks`" +
                        "FROM  `task` ";

                String count = "SELECT COUNT(*) FROM `task` ";

                String sql = "";

                String materialCode = "";
                //hard code
                if (config.get("filters") != null) {
                    Map<String, Object> filters = config.get("filters");
                    materialCode = (String) filters.get("material.code");
                    filters.remove("material.code");
                }

                if (hasLimit && !hasNoLimit) {
                    sql += "WHERE `task`.`id` IN (" +
                            "SELECT `taskid` FROM `material_limit` " +
                            "INNER JOIN `material` " +
                            "ON `material`.`id` = `material_limit`.`materialId` ";

                    if (StringUtils.isNotBlank(materialCode)) {
                        sql += "AND `material`.`code` = '" + materialCode + "' )";
                    } else {
                        sql += ")";
                    }
                } else if (hasNoLimit && !hasLimit) {
                    sql += "WHERE `task`.`id` NOT IN (SELECT `taskid` FROM `material_limit`) ";
                }

                if (!sql.contains("WHERE")) {
                    sql += "WHERE ";
                } else {
                    sql += "AND ";
                }

                sql += " `task`.`taskTypeCode` != " + TaskTypeEnum.SUBSUM.getCode() +
                        " AND `task`.`taskTypeCode` != " + TaskTypeEnum.SUM.getCode();

                sql = createFilter(config, "task", sql);

                SQLQuery selectQuery = session.createSQLQuery(select += sql);
                selectQuery.setResultTransformer(new AliasToBeanResultTransformer(Task.class));
                selectQuery.setMaxResults(config.getLimit());
                selectQuery.setFirstResult(config.getOffset());

                SQLQuery countQuery = session.createSQLQuery(count += sql);

                int total = Integer.valueOf(countQuery.list().get(0).toString());

                return new BasePagingLoadResult<Task>(selectQuery.list(), config.getOffset(), total);
            }
        });
    }

    @Override
    public BasePagingLoadResult<Material> getMaterials(final int year, final QuarterEnum quarter, final BasePagingLoadConfig config) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<BasePagingLoadResult<Material>>() {
            @Override
            public BasePagingLoadResult<Material> doInHibernate(Session session) throws HibernateException, SQLException {
                String select = "SELECT `id` AS `bigId`," +
                        "`name` ," +
                        "`code` ," +
                        "`unit` ," +
                        "`note` " +
                        "FROM  `material` ";

                String count = "SELECT COUNT(*) FROM `material` ";

                String sql = "WHERE `id` NOT IN (SELECT `materialId` FROM `material_price` WHERE `year` = " + year + " AND `quarter` = " + quarter.getCode() + ")";
                sql = createFilter(config, "material", sql);

                SQLQuery selectQuery = session.createSQLQuery(select += sql);
                selectQuery.setResultTransformer(new AliasToBeanResultTransformer(Material.class));
                selectQuery.setMaxResults(config.getLimit());
                selectQuery.setFirstResult(config.getOffset());

                SQLQuery countQuery = session.createSQLQuery(count += sql);

                int total = Integer.valueOf(countQuery.list().get(0).toString());

                return new BasePagingLoadResult<Material>(selectQuery.list(), config.getOffset(), total);
            }
        });
    }

    @Override
    public BasePagingLoadResult<Material> getMaterialTasks(final int year, final int quarter, final BasePagingLoadConfig config) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<BasePagingLoadResult<Material>>() {
            @Override
            public BasePagingLoadResult<Material> doInHibernate(Session session) throws HibernateException, SQLException {
                String select = "SELECT `id` AS `bigId`," +
                        "`name` ," +
                        "`code` ," +
                        "`unit` ," +
                        "`note` " +
                        "FROM  `material` ";

                String count = "SELECT COUNT(*) FROM `material` ";

                String sql = "WHERE `id` IN (SELECT `materialId` FROM `task_material` WHERE `year` = " + year + " AND `quarter` = " + quarter + ")";
                sql = createFilter(config, "material", sql);

                SQLQuery selectQuery = session.createSQLQuery(select += sql);
                selectQuery.setResultTransformer(new AliasToBeanResultTransformer(Material.class));
                selectQuery.setMaxResults(config.getLimit());
                selectQuery.setFirstResult(config.getOffset());

                SQLQuery countQuery = session.createSQLQuery(count += sql);

                int total = Integer.valueOf(countQuery.list().get(0).toString());

                return new BasePagingLoadResult<Material>(selectQuery.list(), config.getOffset(), total);
            }
        });
    }

    @Override
    public BasePagingLoadResult<Material> getMaterials(final long taskId, final BasePagingLoadConfig config) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<BasePagingLoadResult<Material>>() {
            @Override
            public BasePagingLoadResult<Material> doInHibernate(Session session) throws HibernateException, SQLException {
                String select = "SELECT `id` AS `bigId`," +
                        "`name` ," +
                        "`code` ," +
                        "`unit` ," +
                        "`note` " +
                        "FROM  `material` ";

                String count = "SELECT COUNT(*) FROM `material` ";

                String sql = "WHERE `id` NOT IN (SELECT `materialId` FROM `material_limit` WHERE `taskId` = " + taskId + ")";
                sql = createFilter(config, "material", sql);

                SQLQuery selectQuery = session.createSQLQuery(select += sql);
                selectQuery.setResultTransformer(new AliasToBeanResultTransformer(Material.class));
                selectQuery.setMaxResults(config.getLimit());
                selectQuery.setFirstResult(config.getOffset());

                SQLQuery countQuery = session.createSQLQuery(count += sql);

                int total = Integer.valueOf(countQuery.list().get(0).toString());

                return new BasePagingLoadResult<Material>(selectQuery.list(), config.getOffset(), total);
            }
        });
    }

    @Override
    public List<MaterialReportBean> getMaterialOut(final int form, final int to, final int year) {
        return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<MaterialReportBean>>() {
            @Override
            public List<MaterialReportBean> doInHibernate(Session session) throws HibernateException, SQLException {
                String select = "SELECT `material`.`name`, " +
                        "`material`.`code`, " +
                        "`material`.`unit`, " +
//                        "`material_price`.`price`, " +
                        "`material_in`.`weight`, " +
                        "`material_in`.`total`, " +
                        "`material_in`.`code` AS `reportCode`, " +
                        "`station`.`name` AS `stationName`, " +
                        "`group`.`name` AS `groupName`,  " +
                        "`material_group`.`name` AS `reason`,  " +
                        "`material_person`.`personName`,  " +
                        "`material_in`.`id` AS `materialId`, " +
                        "`material_in`.`exportDate` " +
                        "FROM `material_in` " +
                        "INNER JOIN `material` " +
                        "ON `material_in`.`materialId` = `material`.`id` " +
                        "INNER JOIN `material_person` " +
                        "ON `material_in`.`materialPersonId` = `material_person`.`id` " +
                        "INNER JOIN `material_group` " +
                        "ON `material_in`.`materialGroupId` = `material_group`.`id` " +
                        "LEFT OUTER JOIN `station` " +
                        "ON `material_in`.`stationId` = `station`.`id` " +
                        "LEFT OUTER JOIN `group` " +
                        "ON `material_in`.`groupId` = `group`.`id` " +
//                        "INNER JOIN `material_price` " +
//                        "ON `material_in`.`materialId` = `material_price`.`materialId` " +
//                        "AND `material_in`.`year` = `material_price`.`year` " +
//                        "AND `material_in`.`quarter` = `material_price`.`quarter` " +
                        "WHERE `material_in`.`code` >= " + form + " AND `material_in`.`code` <= " + to +
                        " AND `material_in`.`year` = " + year;
                SQLQuery selectQuery = session.createSQLQuery(select);
                selectQuery.setResultTransformer(new AliasToBeanResultTransformer(MaterialReportBean.class));
                return selectQuery.list();
            }
        });
    }

    private String createFilter(BasePagingLoadConfig config, String tableName, String query) {
        String sql = query;
        if (config.get("hasFilter") != null && config.<Boolean>get("hasFilter")) {
            Map<String, Object> filters = config.get("filters");
            if (filters != null) {
                if (!sql.contains("WHERE")) {
                    sql += " WHERE ";
                } else {
                    sql += " AND ";
                }
                sql += "(";
                for (String filter : filters.keySet()) {
                    sql += "`" + tableName + "`.`" + filter + "` LIKE '%" + filters.get(filter) + "%' OR ";
                }
                sql = sql.substring(0, sql.length() - 3) + ")";
            }
        }
        return sql;
    }
}
