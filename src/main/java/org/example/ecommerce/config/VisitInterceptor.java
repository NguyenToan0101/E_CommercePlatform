package org.example.ecommerce.config;

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.service.admin.VisitService;
import org.example.ecommerce.service.customer.recommenderSystem.RecommenderSystemService;
import org.example.ecommerce.service.seller.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

public class VisitInterceptor implements HandlerInterceptor {

    private  VisitService visitService;



    public VisitInterceptor(VisitService visitService) {
        this.visitService = visitService;


    }



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();
        String queryString = request.getQueryString();
        Integer productid = null;


        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        DeviceType deviceType = userAgent.getOperatingSystem().getDeviceType();
        String device = "";
        HttpSession session = request.getSession();
        Customer customer =  (Customer) session.getAttribute("customer");
        Product product = null;
        if (deviceType == DeviceType.MOBILE) {

            device = "mobile";
        } else if (deviceType == DeviceType.TABLET) {

            device = "tablet";
        } else if (deviceType == DeviceType.COMPUTER) {

            device = "desktop";
        }
        if ("/home".equals(path)) {
            visitService.logVisit(path, device);
        }
        if ("/detailproduct".equals(path)) {
            visitService.logVisit(path, device);
            productid = queryString != null ? Integer.parseInt(queryString.replaceFirst("id=","")) : null;
            product = visitService.getProduct(productid);
            if (customer != null) {

                visitService.add(customer,product,1);
            }
            session.setAttribute("product", product);
        }
        if ("/checkout/preview_realtime".equals(path)) {
            visitService.logVisit(path, device);
            if (customer != null) {

                visitService.add(customer,(Product) session.getAttribute("product"),3);
            }

        }
        if ("/cart".equals(path)) {
            visitService.logVisit(path, device);
        }
        if ("/checkout/realtime".equals(path)) {
            visitService.logVisit(path, device);
            if (customer != null) {

                visitService.add(customer,(Product) session.getAttribute("product"),5);
            }
        }
        if (queryString != null) {
            path += "?" + queryString;
            if (customer != null) {
                System.out.println("Path" + path);
            }
        }

        return true;
    }
}

