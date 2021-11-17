package com.labovich.lab4.dao.description;

import com.labovich.lab4.dao.Dao;
import com.labovich.lab4.entity.UserInformation;
import com.labovich.lab4.exeptions.DaoException;


public interface UserInformationDao extends Dao<UserInformation> {

    void updateById(int id, UserInformation userInformation) throws DaoException;
}
