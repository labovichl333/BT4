package com.labovich.lab4.dao.impl;

import com.labovich.lab4.dao.AbstractDao;
import com.labovich.lab4.dao.Table;
import com.labovich.lab4.dao.description.RoleDao;
import com.labovich.lab4.dao.mapper.RowMapperFactory;
import com.labovich.lab4.entity.Role;
import com.labovich.lab4.exeptions.DaoException;

import java.util.Optional;

public class RoleDaoImpl extends AbstractDao<Role> implements RoleDao {
    private static final String SAVE_ROLE_QUERY = "INSERT INTO " + Table.ROLE + " (role) VALUES (?)";
    private static final String FIND_ROLE_BY_NAME_QUERY = "SELECT * FROM " + Table.ROLE + " WHERE role=?";

    public RoleDaoImpl() {
        super(RowMapperFactory.getInstance().getRoleRowMapper(), Table.ROLE);
    }

    @Override
    public int save(Role role) throws DaoException {
        return executeInsertQuery(SAVE_ROLE_QUERY, role.getName());
    }

    @Override
    public Optional<Role> findByName(String name) throws DaoException {
        return executeQueryForSingleResult(FIND_ROLE_BY_NAME_QUERY, name);
    }
}
