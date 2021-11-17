package com.labovich.lab4.controller.command;

import com.labovich.lab4.controller.context.RequestContextHelper;

import javax.servlet.http.HttpServletResponse;

public interface Command {
    CommandResult execute(RequestContextHelper helper, HttpServletResponse response);
}
