/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.StationManagerConstant;
import com.qlkh.client.client.module.content.view.security.StationManagerSecurity;
import com.qlkh.client.client.widget.MyFitLayout;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class StationManagerView.
 *
 * @author Nguyen Duc Dung
 * @since 12/29/11, 6:57 AM
 */
@ViewSecurity(configuratorClass = StationManagerSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = StationManagerConstant.class)
public class StationManagerView extends AbstractView<StationManagerConstant> {

    public static final String ID_COLUMN = "id";
    public static final String STT_COLUMN = "stt";
    public static final int STT_COLUMN_WIDTH = 40;
    public static final String STATION_NAME_COLUMN = "name";
    public static final int STATION_NAME_WIDTH = 300;

    public static final int STATION_LIST_SIZE = 100;

    @I18nField
    Button btnAdd = new Button(null, IconHelper.createPath("assets/images/icons/fam/add.png"));

    @I18nField
    Button btnDelete = new Button(null, IconHelper.createPath("assets/images/icons/fam/delete.png"));

    @I18nField
    Button btnEdit = new Button(null, IconHelper.createPath("assets/images/icons/fam/disk.png"));

    @I18nField
    Button btnRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));

    @I18nField
    Button btnStationEditOk = new Button();

    @I18nField
    Button btnStationEditCancel = new Button();

    @I18nField
    TextField<String> txtStationName = new TextField<String>();

    private ContentPanel contentPanel = new ContentPanel();
    private PagingToolBar pagingToolBar;
    private Grid<BeanModel> stationsGird;

    private FormPanel stationEditPanel = new FormPanel();

    /**
     * Create Grid on View.
     */
    public void createGrid(ListStore<BeanModel> listStore) {
        CheckBoxSelectionModel<BeanModel> selectionModel = new CheckBoxSelectionModel<BeanModel>();
        ColumnModel cm = new ColumnModel(createColumnConfig(selectionModel));
        stationsGird = new Grid<BeanModel>(listStore, cm);
        stationsGird.setBorders(true);
        stationsGird.setLoadMask(true);
        stationsGird.setStripeRows(true);
        stationsGird.setSelectionModel(selectionModel);
        stationsGird.addPlugin(selectionModel);
        stationsGird.getStore().getLoader().setSortDir(Style.SortDir.ASC);
        stationsGird.getStore().getLoader().setSortField(ID_COLUMN);
        stationsGird.addListener(Events.OnKeyDown, new KeyListener() {
            @Override
            public void handleEvent(ComponentEvent e) {
                if (e.getKeyCode() == 112) {
                    btnAdd.fireEvent(Events.Select);
                } else if (e.getKeyCode() == 113 || e.getKeyCode() == KeyCodes.KEY_ENTER) {
                    btnEdit.fireEvent(Events.Select);
                } else if (e.getKeyCode() == 115) {
                    btnRefresh.fireEvent(Events.Select);
                }
            }
        });

        pagingToolBar = new PagingToolBar(STATION_LIST_SIZE);
        ToolBar toolBar = new ToolBar();
        toolBar.add(btnAdd);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnEdit);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnDelete);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnRefresh);

        contentPanel.setLayout(new MyFitLayout());
        contentPanel.add(stationsGird);
        contentPanel.setTopComponent(toolBar);
        contentPanel.setBottomComponent(pagingToolBar);
        contentPanel.setHeaderVisible(false);
        contentPanel.setHeight(Window.getClientHeight() - 90);
        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                contentPanel.layout(true);
            }
        });
        setWidget(contentPanel);
    }

    private List<ColumnConfig> createColumnConfig(CheckBoxSelectionModel<BeanModel> selectionModel) {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
        columnConfigs.add(selectionModel.getColumn());
        ColumnConfig sttColumnConfig = new ColumnConfig(STT_COLUMN, getConstant().sttColumnTitle(), STT_COLUMN_WIDTH);
        sttColumnConfig.setRenderer(new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex,
                                 ListStore<BeanModel> beanModelListStore, Grid<BeanModel> beanModelGrid) {
                if (model.get(STT_COLUMN) == null) {
                    model.set(STT_COLUMN, rowIndex + 1);
                }
                return new Text(String.valueOf(model.get(STT_COLUMN)));
            }
        });
        columnConfigs.add(sttColumnConfig);
        ColumnConfig stationNameColumnConfig = new ColumnConfig(STATION_NAME_COLUMN, getConstant().stationNameColumnTitle(),
                STATION_NAME_WIDTH);
        columnConfigs.add(stationNameColumnConfig);
        return columnConfigs;
    }

    public com.extjs.gxt.ui.client.widget.Window createStationEditWindow() {
        com.extjs.gxt.ui.client.widget.Window window = new com.extjs.gxt.ui.client.widget.Window();
        if (!stationEditPanel.isRendered()) {
            stationEditPanel.setHeaderVisible(false);
            stationEditPanel.setBodyBorder(false);
            stationEditPanel.setBorders(false);
            stationEditPanel.setLabelWidth(120);
        }

        if (!txtStationName.isRendered()) {
            txtStationName.setSelectOnFocus(true);
            txtStationName.setAllowBlank(false);
            txtStationName.setMinLength(4);
        }
        stationEditPanel.add(txtStationName);
        window.setFocusWidget(txtStationName);

        window.add(stationEditPanel);
        window.addButton(btnStationEditOk);
        window.addButton(btnStationEditCancel);
        window.setModal(true);
        window.setSize(380, 120);
        window.setResizable(false);
        window.setHeading(getConstant().stationEditPanelTitle());
        window.addWindowListener(new WindowListener() {
            @Override
            public void windowHide(WindowEvent we) {
                stationEditPanel.clear();
                stationsGird.focus();
            }
        });
        return window;
    }

    public Button getBtnRefresh() {
        return btnRefresh;
    }

    public Button getBtnAdd() {
        return btnAdd;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public Button getBtnEdit() {
        return btnEdit;
    }

    public PagingToolBar getPagingToolBar() {
        return pagingToolBar;
    }

    public Grid<BeanModel> getStationsGird() {
        return stationsGird;
    }

    public Button getBtnStationEditOk() {
        return btnStationEditOk;
    }

    public Button getBtnStationEditCancel() {
        return btnStationEditCancel;
    }

    public TextField<String> getTxtStationName() {
        return txtStationName;
    }

    public ContentPanel getContentPanel() {
        return contentPanel;
    }

    public FormPanel getStationEditPanel() {
        return stationEditPanel;
    }
}
