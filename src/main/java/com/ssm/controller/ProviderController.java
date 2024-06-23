package com.ssm.controller;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.ssm.pojo.SmbmsProvider;
import com.ssm.pojo.SmbmsUser;
import com.ssm.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class ProviderController {
    @Autowired
    ProviderService providerService;
    @GetMapping("/provider")
    public void provider(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        if (method != null && method.equals("query")) {
            this.query(request, response);
        }else if(method != null && method.equals("view")){
            this.getProviderById(request,response,"providerview.jsp");
        }else if(method != null && method.equals("modify")){
            this.getProviderById(request,response,"providermodify.jsp");
        }else if(method != null && method.equals("delprovider")){
            this.delProvider(request,response);
        }
    }
    @PostMapping("/providerAdd")
    private void add(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long id = Long.parseLong(request.getParameter("id"));//id
        String proCode = request.getParameter("proCode");
        String proName = request.getParameter("proName");
        String proContact = request.getParameter("proContact");
        String proPhone = request.getParameter("proPhone");
        String proAddress = request.getParameter("proAddress");
        String proFax = request.getParameter("proFax");
        String proDesc = request.getParameter("proDesc");

        SmbmsProvider provider = new SmbmsProvider();
        provider.setId(id);//id
        provider.setProcode(proCode);
        provider.setProname(proName);
        provider.setProcontact(proContact);
        provider.setProphone(proPhone);
        provider.setProfax(proFax);
        provider.setProaddress(proAddress);
        provider.setProdesc(proDesc);
//        provider.setCreatedby(((SmbmsUser)request.getSession().getAttribute("userSession")).getId());
        provider.setCreationdate(new Date());
        boolean flag = false;
        flag = providerService.add(provider);
        if(flag){
            response.sendRedirect(request.getContextPath()+"/provider?method=query");
        }else{
            request.getRequestDispatcher("/jsp/provideradd.jsp").forward(request, response);
        }
    }
    private void delProvider(HttpServletRequest request, HttpServletResponse response)
            throws  IOException {
        String id = request.getParameter("proid");
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(!StringUtils.isNullOrEmpty(id)){
            int flag = providerService.deleteProviderById(id);
            if(flag == 0){//删除成功
                resultMap.put("delResult", "true");
            }else if(flag == -1){//删除失败
                resultMap.put("delResult", "false");
            }else if(flag > 0){//该供应商下有订单，不能删除，返回订单数
                resultMap.put("delResult", String.valueOf(flag));
            }
        }else{
            resultMap.put("delResult", "notexit");
        }
        //把resultMap转换成json对象输出
        response.setContentType("application/json");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }
    @PostMapping("/modify")
    private void modify(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        String proContact = request.getParameter("proContact");
        String proName = request.getParameter("proName");
        String proPhone = request.getParameter("proPhone");
        String proAddress = request.getParameter("proAddress");
        String proFax = request.getParameter("proFax");
        String proDesc = request.getParameter("proDesc");

        SmbmsProvider provider = new SmbmsProvider();
        provider.setId((long) Integer.parseInt(id));
        provider.setProcontact(proContact);
        provider.setProphone(proPhone);
        provider.setProfax(proFax);
        provider.setProname(proName);
        provider.setProaddress(proAddress);
        provider.setProdesc(proDesc);
        provider.setModifyby(((SmbmsUser)request.getSession().getAttribute("userSession")).getId());
        provider.setModifydate(new Date());
        boolean flag = false;
        flag = providerService.modify(provider);
        if(flag){
            System.out.println("flag is : "  + flag);
            response.sendRedirect(request.getContextPath()+"/provider?method=query");
        }else{
            request.getRequestDispatcher("/jsp/providermodify.jsp").forward(request, response);
        }
    }
    private void query(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String queryProName = request.getParameter("queryProName");
        String queryProCode = request.getParameter("queryProCode");
        if(StringUtils.isNullOrEmpty(queryProName)){
            queryProName = "";
        }
        if(StringUtils.isNullOrEmpty(queryProCode)){
            queryProCode = "";
        }
        List<SmbmsProvider> providerList = new ArrayList<SmbmsProvider>();
        providerList = providerService.getProviderList(queryProName,queryProCode);
        request.setAttribute("providerList", providerList);
        request.setAttribute("queryProName", queryProName);
        request.setAttribute("queryProCode", queryProCode);
        request.getRequestDispatcher("/jsp/providerlist.jsp").forward(request, response);
    }
    private void getProviderById(HttpServletRequest request, HttpServletResponse response,String url)
            throws ServletException, IOException {
        String id = request.getParameter("proid");
        if(!StringUtils.isNullOrEmpty(id)){
            SmbmsProvider provider = null;
            provider = providerService.getProviderById(id);
            request.setAttribute("provider", provider);
            request.getRequestDispatcher("/jsp/"+url).forward(request, response);
        }
    }
}






