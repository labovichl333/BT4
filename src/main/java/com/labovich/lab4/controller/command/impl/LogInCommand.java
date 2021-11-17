package com.labovich.lab4.controller.command.impl;

import com.labovich.lab4.controller.command.Command;
import com.labovich.lab4.controller.command.CommandResult;
import com.labovich.lab4.controller.command.CommandResultType;
import com.labovich.lab4.controller.context.RequestContext;
import com.labovich.lab4.controller.context.RequestContextHelper;
import com.labovich.lab4.entity.Role;
import com.labovich.lab4.entity.User;
import com.labovich.lab4.exeptions.ServiceException;
import com.labovich.lab4.service.ServiceFactory;
import com.labovich.lab4.service.description.RoleService;
import com.labovich.lab4.service.description.UserService;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class LogInCommand implements Command {
    private static final String PROFILE_PAGE = "command=profile";
    private static final String ERROR_PAGE = "WEB-INF/view/error.jsp";
    private static final String LOGIN_PAGE = "WEB-INF/view/login.jsp";
    private static final String PASSWORD_PARAMETER = "password";
    private static final String EMAIL_PARAMETER = "email";
    private static final String USER = "user";
    private static final String ROLE = "role";
    private static final String ERROR_MESSAGE = "errorMessage";

    @Override
    public CommandResult execute(RequestContextHelper helper, HttpServletResponse response) {
        RequestContext requestContext = helper.createContext();

        String password = requestContext.getRequestParameter(PASSWORD_PARAMETER);
        String login = requestContext.getRequestParameter(EMAIL_PARAMETER);

        try {
            UserService userService = ServiceFactory.getInstance().getUserService();
            Optional<User> optionalResult = userService.login(login, password);

            if (optionalResult.isPresent()) {
                requestContext.addSessionAttribute(USER, optionalResult.get());

                RoleService roleService = ServiceFactory.getInstance().getRoleService();
                Optional<Role> role = roleService.retrieveRoleById(optionalResult.get().getRoleId());
                role.ifPresent(value -> requestContext.addSessionAttribute(ROLE, value));

                helper.updateRequest(requestContext);
                return new CommandResult(PROFILE_PAGE, CommandResultType.REDIRECT);
            }
        } catch (ServiceException e) {
            return new CommandResult(ERROR_PAGE, CommandResultType.FORWARD);
        }

        requestContext.addRequestAttribute(ERROR_MESSAGE, true);
        helper.updateRequest(requestContext);
        return new CommandResult(LOGIN_PAGE, CommandResultType.FORWARD);
    }
}