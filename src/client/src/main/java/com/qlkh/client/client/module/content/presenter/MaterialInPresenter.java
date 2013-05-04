package com.qlkh.client.client.module.content.presenter;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.qlkh.client.client.constant.ExceptionConstant;
import com.qlkh.client.client.core.dispatch.StandardDispatchAsync;
import com.qlkh.client.client.core.reader.LoadGridDataReader;
import com.qlkh.client.client.core.rpc.AbstractAsyncCallback;
import com.qlkh.client.client.module.content.place.MaterialInPlace;
import com.qlkh.client.client.module.content.view.MaterialInView;
import com.qlkh.client.client.utils.DiaLogUtils;
import com.qlkh.client.client.utils.GridUtils;
import com.qlkh.core.client.action.core.*;
import com.qlkh.core.client.action.grid.LoadGridDataAction;
import com.qlkh.core.client.action.grid.LoadGridDataResult;
import com.qlkh.core.client.action.material.*;
import com.qlkh.core.client.action.time.GetServerTimeAction;
import com.qlkh.core.client.action.time.GetServerTimeResult;
import com.qlkh.core.client.constant.QuarterEnum;
import com.qlkh.core.client.criterion.ClientRestrictions;
import com.qlkh.core.client.model.*;
import com.smvp4g.mvp.client.core.presenter.AbstractPresenter;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import net.customware.gwt.dispatch.client.DispatchAsync;
import net.customware.gwt.dispatch.shared.ServiceException;

import java.util.*;

/**
 * The Class MaterialInPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 4/23/13 4:08 AM
 */
@Presenter(place = MaterialInPlace.class, view = MaterialInView.class)
public class MaterialInPresenter extends AbstractPresenter<MaterialInView> {

    private DispatchAsync dispatch = StandardDispatchAsync.INSTANCE;

    private Window editWindow;
    private MaterialIn currentMaterial;
    private QuarterEnum currentQuarter;
    private int currentYear;
    private Station currentStation;

    @Override
    public void onActivate() {
        view.show();
        if (currentQuarter != null && currentYear > 0) {
            view.getPagingToolBar().refresh();
        }
    }

