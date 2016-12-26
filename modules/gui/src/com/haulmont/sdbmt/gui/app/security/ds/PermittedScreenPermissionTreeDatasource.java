/*
 * Copyright (c) 2016 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.sdbmt.gui.app.security.ds;

import com.haulmont.bali.datastruct.Node;
import com.haulmont.bali.datastruct.Tree;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.app.security.ds.ScreenPermissionTreeDatasource;
import com.haulmont.cuba.gui.app.security.entity.BasicPermissionTarget;
import com.haulmont.cuba.security.global.UserSession;

import java.util.LinkedList;
import java.util.List;

public class PermittedScreenPermissionTreeDatasource extends ScreenPermissionTreeDatasource {

    private UserSessionSource uss = AppBeans.get(UserSessionSource.class);

    @Override
    public Tree<BasicPermissionTarget> getPermissions() {
        Tree<BasicPermissionTarget> permissions = super.getPermissions();
        return filterPermitted(permissions);
    }

    private Tree<BasicPermissionTarget> filterPermitted(Tree<BasicPermissionTarget> permissions) {
        UserSession session = uss.getUserSession();
        List<Node<BasicPermissionTarget>> newRootNodes = new LinkedList<>();
        for (Node<BasicPermissionTarget> root : permissions.getRootNodes()) {
            newRootNodes.add(filterNode(session, root));
        }
        return new Tree<>(newRootNodes);
    }

    private Node<BasicPermissionTarget> filterNode(UserSession session, Node<BasicPermissionTarget> rootNode) {
        Node<BasicPermissionTarget> filteredRootNode = new Node<>(rootNode.getData());
        rootNode.getChildren().stream()
                .filter(child -> session.isScreenPermitted(child.getData().getPermissionValue()))
                .forEach(child -> filteredRootNode.addChild(filterNode(session, child)));
        return filteredRootNode;
    }
}
