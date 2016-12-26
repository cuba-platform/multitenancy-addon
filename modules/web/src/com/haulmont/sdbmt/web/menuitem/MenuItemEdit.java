/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.sdbmt.web.menuitem;

import com.haulmont.cuba.core.app.FileStorageService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.Embedded;
import com.haulmont.cuba.gui.components.FileUploadField;
import com.haulmont.sdbmt.entity.MenuItem;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.util.Map;

public class MenuItemEdit extends AbstractEditor<MenuItem> {
    @Inject
    private Embedded image;
    @Named("fieldGroup.photo")
    private FileUploadField photoField;
    @Inject
    private FileStorageService fileStorageService;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
    }

    @Override
    protected void postInit() {
        updateImage();
        photoField.addValueChangeListener(e -> updateImage());
    }

    private void updateImage() {
        FileDescriptor imageFile = getItem().getPhoto();
        if (imageFile == null) {
            return;
        }

        byte[] bytes = null;
        try {
            bytes = fileStorageService.loadFile(imageFile);
        } catch (FileStorageException e) {
            showNotification("Unable to load image file", NotificationType.HUMANIZED);
        }
        if (bytes != null) {
            image.setSource(imageFile.getName(), new ByteArrayInputStream(bytes));
            image.setType(Embedded.Type.IMAGE);
            image.setHeight("250px");
            image.setWidth("250px");
        } else {
            image.setVisible(false);
        }
    }
}