    @Override
    protected void doBind() {
        dispatch.execute(new GetServerTimeAction(), new AbstractAsyncCallback<GetServerTimeResult>() {
            @Override
            public void onSuccess(GetServerTimeResult result) {
                currentQuarter = result.getQuarter();
                currentYear = result.getYear();
                final BeanModelFactory factory = BeanModelLookup.get().getFactory(Station.class);
                final ListStore<BeanModel> store = new ListStore<BeanModel>();
                view.getCbStation().setStore(store);
                StandardDispatchAsync.INSTANCE.execute(new LoadAction(Station.class.getName(), ClientRestrictions.eq("company", false)),
                        new AbstractAsyncCallback<LoadResult>() {
                            @Override
                            public void onSuccess(LoadResult result) {
                                for (Station entity : result.<Station>getList()) {
                                    store.add(factory.createModel(entity));
                                }
                                if (!result.getList().isEmpty()) {
                                    view.getCbStation().setValue(store.getAt(0));
                                    currentStation = store.getAt(0).getBean();
                                    view.createGrid(createGridStore());
                                    view.getPagingToolBar().bind((PagingLoader<?>) view.getGird().getStore().getLoader());
                                    view.getPagingToolBar().refresh();
                                    view.getCbQuarter().setSimpleValue(currentQuarter);
                                    view.getCbYear().setSimpleValue(currentYear);

                                    view.getCbStation().addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {
                                        @Override
                                        public void selectionChanged(SelectionChangedEvent<BeanModel> beanModelSelectionChangedEvent) {
                                            currentStation = view.getCbStation().getValue().getBean();
                                            view.getPagingToolBar().refresh();
                                        }
                                    });

                                    view.getCbQuarter().addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<QuarterEnum>>() {
                                        @Override
                                        public void selectionChanged(SelectionChangedEvent<SimpleComboValue<QuarterEnum>> simpleComboValueSelectionChangedEvent) {
                                            currentQuarter = view.getCbQuarter().getSimpleValue();
                                            view.getPagingToolBar().refresh();
                                        }
                                    });

                                    view.getCbYear().addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<Integer>>() {
                                        @Override
                                        public void selectionChanged(SelectionChangedEvent<SimpleComboValue<Integer>> simpleComboValueSelectionChangedEvent) {
                                            currentYear = view.getCbYear().getSimpleValue();
                                            view.getPagingToolBar().refresh();
                                        }
                                    });
                                }
                            }
                        });
            }
        });

        view.getCbGroup().setStore(GridUtils.createListStoreForCb(MaterialGroup.class));

        view.getBtnAdd().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {

                view.getCbPerson().clearSelections();
                view.getCbPerson().setStore(GridUtils.createListStoreForCb(MaterialPerson.class,
                        ClientRestrictions.eq("station.id", currentStation.getId())));

                currentMaterial = new MaterialIn();
                currentMaterial.setCreateBy(1l);
                currentMaterial.setUpdateBy(1l);

                editWindow = view.createEditWindow(createMaterialStore());
                view.resetEditPanel();
                view.getTxtTotal().setEnabled(true);
                view.getTxtTotal().setReadOnly(false);
                view.getMaterialPagingToolBar().bind((PagingLoader<?>) view.getMaterialGrid().getStore().getLoader());
                if (view.getMaterialGrid().getStore().getLoadConfig() != null) {
                    resetMaterialFilter();
                }
                view.getMaterialPagingToolBar().refresh();
                view.getMaterialGrid().getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {
                    @Override
                    public void selectionChanged(SelectionChangedEvent<BeanModel> event) {
                        if (event.getSelectedItem() != null) {
                            Material material = event.getSelectedItem().getBean();
                            LoadMaterialInTotalAction loadAction = new LoadMaterialInTotalAction(material.getId(), currentStation.getId(),
                                    null, currentQuarter.getCode(), currentYear);
                            view.getMaterialGrid().mask();
                            dispatch.execute(loadAction, new AbstractAsyncCallback<LoadMaterialInTotalResult>() {
                                @Override
                                public void onSuccess(LoadMaterialInTotalResult result) {
                                    view.getMaterialGrid().unmask();
                                    view.getTxtTotal().setValue(result.getTotal());
                                }
                            });
                        }
                    }
                });
                editWindow.show();
            }
        });

        view.getBtnEdit().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                if (view.getGird().getSelectionModel().getSelectedItem() != null) {
                    view.getCbPerson().setStore(GridUtils.createListStoreForCb(MaterialPerson.class,
                            ClientRestrictions.eq("station.id", currentStation.getId())));

                    MaterialIn selectedMaterial = view.getGird().getSelectionModel().getSelectedItem().getBean();
                    editWindow = view.createEditWindow(null);
                    currentMaterial = selectedMaterial;

                    view.getTxtCode().setValue(currentMaterial.getCode());
                    view.getTxtTotal().setValue(currentMaterial.getTotal());
                    view.getTxtWeight().setValue(currentMaterial.getWeight());
                    view.getExportDate().setValue(currentMaterial.getExportDate());

                    view.getTxtTotal().setEnabled(false);
                    view.getTxtTotal().setReadOnly(true);

                    BeanModelFactory groupFactory = BeanModelLookup.get().getFactory(MaterialGroup.class);
                    BeanModelFactory personFactory = BeanModelLookup.get().getFactory(MaterialPerson.class);

                    if (currentMaterial.getMaterialGroup() != null) {
                        BeanModel group = groupFactory.createModel(currentMaterial.getMaterialGroup());
                        view.getCbGroup().setValue(group);
                    } else {
                        view.getCbGroup().clear();
                    }

                    if (currentMaterial.getMaterialPerson() != null) {
                        BeanModel person = personFactory.createModel(currentMaterial.getMaterialPerson());
                        view.getCbPerson().setValue(person);
                        view.getCbPerson().clearInvalid();
                    } else {
                        view.getCbPerson().clear();
                    }

                    editWindow.show();
                    editWindow.layout(true);
                }
            }
        });

        view.getBtnRefresh().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                resetFilter();
                view.getPagingToolBar().refresh();
            }
        });

        view.getBtnEditCancel().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                view.resetEditPanel();
                editWindow.hide();
            }
        });

        view.getBtnEditOk().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                if (view.getEditPanel().isValid()) {
                    Material material = null;
                    if (currentMaterial != null && currentMaterial.getMaterial() != null) {
                        material = currentMaterial.getMaterial();
                    } else if (view.getMaterialGrid().getSelectionModel().getSelectedItem() != null) {
                        material = view.getMaterialGrid().getSelectionModel().getSelectedItem().getBean();
                    }

                    if (material != null) {
                        double total = view.getTxtTotal().getValue().doubleValue();
                        double weight = view.getTxtWeight().getValue().doubleValue();

                        MaterialPerson materialPerson = view.getCbPerson().getValue().getBean();
                        MaterialGroup materialGroup = view.getCbGroup().getValue().getBean();

                        currentMaterial.setMaterial(material);
                        currentMaterial.setMaterialGroup(materialGroup);
                        currentMaterial.setMaterialPerson(materialPerson);
                        currentMaterial.setTotal(total);
                        currentMaterial.setWeight(weight);
                        currentMaterial.setCreatedDate(new Date());
                        currentMaterial.setStation(currentStation);
                        currentMaterial.setYear(currentYear);
                        currentMaterial.setQuarter(currentQuarter.getCode());
                        currentMaterial.setCode(view.getTxtCode().getValue());
                        currentMaterial.setExportDate(view.getExportDate().getValue());

                        dispatch.execute(new SaveAction(currentMaterial), new AbstractAsyncCallback<SaveResult>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                if (caught instanceof ServiceException) {
                                    String causeClassName = ((ServiceException) caught).getCauseClassname();
                                    if (causeClassName.contains(ExceptionConstant.DATA_EXCEPTION)) {
                                        DiaLogUtils.showMessage(view.getConstant().existCodeMessage());
                                    }
                                } else {
                                    super.onFailure(caught);
                                }
                            }

                            @Override
                            public void onSuccess(SaveResult saveResult) {
                                editWindow.hide();
                                view.resetEditPanel();
                                view.getPagingToolBar().refresh();
                            }
                        });

                    } else {
                        DiaLogUtils.showMessage(view.getConstant().materialError());
                    }
                }
            }
        });

        view.getTxtCodeSearch().addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    String st = view.getTxtCodeSearch().getValue();
                    view.getTxtNameSearch().clear();
                    if (StringUtils.isNotBlank(st)) {
                        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getGird().getStore().getLoadConfig();
                        loadConfig.set("hasFilter", true);
                        Map<String, Object> filters = new HashMap<String, Object>();
                        filters.put("code", st);
                        loadConfig.set("filters", filters);
                    } else {
                        resetFilter();
                    }
                    view.getPagingToolBar().refresh();
                } else if (event.getKeyCode() == KeyCodes.KEY_ESCAPE) {
                    resetFilter();
                    com.google.gwt.user.client.Timer timer = new Timer() {
                        @Override
                        public void run() {
                            view.getPagingToolBar().refresh();
                        }
                    };
                    timer.schedule(100);
                }
            }
        });

        view.getTxtNameSearch().addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    String st = view.getTxtNameSearch().getValue();
                    view.getTxtCodeSearch().clear();
                    if (StringUtils.isNotBlank(st)) {
                        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getGird().getStore().getLoadConfig();
                        loadConfig.set("hasFilter", true);
                        Map<String, Object> filters = new HashMap<String, Object>();
                        filters.put("material.name", st);
                        filters.put("material.code", st);
                        loadConfig.set("filters", filters);
                    } else {
                        resetFilter();
                    }
                    view.getPagingToolBar().refresh();
                } else if (event.getKeyCode() == KeyCodes.KEY_ESCAPE) {
                    resetFilter();
                    com.google.gwt.user.client.Timer timer = new Timer() {
                        @Override
                        public void run() {
                            view.getPagingToolBar().refresh();
                        }
                    };
                    timer.schedule(100);
                }
            }
        });

        view.getBtnDelete().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                final List<BeanModel> models = view.getGird().getSelectionModel().getSelectedItems();
                if (!models.isEmpty()) {
                    final List<Long> materialIds = new ArrayList<Long>(models.size());
                    for (BeanModel model : models) {
                        MaterialIn material = model.getBean();
                        materialIds.add(material.getId());
                    }

                    DiaLogUtils.conform(view.getConstant().deleteMessage(), new Listener<MessageBoxEvent>() {
                        @Override
                        public void handleEvent(MessageBoxEvent be) {
                            if (be.getButtonClicked().getText().equals("Yes")) {
                                dispatch.execute(new DeleteAction(MaterialIn.class.getName(), materialIds), new AbstractAsyncCallback<DeleteResult>() {
                                    @Override
                                    public void onSuccess(DeleteResult deleteResult) {
                                        DiaLogUtils.notify(view.getConstant().deleteMessageSuccess());
                                        view.getPagingToolBar().refresh();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        view.getBtnCopy().addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent buttonEvent) {
                dispatch.execute(new CopyMaterialInAction(currentYear, currentQuarter.getCode(), currentStation.getId()), new AbstractAsyncCallback<CopyMaterialInResult>() {
                    @Override
                    public void onSuccess(CopyMaterialInResult copyMaterialInResult) {
                        view.getPagingToolBar().refresh();
                    }
                });
            }
        });
    }

    private ListStore<BeanModel> createGridStore() {
        RpcProxy<LoadGridDataResult> rpcProxy = new RpcProxy<LoadGridDataResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadGridDataResult> callback) {
                LoadGridDataAction loadAction;
                if (!currentStation.isCompany()) {
                    loadAction = new LoadGridDataAction(MaterialIn.class.getName(),
                            ClientRestrictions.eq("year", currentYear),
                            ClientRestrictions.eq("quarter", currentQuarter.getCode()),
                            ClientRestrictions.eq("station.id", currentStation.getId()));
                } else {
                    loadAction = new LoadGridDataAction(MaterialIn.class.getName(),
                            ClientRestrictions.eq("year", currentYear),
                            ClientRestrictions.eq("quarter", currentQuarter.getCode()));
                }

                loadAction.setConfig((BasePagingLoadConfig) loadConfig);
                dispatch.execute(loadAction, callback);
            }
        };

        PagingLoader<PagingLoadResult<MaterialIn>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<MaterialIn>>(rpcProxy, new LoadGridDataReader()) {
                    @Override
                    protected void onLoadFailure(Object loadConfig, Throwable t) {
                        super.onLoadFailure(loadConfig, t);
                        //Log load exception.
                        DiaLogUtils.logAndShowRpcErrorMessage(t);
                    }
                };

        return new ListStore<BeanModel>(pagingLoader);
    }


    private ListStore<BeanModel> createMaterialStore() {
        RpcProxy<LoadMaterialWithTaskResult> rpcProxy = new RpcProxy<LoadMaterialWithTaskResult>() {
            @Override
            protected void load(Object loadConfig, AsyncCallback<LoadMaterialWithTaskResult> callback) {
                dispatch.execute(new LoadMaterialWithTaskAction((BasePagingLoadConfig) loadConfig, currentQuarter.getCode(), currentYear), callback);
            }
        };

        PagingLoader<PagingLoadResult<Material>> pagingLoader =
                new BasePagingLoader<PagingLoadResult<Material>>(rpcProxy, new LoadGridDataReader()) {
                    @Override
                    protected void onLoadFailure(Object loadConfig, Throwable t) {
                        super.onLoadFailure(loadConfig, t);
                        //Log load exception.
                        DiaLogUtils.logAndShowRpcErrorMessage(t);
                    }
                };

        return new ListStore<BeanModel>(pagingLoader);
    }

    private void resetFilter() {
        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getGird().getStore().getLoadConfig();
        loadConfig.set("hasFilter", false);
        loadConfig.set("filters", null);
        view.getTxtCodeSearch().clear();
        view.getTxtNameSearch().clear();
    }

    private void resetMaterialFilter() {
        BasePagingLoadConfig loadConfig = (BasePagingLoadConfig) view.getMaterialGrid().
                getStore().getLoadConfig();
        loadConfig.set("hasFilter", false);
        loadConfig.set("filters", null);
        view.getTxtMaterialSearch().clear();
    }
}
