package org.example.ecommerce.controller.customer.wallet;

import org.example.ecommerce.common.dto.promotion.CategoryDTO;
import org.example.ecommerce.common.dto.promotion.PromotionDTO;
import org.example.ecommerce.common.dto.shipping.AddressDTO;

import org.example.ecommerce.common.dto.shipping.ShippingRequest;
import org.example.ecommerce.entity.*;
import org.example.ecommerce.service.PromotionService;
import org.example.ecommerce.service.admin.OderLogService;
import org.example.ecommerce.service.customer.order.OderItemService;
import org.example.ecommerce.service.customer.order.OrderService;
import org.example.ecommerce.service.customer.order.ShippingService;
import org.example.ecommerce.service.customer.wallet.CartPreviewDTO;
import org.example.ecommerce.service.customer.wallet.PaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    private final PaymentService paymentService;
    private final PromotionService promotionService;
    private final ShippingService shippingService;
    private  final OrderService orderService;
    private final OderLogService oderLogService;
    private final OderItemService oderItemService;
    private final AddressDTO addressDTOs;

    public CheckoutController(PaymentService paymentService, PromotionService promotionService, ShippingService shippingService, OrderService orderService, OderLogService oderLogService, OderItemService oderItemService, AddressDTO addressDTOs) {
        this.paymentService = paymentService;
        this.promotionService = promotionService;
        this.shippingService = shippingService;
        this.orderService = orderService;
        this.oderLogService = oderLogService;
        this.oderItemService = oderItemService;
        this.addressDTOs = addressDTOs;
    }

    @PostMapping
    public String processCheckout(@RequestParam(value = "cartItemIds", required = false) List<Integer> cartItemIds,
                                  @ModelAttribute Customer customer,
                                  HttpSession session, Model model) {
        Customer customer1 = (Customer) session.getAttribute("customer");
        if (customer1 == null) {
            return "redirect:/login";
        }
        String result = paymentService.checkout(customer1, cartItemIds, customer.getFirstname() + " " + customer.getLastname(), customer.getPhone(), customer.getAddress());
        if ("Thanh toán thành công".equals(result)) {
            model.addAttribute("message", result);
            return "/customer/wallet/checkout-success";
        } else {
            model.addAttribute("error", result);
            return "/customer/wallet/checkout-fail";
        }
    }

    @PostMapping("/realtime")
    public String checkoutRealtime(@RequestParam Integer inventory, @RequestParam Integer quantity,
                                   @RequestParam(required = false) Integer freeshipId,
                                   @RequestParam(required = false) Integer discountId,
                                   @RequestParam(required = false) BigDecimal  totalPrice,

                                   @ModelAttribute Customer customer,
                                   HttpSession session, Model model) {
        Customer customer1 = (Customer) session.getAttribute("customer");
        if (customer1 == null) {
            return "redirect:/login";
        }
        String result = paymentService.checkoutRealtime(freeshipId,discountId,totalPrice,customer1, inventory, quantity, customer.getFirstname() + " " + customer.getLastname(), customer.getPhone(), customer.getAddress());
        if ("Thanh toán thành công".equals(result)) {
            model.addAttribute("message", result);
            return "/customer/wallet/checkout-success";
        } else {
            model.addAttribute("error", result);
            return "/customer/wallet/checkout-fail";
        }
    }

    @GetMapping("/preview")
    public String previewCheckout(
            @RequestParam List<Integer> cartItemIds,
            Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        List<CartPreviewDTO> preview = paymentService.getCheckoutPreview(customer, cartItemIds);
        model.addAttribute("customer", customer);
        model.addAttribute("items", preview);
        return "customer/wallet/checkout-preview";
    }

    @GetMapping("/preview_realtime")
    public String previewCheckoutRealtime(
            @RequestParam Integer inventory, @RequestParam Integer quantity,
            Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        CartPreviewDTO preview = paymentService.getCheckoutPreviewRealtime(customer, inventory, quantity);

        List<PromotionDTO> promotionDTOS = promotionService.getPromotions();
        Product product = paymentService.getProductById(inventory);
//        Inventory inventoryPayment = paymentService.getInventoryById(inventory);
        List<PromotionDTO> voucherDiscount = new ArrayList<>();
        List<PromotionDTO> voucherFreeShip = new ArrayList<>();
        for (PromotionDTO promotionDTO : promotionDTOS) {
            System.out.println("----------total promtion");
            System.out.println( promotionDTO.getName()+ "Per Usage limi"+  promotionDTO.getPerUserLimit()+ "----------Count promotion used------------" + oderLogService.countPromtionUsedByCustomerid(customer.getId(),promotionDTO.getId()));
            System.out.println(promotionDTO.getName()+"Usage limit" +  promotionDTO.getUsageLimit() + "Usage Count" + promotionDTO.getUsageCount());
            if (promotionDTO.getStatus().equalsIgnoreCase("ACTIVE")
                    && promotionDTO.getPerUserLimit() >= oderLogService.countPromtionUsedByCustomerid(customer.getId(),promotionDTO.getId())
                    && promotionDTO.getUsageLimit() >= promotionDTO.getUsageCount()
            ) {
                System.out.println(" --------------Active promotion");
                System.out.println( promotionDTO.getName()+ "Per Usage limi"+  promotionDTO.getPerUserLimit()+ "----------Count promotion used------------" + oderLogService.countPromtionUsedByCustomerid(customer.getId(),promotionDTO.getId()));
                System.out.println(promotionDTO.getName()+"Usage limit" +  promotionDTO.getUsageLimit() + "Usage Count" + promotionDTO.getUsageCount());
                for (CategoryDTO category : promotionDTO.getCategories()) {
                    Integer ID_OF_ALL_CATEGORY = 313;
                    if (promotionDTO.getType().equalsIgnoreCase("SHIPPING")
                            && (category.getValue().equals(ID_OF_ALL_CATEGORY) || (category.getValue().equals(product.getCategoryid().getId())))
                    ) {
                        voucherFreeShip.add(promotionDTO);
                    } else if (!promotionDTO.getType().equalsIgnoreCase("SHIPPING")
                            && (category.getValue().equals(ID_OF_ALL_CATEGORY) || (category.getValue().equals(product.getCategoryid().getId())))) {
                        List<Orderitem> list = oderItemService.getAllOrderItems();
                        if(promotionDTO.getId() == 17){
                            boolean checkNewUser = isCheckNewUser(list, customer);
                            if(!checkNewUser){
                                voucherDiscount.add(promotionDTO);
                            }
                        }else {
                            voucherDiscount.add(promotionDTO);
                        }



                    }


                }
            }
        }


        System.out.println("dis" + voucherDiscount);
        System.out.println("free" + voucherFreeShip);

        model.addAttribute("voucherDiscount", voucherDiscount);
        model.addAttribute("voucherFreeShip", voucherFreeShip);
        model.addAttribute("price", preview.getPrice().intValueExact() * preview.getQuantity());

        model.addAttribute("items", preview);
        model.addAttribute("customer", customer);
        return "customer/wallet/checkout-preview";
    }
    private static boolean isCheckNewUser(List<Orderitem> list, Customer customer) {
        boolean checkNewUser = false;
        for (Orderitem orderitem : list) {

            if(orderitem.getPromotionid() != null){
                if(orderitem.getOrderid().getCustomerid().getId().equals(customer.getId())
                        && (orderitem.getPromotionid() == 17)) {

                    checkNewUser = true;

                }
            }
        }
        return checkNewUser;
    }
