package com.labovich.lab4.dao.mapper.impl;

import com.labovich.lab4.dao.mapper.Column;
import com.labovich.lab4.dao.mapper.RowMapper;
import com.labovich.lab4.entity.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleRowMapper implements RowMapper<Role> {

    @Override
    public Role map(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getInt(Column.ID));
        role.setName(resultSet.getString(Column.ROLE_NAME));
        return role;
    }
}