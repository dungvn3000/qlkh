/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelFactory;
import com.extjs.gxt.ui.client.data.BeanModelLookup;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlkh.client.client.core.dispatch.StandardDispatchAsync;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.StationManagerPlace;
import com.qlkh.client.client.module.content.view.StationManagerView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.core.client.action.core.DeleteAction;
import com.qlkh.core.client.action.core.DeleteResult;
import com.qlkh.core.client.action.core.SaveAction;
import com.qlkh.core.client.action.core.SaveResult;
import com.qlkh.core.client.model.*;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.CollectionsUtils;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.client.DispatchAsync;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class StationManagerPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 12/29/11, 7:00 AM
 */
@Presenter(view = StationManagerView.class, place = StationManagerPlace.class)
public class StationManagerPresenter extends AbstractPresenter<StationManagerView> {

    private static final String[] RELATE_ENTITY_NAMES = {User.class.getName(), Branch.class.getName(),
            TaskDetailDK.class.getName(), TaskDetailKDK.class.getName(), TaskDetailNam.class.getName()};

    private DispatchAsync dispatch = StandardDispatchAsync.INSTANCE;

    private Station currentStation;
    private Window stationEditWindow;

    @Override
    public void onActivate() {
        view.show();
        view.getPagingToolBar().refresh();
        view.getStationsGird().focus();
    }

    @Override
    protected void doBind() {
        view.createGrid(GridUtils.createListStore(Station.class));
        view.getPagingToolBar().bind((PagingLoader<?>) view.getStationsGird().getStore().getLoader());
        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                stationEditWindow = view.createStationEditWindow();
                currentStation = new Station();
                stationEditWindow.show();
            }
        });
        view.getBtnEdit().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (view.getStationsGird().getSelectionModel().getSelectedItem() != null) {
                    currentStation = view.getStationsGird().getSelectionModel().getSelectedItem().getBean();
                    view.getTxtStationName().setValue(currentStation.getName());
                    stationEditWindow = view.createStationEditWindow();
                    stationEditWindow.show();
                }
            }
        });
        view.getBtnRefresh().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                view.getPagingToolBar().refresh();
            }
        });
        view.getBtnDelete().addSelectionListener(new DeleteButtonEventListener());
        view.getBtnStationEditOk().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (currentStation != null && view.getStationEditPanel().isValid()) {
                    currentStation.setName(view.getTxtStationName().getValue());
                    currentStation.setCreateBy(1l);
                    currentStation.setUpdateBy(1l);
                    dispatch.execute(new SaveAction(currentStation), new AbstractAsyncCallback<SaveResult>() {
                        @Override
                        public void onSuccess(SaveResult result) {
                            DiaLogUtils.notify(view.getConstant().saveMessageSuccess());
                            stationEditWindow.hide();
                            updateGrid(result.<Station>getEntity());
                        }
                    });
                }
            }
        });
        view.getBtnStationEditCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                stationEditWindow.hide();
            }
        });
    }

    private void updateGrid(Station station) {
        boolean isNotFound = true;
        BeanModelFactory factory = BeanModelLookup.get().getFactory(Station.class);
        BeanModel updateModel = factory.createModel(station);
        for (BeanModel model : view.getStationsGird().getStore().getModels()) {
            if (station.getId().equals(model.<Station>getBean().getId())) {
                int index = view.getStationsGird().getStore().indexOf(model);
                view.getStationsGird().getStore().remove(model);
                view.getStationsGird().getStore().insert(updateModel, index);
                isNotFound = false;
            }
        }
        if (isNotFound) {
            view.getStationsGird().getStore().add(updateModel);
            view.getStationsGird().getView().ensureVisible(view.getStationsGird()
                    .getStore().getCount() - 1, 1, false);
        }
        view.getStationsGird().getSelectionModel().select(updateModel, false);
    }

    private class DeleteButtonEventListener extends SelectionListener<ButtonEvent> {
        @Override
        public void componentSelected(ButtonEvent ce) {
            final List<BeanModel> models = view.getStationsGird().getSelectionModel().getSelectedItems();
            if (CollectionsUtils.isNotEmpty(models)) {
                if (models.size() == 1) {
                    final Station station = (Station) models.get(0).getBean();
                    showDeleteTagConform(station.getId(), station.getName());
                } else {
                    List<Long> stationIds = new ArrayList<Long>(models.size());
                    for (BeanModel model : models) {
                        final Station station = (Station) model.getBean();
                        stationIds.add(station.getId());
                    }
                    showDeleteTagConform(stationIds, null);
                }
            }
        }
    }

    private void showDeleteTagConform(long stationId, String tagName) {
        List<Long> stationIds = new ArrayList<Long>(1);
        stationIds.add(stationId);
        showDeleteTagConform(stationIds, tagName);
    }

    private void showDeleteTagConform(final List<Long> stationIds, String tagName) {
        assert stationIds != null;
        String deleteMessage;
        final AsyncCallback<DeleteResult> callback = new AbstractAsyncCallback<DeleteResult>() {
            @Override
            public void onSuccess(DeleteResult result) {
                if (result.isDelete()) {
                    //Reload grid.
                    view.getPagingToolBar().refresh();
                    DiaLogUtils.notify(view.getConstant().deleteStationMessageSuccess());
                } else {
                    DiaLogUtils.showMessage(view.getConstant().deleteErrorMessage());
                }
            }
        };
        final boolean hasManyTag = stationIds.size() > 1;
        if (hasManyTag) {
            deleteMessage = view.getConstant().deleteAllStationMessage();
        } else {
            deleteMessage = StringUtils.substitute(view.getConstant().deleteStationMessage(), tagName);
        }

        DiaLogUtils.conform(deleteMessage, new Listener<MessageBoxEvent>() {
            @Override
            public void handleEvent(MessageBoxEvent be) {
                if (be.getButtonClicked().getText().equals("Yes")) {
                    if (hasManyTag) {
                        dispatch.execute(new DeleteAction(Station.class.getName(), stationIds,
                                RELATE_ENTITY_NAMES), callback);
                    } else {
                        dispatch.execute(new DeleteAction(Station.class.getName(), stationIds.get(0),
                                RELATE_ENTITY_NAMES), callback);
                    }
                }
            }
        });
    }

    @Override
    public String mayStop() {
        if (stationEditWindow != null && stationEditWindow.isVisible()) {
            return view.getConstant().conformExitMessage();
        }
        return null;
    }

    @Override
    public void onCancel() {
        if (stationEditWindow != null && stationEditWindow.isVisible()) {
            stationEditWindow.hide();
        }
    }
}