//    @GetMapping("/preview_realtime_data")
//    @ResponseBody
//    public Map<String, Object> getPreviewData(
//            @RequestParam Integer inventory,
//            @RequestParam Integer quantity,
//            HttpSession session) {
//
//        Customer customer = (Customer) session.getAttribute("customer");
//        CartPreviewDTO preview = paymentService.getCheckoutPreviewRealtime(customer, inventory, quantity);
//
//        Product product = paymentService.getProductById(inventory);
//        Inventory inventoryPayment = paymentService.getInventoryById(inventory);
//
//        ShippingRequest shippingRequest = new ShippingRequest();
//        shippingRequest.setProvinceFrom(paymentService.getProvinceShopAddressById(product));
//        shippingRequest.setProvinceTo(addressDTOs.getProvinceName());
//        shippingRequest.setDistrictTo(addressDTOs.getDistrictName());
//        shippingRequest.setWeight(inventoryPayment.getWeight());
//        shippingRequest.setHeight(inventoryPayment.getHeight());
//        shippingRequest.setLength(inventoryPayment.getLength());
//        shippingRequest.setWidth(inventoryPayment.getWidth());
//        shippingRequest.setCategoryName(product.getCategoryid().getCategoryname());
//
//
//        System.out.println("From " + paymentService.getProvinceShopAddressById(product));
//        System.out.println("To " + addressDTOs.getProvinceName());
//        System.out.println("To district " + addressDTOs.getDistrictName());
//        System.out.println("-------"+ shippingRequest);
//        System.out.println("fee shipping" + shippingService.calculateShippingFee(shippingRequest));
//
//        BigDecimal feeShip = BigDecimal.valueOf(shippingService.calculateShippingFee(shippingRequest));
//        BigDecimal totalPrice = inventoryPayment.getPrice().add(feeShip).multiply(BigDecimal.valueOf(preview.getQuantity()));
//        Map<String, Object> response = new HashMap<>();
//        response.put("feeShip", feeShip);
//        response.put("priceTotal", totalPrice);
//        response.put("timeDelivery" , shippingService.timeDelivery(shippingRequest).toString());
//        return response;
//    }
@GetMapping("/preview_realtime_data")
@ResponseBody
public Map<String, Object> getPreviewData(
        @RequestParam Integer inventory,
        @RequestParam Integer quantity,
        @RequestParam(value = "freeshipId", required = false) Integer freeshipId,
        @RequestParam(value = "discountId", required = false) Integer discountId,
        HttpSession session) {

    System.out.println("Freeship ID: " + freeshipId);
    System.out.println("Discount ID: " + discountId);

    Customer customer = (Customer) session.getAttribute("customer");
    CartPreviewDTO preview = paymentService.getCheckoutPreviewRealtime(customer, inventory, quantity);

    Product product = paymentService.getProductById(inventory);
    Inventory inventoryPayment = paymentService.getInventoryById(inventory);
    ShippingRequest shippingRequest = new ShippingRequest();
    shippingRequest.setProvinceFrom(paymentService.getProvinceShopAddressById(product));
    shippingRequest.setProvinceTo(addressDTOs.getProvinceName());
    shippingRequest.setDistrictTo(addressDTOs.getDistrictName());
    shippingRequest.setCategoryName(product.getCategoryid().getCategoryname());
    if(product.getUseVariantShipping()){


        shippingRequest.setWeight(inventoryPayment.getWeight());
        shippingRequest.setHeight(inventoryPayment.getHeight());
        shippingRequest.setLength(inventoryPayment.getLength());
        shippingRequest.setWidth(inventoryPayment.getWidth());

    }else {
        shippingRequest.setWeight(product.getWeight());
        shippingRequest.setHeight(product.getHeight());
        shippingRequest.setLength(product.getLength());
        shippingRequest.setWidth(product.getWidth());
    }




    BigDecimal feeShip = BigDecimal.valueOf(shippingService.calculateShippingFee(shippingRequest));
    BigDecimal totalPrice = inventoryPayment.getPrice().multiply(BigDecimal.valueOf(preview.getQuantity())) .add(feeShip);
    BigDecimal feeShipVoucher = totalPrice;
    BigDecimal discountVoucher = BigDecimal.ZERO;
    BigDecimal feeShipDefault = feeShip;

    if(freeshipId != null) {
        Promotion promotion = promotionService.getPromotionById(freeshipId).orElseThrow();
        System.out.println("----"  + promotion);
        feeShip = feeShip.subtract(BigDecimal.valueOf(promotion.getValue()));
        if(feeShip.compareTo(BigDecimal.ZERO) <= 0) {
            feeShip = BigDecimal.ZERO;
        }
        totalPrice = inventoryPayment.getPrice().multiply(BigDecimal.valueOf(preview.getQuantity())).add(feeShip);
        feeShipVoucher = feeShipVoucher.subtract(totalPrice);
//        if(feeShipVoucher.compareTo(BigDecimal.ZERO) <= 0) {
//            feeShipVoucher = feeShip;
//        }
    }

    if (discountId != null ) {
        Promotion promotion = promotionService.getPromotionById(discountId).orElseThrow();
        System.out.println("----"  + promotion);
        if(promotion.getType().equalsIgnoreCase("PERCENTAGE")) {

            discountVoucher = totalPrice.multiply(BigDecimal.valueOf(promotion.getValue()/100));
            totalPrice = totalPrice.subtract(discountVoucher);
            if(discountVoucher.compareTo(BigDecimal.valueOf(promotion.getMaxDiscount())) > 0) {
                totalPrice = totalPrice.add(discountVoucher).subtract(BigDecimal.valueOf(promotion.getMaxDiscount()));
            }
        }else {
            discountVoucher = BigDecimal.valueOf(promotion.getValue());
            totalPrice = totalPrice.subtract(discountVoucher);

        }
//        if(freeshipId != null){
//            discountVoucher = discountVoucher.subtract(feeShipVoucher);
//        }
    }
    String timeDeli = shippingService.timeDelivery(shippingRequest).keySet() + " - " + shippingService.timeDelivery(shippingRequest).values();
    Map<String, Object> response = new HashMap<>();
    response.put("feeShip", feeShipDefault);
    response.put("priceTotal", totalPrice);
    response.put("timeDelivery", timeDeli);
    if(freeshipId != null) {
        response.put("feeShipVoucher", feeShipVoucher);
    }

    if (discountId != null ) {
        response.put("discountVoucher", discountVoucher);
    }
    return response;
}



    @PostMapping("/address")
    public ResponseEntity<String> receiveAddress(@RequestBody AddressDTO addressDTO) {
        String province = addressDTO.getProvinceName();
        String district = addressDTO.getDistrictName();
        addressDTOs.setProvinceName(addressDTO.getProvinceName());
        addressDTOs.setDistrictName(addressDTO.getDistrictName());
        System.out.println("Province: " + province + ", District: " + district);

        return ResponseEntity.ok("Địa chỉ đã nhận");
    }


}