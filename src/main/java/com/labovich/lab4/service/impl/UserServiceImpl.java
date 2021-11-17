package com.labovich.lab4.service.impl;

import com.labovich.lab4.dao.DaoFactory;
import com.labovich.lab4.dao.description.UserDao;
import com.labovich.lab4.dao.impl.RoleDaoImpl;
import com.labovich.lab4.dao.impl.UserInformationDaoImpl;
import com.labovich.lab4.entity.Role;
import com.labovich.lab4.entity.User;
import com.labovich.lab4.entity.UserInformation;
import com.labovich.lab4.entity.UserOrder;
import com.labovich.lab4.exeptions.DaoException;
import com.labovich.lab4.exeptions.ServiceException;
import com.labovich.lab4.service.description.UserService;
import com.labovich.lab4.service.validator.Validator;
import com.labovich.lab4.service.validator.ValidatorFactory;
import org.apache.commons.codec.digest.DigestUtils;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {


    private static final String USER = "user";

    @Override
    public Optional<User> login(String email, String password) throws ServiceException {
        if (email == null || password == null) {
            return Optional.empty();
        }

        if (!isEmailValid(email)) {
            return Optional.empty();
        }

        try {
            UserDao userDao = DaoFactory.getInstance().getUserDao();
            return userDao.findByEmailAndPassword(email, password);
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<User> getUsersFromOrders(List<UserOrder> orders) throws ServiceException {
        List<User> users = new LinkedList<>();
        try {
            for (UserOrder order : orders) {
                Optional<User> user = retrieveUserById(order.getUserId());
                if (user.isPresent()) {
                    if (!users.contains(user.get())) {
                        users.add(user.get());
                    }
                }
            }
        } catch (ServiceException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return users;
    }
    @Override
    public boolean register(String name, String surname,
                            String email, String phone, String password) throws ServiceException {
        if (name == null || surname == null ||
                email == null || phone == null || password == null) {
            return false;
        }

        if (!(isEmailValid(email) && isUserInformationValid(name, surname,  phone))) {
            return false;
        }
        try {
            UserDao userDao = DaoFactory.getInstance().getUserDao();
            if (userDao.findByEmail(email).isPresent()) {
                return false;
            }
            RoleDaoImpl roleDao = DaoFactory.getInstance().getRoleDao();
            Optional<Role> role = roleDao.findByName(USER);
            if (!role.isPresent()) {
                return false;
            }

            UserInformationDaoImpl userInformationDao = DaoFactory.getInstance().getUserInformationDao();
            UserInformation userInformation = buildUserInformation(name, surname,  phone);
            int userInformationId = userInformationDao.save(userInformation);

            User user = buildUser(email, password, userInformationId, role.get().getId());
            userDao.save(user);

            return true;
        } catch (DaoException e) {

            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> retrieveUserById(int userId) throws ServiceException {
        try {
            UserDao userDao = DaoFactory.getInstance().getUserDao();
            Optional<User> result;
            result = userDao.findById(userId);
            return result;
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }



    private boolean isEmailValid(String email) {
        Validator validator = ValidatorFactory.getInstance().getEmailValidator();
        return validator.isValid(email);
    }

    private boolean isUserInformationValid(String name, String surname, String phone) {
        Validator nameValidator = ValidatorFactory.getInstance().getNameValidator();
        Validator phoneValidator = ValidatorFactory.getInstance().getPhoneValidator();

        return nameValidator.isValid(name) && nameValidator.isValid(surname)  &&
                phoneValidator.isValid(phone);
    }

    private UserInformation buildUserInformation(String name, String surname, String phone) {
        UserInformation userInformation = new UserInformation();
        userInformation.setName(name);
        userInformation.setSurname(surname);
        userInformation.setPhone(phone);
        return userInformation;
    }

    private User buildUser(String email, String password, int userInformationId, int roleId) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(DigestUtils.sha1Hex(password));
        user.setUserInformationId(userInformationId);
        user.setRoleId(roleId);
        return user;
    }
}
