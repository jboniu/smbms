package com.ssm.controller;

import com.alibaba.fastjson.JSONArray;
import com.ssm.pojo.SmbmsRole;
import com.ssm.pojo.SmbmsUser;
import com.ssm.service.impl.RoleServiceImpl;
import com.ssm.service.impl.UserServiceImpl;
import com.ssm.util.PageSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    RoleServiceImpl roleServiceImpl;

    @GetMapping("/user")
    public String User(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        System.out.println("method:" + method);
        if (method != null && method.equals("view")) {
            this.view(req, resp);
            return "userview";
        } else if (method != null && method.equals("pwdmodify")) {
            this.pwdModify(req, resp);
        } else if (method != null && method.equals("query")) {
            this.query(req, resp);
        } else if (method != null && method.equals("modify")) {
            this.findById(req, resp);
        }else if (method != null && method.equals("deluser")){
            this.deleUser(req,resp);
        }
        return null;
    }
    private void deleUser(HttpServletRequest req, HttpServletResponse resp) {
        //从前端获取 要删除的用户 的信息
        String userid = req.getParameter("uid");
        int delId = 0;
        //先转换
        try {
            delId = Integer.parseInt(userid);
        }catch (Exception e){
            e.printStackTrace();
            delId = 0;
        }
        System.out.println("-------------------------------------------");
        System.out.println(delId);
        //将结果存放在map集合中 让Ajax使用
        Map<String, String> resultMap = new HashMap<>();
        if(delId<=0){
            resultMap.put("delResult","notexist");
        }else {
            if(userServiceImpl.deleUser(delId)){
                resultMap.put("delResult","true");
            }else {
                resultMap.put("delResult", "false");
            }
        }
        //上面已经封装好 现在需要传给Ajax 格式为json 所以我们得转换格式
        resp.setContentType("application/json");//将应用的类型变成json
        PrintWriter writer = null;
        try {
            writer = resp.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //JSONArray 阿里巴巴的JSON工具类 用途就是：转换格式
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }
    @PostMapping("/user/modifyUser")
    public void modifyUser(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        String id = request.getParameter("uid");
        String userName = request.getParameter("userName");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");
        SmbmsUser user = new SmbmsUser();
        user.setId((long) Integer.parseInt(id));
        user.setUsername(userName);
        user.setGender(Integer.valueOf(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserrole((long) Integer.parseInt(userRole));
        user.setModifyby(((SmbmsUser)request.getSession().getAttribute("userSession")).getId());
        user.setModifydate(new Date());

        if(userServiceImpl.modifyUser(user)){
            response.sendRedirect(request.getContextPath()+"/user?method=query");
        }else{
            request.getRequestDispatcher("/jsp/usermodify.jsp").forward(request, response);
        }
    }

    @PostMapping("/user/savepwd")
    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) {
        //获取当前页面登录者的信息
        Object o = req.getSession().getAttribute("userSession");
        String password = req.getParameter("newpassword");
        Boolean flag = false;
        if (o != null && password != null && password.length() != 0) {
            flag = userServiceImpl.updatePwd(((SmbmsUser) o).getId(), password);
            if (flag) {
                req.getSession().setAttribute("message", "修改密码成功，请使用新密码登录！");
                req.getSession().removeAttribute("userSession");
            } else {
                req.getSession().setAttribute("message", "修改密码失败，请重新修改！");
            }
        } else {
            req.getSession().setAttribute("message", "新密码不符合要求，请重新输入！");
        }

        try {
            req.getRequestDispatcher("/jsp/pwdmodify.jsp").forward(req, resp);
            req.getSession().removeAttribute("message");
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("")
    public void pwdModify(HttpServletRequest req, HttpServletResponse resp) {
        Object o = req.getSession().getAttribute("userSession");
        String oldpqssword = req.getParameter("oldpassword");
        Map<String, String> resultMap = new HashMap<>();
        if (o == null) {
            resultMap.put("result", "sessionerror");
        } else if (!(oldpqssword != null && oldpqssword.length() != 0)) {
            resultMap.put("result", "error");
        } else {//getUserPassword
            String password = ((SmbmsUser) o).getUserpassword();
            System.out.println("oldpassword: " + oldpqssword);
            System.out.println("password " + password);
            if (password != null && password.equals(oldpqssword)) {
                resultMap.put("result", "true");
            } else {
                resultMap.put("result", "false");
            }
        }

        resp.setContentType("application/json"); // ajax异步请求
        try {
            PrintWriter printWriter = resp.getWriter();
            // JSONArray 阿里巴巴的JSON工具类，转换格式
            // Map -- > json
            printWriter.write(JSONArray.toJSONString(resultMap));
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @PostMapping("/useradd")
    public void add(HttpServletRequest req, HttpServletResponse resp) {
        Long id = Long.parseLong(req.getParameter("id"));
//        String id = req.getParameter("uid");
        String userCode = req.getParameter("userCode");
        System.out.println("    add userCode : " + userCode);
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");


        SmbmsUser user = new SmbmsUser();
        user.setId(id);//
        user.setUsercode(userCode);
        user.setUsername(userName);
        user.setUserpassword(userPassword);
        user.setAddress(address);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserrole((long) Integer.parseInt(userRole));
        user.setCreationdate(new Date());
        user.setCreatedby(((SmbmsUser) req.getSession().getAttribute("userSession")).getId());
        if (userServiceImpl.addUser(user)) {
            try {
                resp.sendRedirect(req.getContextPath() + "/jsp/useradd.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                req.getRequestDispatcher("useradd.jsp").forward(req, resp);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void query(HttpServletRequest req, HttpServletResponse resp) {
        //从前端获取数据 userlist.jsp
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");//值为0 、1、2、3
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;

        // 设置当前页和页面用户数量
        int currentPageNo = 1;
        int pageSize = 5;

        if (queryUserName == null) {
            queryUserName = "";
        }
        if (temp != null && !temp.equals("")) {
            queryUserRole = Integer.parseInt(temp);
        }
        if (pageIndex != null) {
            currentPageNo = Integer.parseInt(pageIndex);
        }
        System.out.println("queryUserName : " + queryUserName + ", queryUserRole : " + queryUserRole);
        int totalCount = userServiceImpl.getUserCount(queryUserName, queryUserRole);

        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);
        int totalPageCount = pageSupport.getTotalPageCount();

        //控制首页和尾页
        //如果页数小于1，就显示第一页  页数大于 最后一页就 显示最后一页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        // 获取用户列表展示，传给前端展示
        List<SmbmsUser> userList = userServiceImpl.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.getSession().setAttribute("userList", userList);
        for (SmbmsUser u : userList) {
            System.out.println(u);
        }
//        roleServiceImpl.getRolelist();
        List<SmbmsRole> roleList = roleServiceImpl.getRolelist();
        req.getSession().setAttribute("roleList", roleList);

        req.getSession().setAttribute("totalCount", totalCount);
        req.getSession().setAttribute("currentPageNo", currentPageNo);
        req.getSession().setAttribute("totalPageCount", totalPageCount);

        req.getSession().setAttribute("queryUserName", queryUserName);
        req.getSession().setAttribute("queryUserRole", queryUserRole);

        try {
            req.getRequestDispatcher("/jsp/userlist.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void view(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从前端获取 要修改的用户 的id
        String uid = req.getParameter("uid");

        if (uid != null && uid.length() != 0) {
            //查询要更改的用户信息
            SmbmsUser user = null;
            user = userServiceImpl.getUserById(uid);
            //将用户信息保存至 request中 让usermodify.jsp显示
            req.setAttribute("user", user);
            req.getRequestDispatcher("/jsp/userview.jsp").forward(req, resp);
        }
    }

    private void findById(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从前端获取 要修改的用户 的id
        String uid = req.getParameter("uid");

        if (uid != null && uid.length() != 0) {
            //查询要更改的用户信息
            SmbmsUser user = null;
            user = userServiceImpl.getUserById(uid);
            //将用户信息保存至 request中 让usermodify.jsp显示
            req.setAttribute("user", user);
            req.getRequestDispatcher("/jsp/usermodify.jsp").forward(req, resp);
        }
    }
}



