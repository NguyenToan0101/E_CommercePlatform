package org.example.ecommerce.config;

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ecommerce.service.admin.VisitService;
import org.springframework.web.servlet.HandlerInterceptor;

public class VisitInterceptor implements HandlerInterceptor {

    private final VisitService visitService;

    public VisitInterceptor(VisitService visitService) {
        this.visitService = visitService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();

        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        DeviceType deviceType = userAgent.getOperatingSystem().getDeviceType();
        String device = "";
        if (deviceType == DeviceType.MOBILE) {
            System.out.println("Người dùng đang dùng Mobile");
            device = "mobile";
        } else if (deviceType == DeviceType.TABLET) {
            System.out.println("Người dùng đang dùng Tablet");
            device = "tablet";
        } else if (deviceType == DeviceType.COMPUTER) {
//            System.out.println("Người dùng đang dùng Desktop");
            device = "desktop";
        }
        if ("/home".equals(path)) {
            visitService.logVisit(path, device);
        }
        if ("/detailproduct".equals(path)) {
            visitService.logVisit(path, device);
        }
        if ("/checkout/preview_realtime".equals(path)) {
            visitService.logVisit(path, device);
        }
        if ("/cart".equals(path)) {
            visitService.logVisit(path, device);
        }
        if ("/checkout/realtime".equals(path)) {
            visitService.logVisit(path, device);
        }

        return true;
    }
}

