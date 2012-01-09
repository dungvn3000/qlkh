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

package com.qlvt.client.client.module.main.view;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.qlvt.client.client.constant.DomIdConstant;
import com.qlvt.client.client.module.content.place.*;
import com.qlvt.client.client.module.main.view.i18n.MainMenuConstant;
import com.qlvt.client.client.module.main.view.security.MainMenuViewSecutiry;
import com.qlvt.client.client.module.user.place.UserManagerPlace;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.smvp4g.mvp.client.core.eventbus.annotation.HistoryHandler;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.FieldSecurity;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;
import com.smvp4g.mvp.client.widget.MenuLink;

import static com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;

/**
 * The Class MainMenuView.
 *
 * @author Nguyen Duc Dung
 * @since 12/28/11, 9:07 AM
 */
@ViewSecurity(configuratorClass = MainMenuViewSecutiry.class)
@View(parentDomId = DomIdConstant.TOP_PANEL, constantsClass = MainMenuConstant.class)
public class MainMenuView extends AbstractView<MainMenuConstant> {

    @FieldSecurity
    @HistoryHandler
    @I18nField
    MenuLink mnlUserManager = new MenuLink(UserManagerPlace.class);

    @FieldSecurity
    @HistoryHandler
    @I18nField
    MenuLink mnlStationManager = new MenuLink(StationManagerPlace.class);


    @FieldSecurity
    @HistoryHandler
    @I18nField
    MenuLink mnlBranchManager = new MenuLink(BranchManagerPlace.class);

    @FieldSecurity
    @HistoryHandler
    @I18nField
    MenuLink mnlTaskManage = new MenuLink(TaskManagerPlace.class);

    @FieldSecurity
    @HistoryHandler
    @I18nField
    MenuLink mnlTaskDetail = new MenuLink(TaskDetailPlace.class);

    @FieldSecurity
    @HistoryHandler
    @I18nField
    MenuLink mnlTaskAnnualDetail = new MenuLink(TaskAnnualDetailPlace.class);

    @FieldSecurity
    @I18nField
    Anchor ancLogout = new Anchor("");

    @FieldSecurity
    @I18nField
    Label lblWelcome = new Label();

    private HBoxLayoutContainer mainPanel = new HBoxLayoutContainer();

    @Override
    protected void initializeView() {
        mainPanel.add(mnlUserManager, new BoxLayoutData(new Margins(0, 5, 0, 0)));
        mainPanel.add(mnlStationManager, new BoxLayoutData(new Margins(0, 5, 0, 0)));
        mainPanel.add(mnlBranchManager, new BoxLayoutData(new Margins(0, 5, 0, 0)));
        mainPanel.add(mnlTaskManage, new BoxLayoutData(new Margins(0, 5, 0, 0)));
        mainPanel.add(mnlTaskAnnualDetail, new BoxLayoutData(new Margins(0, 5, 0, 0)));
        mainPanel.add(mnlTaskDetail, new BoxLayoutData(new Margins(0, 5, 0, 0)));

        BoxLayoutData flex = new BoxLayoutData();
        flex.setFlex(1);
        mainPanel.add(new Label(), flex);
        lblWelcome.setWidth("200px");
        lblWelcome.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        mainPanel.add(lblWelcome, new BoxLayoutData(new Margins(0, 5, 0, 0)));
        mainPanel.add(ancLogout, new BoxLayoutData(new Margins(0,5,0,0)));
        mainPanel.setHeight(22);
        mainPanel.setEnableOverflow(false);
        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                mainPanel.setPixelSize(event.getWidth(), 22);
            }
        });
        setWidget(mainPanel);
        setStyleName("top_menu");
    }

    public Anchor getAncLogout() {
        return ancLogout;
    }

    public Label getLblWelcome() {
        return lblWelcome;
    }
}
