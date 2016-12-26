/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.sdbmt.web.menuitem;

import com.haulmont.cuba.gui.app.core.file.FileDownloadHelper;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.sdbmt.entity.MenuItem;

import javax.inject.Inject;
import java.util.Map;

public class MenuItemBrowse extends AbstractLookup {
    @Inject
    private Table<MenuItem> menuItemsTable;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        FileDownloadHelper.initGeneratedColumn(menuItemsTable, "photo");
    }
}