package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlkh.client.client.core.reader.LoadGridDataReader;
import com.qlkh.client.client.module.content.place.LimitJobPlace;
import com.qlkh.client.client.module.content.presenter.share.AbstractTaskDetailPresenter;
import com.qlkh.client.client.module.content.view.MaterialView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.core.client.action.material.LoadMaterialAction;
import com.qlkh.core.client.action.material.LoadMaterialResult;
import com.qlkh.core.client.action.task.LoadTaskHasLimitAction;
import com.qlkh.core.client.action.task.LoadTaskHasLimitResult;
import com.qlkh.core.client.model.Task;
import com.qlkh.core.client.model.TaskDetailDK;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;

/**
 * The Class LimitJobPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 3/20/13 10:10 AM
 */
@Presenter(view = MaterialView.class, place = LimitJobPlace.class)
public class MaterialPresenter extends AbstractTaskDetailPresenter<MaterialView> {

    @Override
    protected ListStore<BeanModel> createSubTaskListStore() {
        RpcProxy<LoadMaterialResult> rpcProxy = new RpcProxy<LoadMaterialResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadMaterialResult> callback) {
                long currentTaskId = -1;
                if (currentTask != null) {
                    currentTaskId = currentTask.getId();
                }
                dispatch.execute(new LoadMaterialAction((BasePagingLoadConfig) loadConfig, currentTaskId), callback);
            }
        };

        PagingLoader<PagingLoadResult<LoadMaterialResult>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<LoadMaterialResult>>(rpcProxy, new LoadGridDataReader()) {
                    @Override
                    protected void onLoadFailure(Object loadConfig, Throwable t) {
                        super.onLoadFailure(loadConfig, t);
                        //Log load exception.
                        DiaLogUtils.logAndShowRpcErrorMessage(t);
                    }
                };

        return new ListStore<BeanModel>(pagingLoader);
    }

    @Override
    protected void createTaskGrid() {
        RpcProxy<LoadTaskHasLimitResult> rpcProxy = new RpcProxy<LoadTaskHasLimitResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadTaskHasLimitResult> callback) {
                dispatch.execute(new LoadTaskHasLimitAction(view.getCbShowTaskHasLimit().getValue(),
                        view.getCbShowTaskHasNoLimit().getValue(), (BasePagingLoadConfig) loadConfig), callback);
            }
        };

        PagingLoader<PagingLoadResult<LoadTaskHasLimitResult>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<LoadTaskHasLimitResult>>(rpcProxy, new LoadGridDataReader()) {
                    @Override
                    protected void onLoadFailure(Object loadConfig, Throwable t) {
                        super.onLoadFailure(loadConfig, t);
                        //Log load exception.
                        DiaLogUtils.logAndShowRpcErrorMessage(t);
                    }
                };

        view.createTaskGrid(new ListStore<BeanModel>(pagingLoader));
        view.getTaskPagingToolBar().bind((PagingLoader<?>) view.getTaskGird().getStore().getLoader());

        resetView();

        view.getTaskGird().getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
                int index = se.getSelection().size() - 1;
                if (index >= 0) {
                    Task task = se.getSelection().get(index).getBean();
                    if (task.getId() != null) {
                        currentTask = task;
                        view.getSubTaskPagingToolBar().refresh();
                    } else {
                        emptySubGird();
                    }
                }
            }
        });
        view.getTaskGird().focus();

        createSubTaskGrid();
    }

    @Override
    protected void createSubTaskGrid() {
        view.createSubTaskGrid(createSubTaskListStore());
        view.getSubTaskPagingToolBar().bind((PagingLoader<?>) view.getSubTaskDetailGird().getStore().getLoader());
    }

}