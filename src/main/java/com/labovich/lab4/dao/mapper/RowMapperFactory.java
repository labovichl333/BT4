package com.labovich.lab4.dao.mapper;

import com.labovich.lab4.dao.mapper.impl.*;
import com.labovich.lab4.entity.*;

public class RowMapperFactory {
    public static RowMapperFactory getInstance() {
        return Holder.INSTANCE;
    }


    private final RowMapper<User> userRowMapper = new UserRowMapper();
    private final RowMapper<Role> roleRowMapper = new RoleRowMapper();
    private final RowMapper<UserInformation> userInformationRowMapper = new UserInformationRowMapper();
    private final RowMapper<UserOrder> userOrderRowMapper = new UserOrderRowMapper();
    private final RowMapper<Apartment> apartmentRowMapper = new ApartmentRowMapper();

    public RowMapper<User> getUserRowMapper() {
        return userRowMapper;
    }

    public RowMapper<Role> getRoleRowMapper() {
        return roleRowMapper;
    }

    public RowMapper<UserInformation> getUserInformationRowMapper() {
        return userInformationRowMapper;
    }

    public RowMapper<UserOrder> getUserOrderRowMapper() {
        return userOrderRowMapper;
    }

    public RowMapper<Apartment> getApartmentRowMapper() {
        return apartmentRowMapper;
    }

    private static class Holder {
        private static final RowMapperFactory INSTANCE = new RowMapperFactory();
    }
}
