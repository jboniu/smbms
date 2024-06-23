package com.ssm.controller;

import com.ssm.pojo.SmbmsUser;
import com.ssm.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class Login {
    @Autowired
    UserServiceImpl userServiceImpl;

    @PostMapping("/login")
    public String Login(String userCode, String userPassword, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SmbmsUser smbmsUser = new SmbmsUser();
        smbmsUser.setUsercode(userCode);
        smbmsUser.setUserpassword(userPassword);
        List<SmbmsUser> smbmsUsers = userServiceImpl.loginFind(smbmsUser);
        if (smbmsUsers.size() == 0) {
            //给error添加一段文字
            request.setAttribute("error", "用户名或密码不正确！");
            //重定向到login.jsp
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return "error";
        } else {
            //给usersession添加当前进入的用户信息
            request.getSession().setAttribute("userSession", smbmsUsers.get(0));
            return "frame";
        }
    }
}



