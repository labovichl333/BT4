package com.labovich.lab4.controller.command.impl.transition;

import com.labovich.lab4.controller.command.Command;
import com.labovich.lab4.controller.command.CommandResult;
import com.labovich.lab4.controller.command.CommandResultType;
import com.labovich.lab4.controller.context.RequestContext;
import com.labovich.lab4.controller.context.RequestContextHelper;
import com.labovich.lab4.entity.User;
import com.labovich.lab4.entity.UserInformation;
import com.labovich.lab4.exeptions.ServiceException;
import com.labovich.lab4.service.ServiceFactory;
import com.labovich.lab4.service.description.UserInformationService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public class GoToProfileCommand implements Command {
    private static final String PAGE = "WEB-INF/view/profile.jsp";
    private static final String ERROR_PAGE = "WEB-INF/view/error.jsp";
    private static final String USER = "user";
    private static final String USER_INFORMATION = "userInformation";


    @Override
    public CommandResult execute(RequestContextHelper helper, HttpServletResponse response) {
        RequestContext requestContext = helper.createContext();

        User user = (User) requestContext.getSessionAttribute(USER);
        if (user == null) {
            helper.updateRequest(requestContext);
            return new CommandResult(PAGE, CommandResultType.FORWARD);
        }

        try {


            int userInformationId = user.getUserInformationId();
            UserInformationService userInformationService = ServiceFactory.getInstance().getUserInformationService();

            Optional<UserInformation> userInformation = userInformationService.retrieveUserInformationById(userInformationId);
            userInformation.ifPresent(information -> requestContext.addRequestAttribute(USER_INFORMATION, information));
        } catch (ServiceException e) {
            return new CommandResult(ERROR_PAGE, CommandResultType.FORWARD);
        }

        helper.updateRequest(requestContext);
        return new CommandResult(PAGE, CommandResultType.FORWARD);
    }
}
