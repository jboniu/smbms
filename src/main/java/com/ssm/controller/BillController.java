package com.ssm.controller;
//
import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.ssm.pojo.SmbmsBill;
import com.ssm.pojo.SmbmsProvider;
import com.ssm.pojo.SmbmsUser;
import com.ssm.service.BillService;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class BillController {
    @Autowired
    ProviderService providerService;
    @Autowired
    BillService billService;

    @GetMapping("/bill")
    public void Bill(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        if (method != null && method.equals("query")) {
            this.query(request, response);
        } else if (method != null && method.equals("view")) {
            this.getBillById(request, response, "/jsp/billview.jsp");
        } else if (method != null && method.equals("modify")) {
            this.getBillById(request, response, "/jsp/billmodify.jsp");
        } else if (method != null && method.equals("getproviderlist")) {
            this.getProviderlist(request, response);
        } else if (method != null && method.equals("delbill")) {
            this.delBill(request, response);
        }
    }

    private void delBill(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("billid");
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (!StringUtils.isNullOrEmpty(id)) {
            boolean flag = billService.deleteBillById(id);
            if (flag) {//删除成功
                resultMap.put("delResult", "true");
            } else {//删除失败
                resultMap.put("delResult", "false");
            }
        } else {
            resultMap.put("delResult", "notexit");
        }
        //把resultMap转换成json对象输出
        response.setContentType("application/json");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    @PostMapping("/billSave")
    private void modify(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("modify===============");
        String id = request.getParameter("id");
        String billCode = request.getParameter("billCode");
        String productName = request.getParameter("productName");
        String productdesc = request.getParameter("productdesc");
        String productUnit = request.getParameter("productUnit");
        String productCount = request.getParameter("productCount");
        String totalPrice = request.getParameter("totalPrice");
        String providerId = request.getParameter("providerId");
        String isPayment = request.getParameter("isPayment");

        SmbmsBill bill = new SmbmsBill();
        bill.setId((long) Integer.parseInt(id));
        bill.setBillcode(billCode);
        bill.setProductname(productName);
        bill.setProductdesc(productdesc);
        bill.setProductunit(productUnit);
        bill.setProductcount(new BigDecimal(productCount).setScale(2, BigDecimal.ROUND_DOWN));
        bill.setIspayment(Integer.parseInt(isPayment));
        bill.setTotalprice(new BigDecimal(totalPrice).setScale(2, BigDecimal.ROUND_DOWN));
        bill.setProviderid((long) Integer.parseInt(providerId));

        bill.setModifyby(((SmbmsUser) request.getSession().getAttribute("userSession")).getId());
        bill.setModifydate(new Date());
        boolean flag = false;
        flag = billService.modify(bill);
        if (flag) {
            response.sendRedirect(request.getContextPath() + "/bill?method=query");
        } else {
            request.getRequestDispatcher("/jsp/billmodify.jsp").forward(request, response);
        }
    }

    private void getProviderlist(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("getproviderlist ========================= ");
        List<SmbmsProvider> providerList = new ArrayList<>();
        providerList = providerService.getProviderList("", "");
        System.out.println(providerList);
        //把providerList转换成json对象输出
        response.setContentType("application/json");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(providerList));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    @PostMapping("/billAdd")
    public void billAdd(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        long id = Long.parseLong(request.getParameter("id"));//id1
        String billCode = request.getParameter("billCode");
        String productName = request.getParameter("productName");
        String productDesc = request.getParameter("productDesc");
        String productUnit = request.getParameter("productUnit");

        String productCount = request.getParameter("productCount");
        String totalPrice = request.getParameter("totalPrice");
        String providerId = request.getParameter("providerId");
        String isPayment = request.getParameter("isPayment");

        SmbmsBill bill = new SmbmsBill();
        bill.setId(id);//id2
        bill.setBillcode(billCode);
        bill.setProductname(productName);
        bill.setProductdesc(productDesc);
        bill.setProductunit(productUnit);
        bill.setProductcount(new BigDecimal(productCount).setScale(2, BigDecimal.ROUND_DOWN));
        bill.setIspayment(Integer.parseInt(isPayment));
        bill.setTotalprice(new BigDecimal(totalPrice).setScale(2, BigDecimal.ROUND_DOWN));
        bill.setProviderid((long) Integer.parseInt(providerId));
        bill.setCreatedby(((SmbmsUser) request.getSession().getAttribute("userSession")).getId());
        bill.setCreationdate(new Date());
        boolean flag = false;
        flag = billService.add(bill);
        System.out.println("add flag -- > " + flag);
        if (flag) {
            response.sendRedirect(request.getContextPath() + "/bill?method=query");
        } else {
            request.getRequestDispatcher("/jsp/billadd.jsp").forward(request, response);
        }
    }

    private void getBillById(HttpServletRequest request, HttpServletResponse response, String url)
            throws ServletException, IOException {
        String id = request.getParameter("billid");
        if (!StringUtils.isNullOrEmpty(id)) {
            SmbmsBill bill = null;
            bill = billService.getBillById(id);
            request.setAttribute("bill", bill);
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    private void query(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<SmbmsProvider> providerList = new ArrayList<SmbmsProvider>();
        providerList = providerService.getProviderList("", "");
        request.setAttribute("providerList", providerList);

        String queryProductName = request.getParameter("queryProductName");
        String queryProviderId = request.getParameter("queryProviderId");
        String queryIsPayment = request.getParameter("queryIsPayment");
        if (StringUtils.isNullOrEmpty(queryProductName)) {
            queryProductName = "";
        }

        List<SmbmsBill> billList = new ArrayList<SmbmsBill>();
        SmbmsBill bill = new SmbmsBill();
        if (StringUtils.isNullOrEmpty(queryIsPayment)) {
            bill.setIspayment(0);
        } else {
            bill.setIspayment(Integer.parseInt(queryIsPayment));
        }

        if (StringUtils.isNullOrEmpty(queryProviderId)) {
            bill.setProviderid(0L);
        } else {
            bill.setProviderid(Long.valueOf(queryProviderId));
        }
        bill.setProductname(queryProductName);
        billList = billService.getBillList(bill);
        System.out.println("=========================================");
        System.out.println(billList);
        request.setAttribute("billList", billList);
        request.setAttribute("queryProductName", queryProductName);
        request.setAttribute("queryProviderId", queryProviderId);
        request.setAttribute("queryIsPayment", queryIsPayment);
        request.getRequestDispatcher("/jsp/billlist.jsp").forward(request, response);

    }
}

