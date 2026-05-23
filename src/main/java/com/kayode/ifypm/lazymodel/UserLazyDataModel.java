package com.kayode.ifypm.lazymodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayode.ifypm.model.PagedList;
import com.kayode.ifypm.model.Role;
import com.kayode.ifypm.model.User;
import com.kayode.ifypm.service.UserService;

public class UserLazyDataModel extends LazyDataModel<User> {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(UserLazyDataModel.class);

    private final UserService service;
    private final Role role;

    public UserLazyDataModel(UserService service, Role role) {
        this.service = service;
        this.role = role;
    }

    @Override
    public List<User> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        try {
            PagedList<User> page = service.fetchUsersByRole(first, pageSize, role);
            this.setRowCount(page.getCount());
            return page.getList();
        } catch (Exception e) {
            LOG.error("Error loading users (role={})", role, e);
            return new ArrayList<>();
        }
    }

    @Override
    public String getRowKey(User u) {
        return u.getId() != null ? u.getId().toString() : null;
    }

    @Override
    public User getRowData(String rowKey) {
        List<User> data = getWrappedData();
        if (data == null || rowKey == null) return null;
        for (User u : data) {
            if (rowKey.equals(String.valueOf(u.getId()))) return u;
        }
        return null;
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        return 0;
    }
}
