package org.example.ecommerce.service.customer.order;

import org.example.ecommerce.common.dto.shipping.ShippingRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ShippingService {

    private static final Map<String, String> MAP_INTERNAL_DISTRICT = Map.<String, String>ofEntries(
            Map.entry("Hà Nội", "Hoàn Kiếm"),
            Map.entry("Hải Phòng", "Hồng Bàng"),
            Map.entry("Đà Nẵng", "Hải Châu"),
            Map.entry("Hồ Chí Minh", "Quận 1"),
            // Vùng Bắc:
            Map.entry("Vĩnh Phúc", "Vĩnh Yên"),
            Map.entry("Bắc Ninh", "Bắc Ninh"),
            Map.entry("Quảng Ninh", "Hạ Long"),
            Map.entry("Hải Dương", "Hải Dương"),
            Map.entry("Hưng Yên", "Hưng Yên"),
            Map.entry("Thái Bình", "Thái Bình"),
            Map.entry("Hà Nam", "Phủ Lý"),
            Map.entry("Nam Định", "Nam Định"),
            Map.entry("Ninh Bình", "Hoa Lư"),
            Map.entry("Hà Giang", "Hà Giang"),
            Map.entry("Cao Bằng", "Cao Bằng"),
            Map.entry("Bắc Kạn", "Bắc Kạn"),
            Map.entry("Tuyên Quang", "Tuyên Quang"),
            Map.entry("Lào Cai", "Lào Cai"),
            Map.entry("Yên Bái", "Yên Bái"),
            Map.entry("Thái Nguyên", "Thái Nguyên"),
            Map.entry("Lạng Sơn", "Lạng Sơn"),
            Map.entry("Bắc Giang", "Bắc Giang"),
            Map.entry("Phú Thọ", "Việt Trì"),
            Map.entry("Điện Biên", "Điện Biên Phủ"),
            Map.entry("Lai Châu", "Lai Châu"),
            Map.entry("Sơn La", "Sơn La"),
            Map.entry("Hòa Bình", "Hòa Bình"),
            Map.entry("Thanh Hóa", "Thanh Hóa"),
            Map.entry("Nghệ An", "Vinh"),
            Map.entry("Hà Tĩnh", "Hà Tĩnh"),
            Map.entry("Quảng Bình", "Đồng Hới"),
            Map.entry("Quảng Trị", "Đông Hà"),
            Map.entry("Quảng Nam", "Tam Kỳ"),
            Map.entry("Quảng Ngãi", "Quảng Ngãi"),
            Map.entry("Bình Định", "Quy Nhơn"),
            Map.entry("Phú Yên", "Tuy Hòa"),
            Map.entry("Khánh Hòa", "Nha Trang"),
            Map.entry("Ninh Thuận", "Phan Rang – Tháp Chàm"),
            Map.entry("Bình Thuận", "Phan Thiết"),
            Map.entry("Tây Ninh", "Tây Ninh"),
            Map.entry("Bình Phước", "Đồng Xoài"),
            Map.entry("Bình Dương", "Thủ Dầu Một"),
            Map.entry("Đồng Nai", "Biên Hòa"),
            Map.entry("Bà Rịa – Vũng Tàu", "Bà Rịa"),
            Map.entry("An Giang", "Long Xuyên"),
            Map.entry("Đồng Tháp", "Cao Lãnh"),
            Map.entry("Long An", "Tân An"),
            Map.entry("Tiền Giang", "Mỹ Tho"),
            Map.entry("Kiên Giang", "Rạch Giá"),
            Map.entry("Vĩnh Long", "Vĩnh Long"),
            Map.entry("Bến Tre", "Bến Tre"),
            Map.entry("Hậu Giang", "Vị Thanh"),
            Map.entry("Trà Vinh", "Trà Vinh"),
            Map.entry("Sóc Trăng", "Sóc Trăng"),
            Map.entry("Bạc Liêu", "Bạc Liêu"),
            Map.entry("Cà Mau", "Cà Mau"),
            Map.entry("Cần Thơ", "Ninh Kiều"),
            Map.entry("Huế", "Thuận Hóa"),
            Map.entry("Kon Tum", "Kon Tum"),
            Map.entry("Gia Lai", "Pleiku"),
            Map.entry("Đăk Lăk", "Buôn ma thuột"),
            Map.entry("Đăk Nông", "Gia Nghĩa"),
            Map.entry("Lâm Đồng", "Đà Lạt")



    );




    private static final Map<String, String> REGION_MAP = Map.<String, String>ofEntries(
            // Miền Bắc
            Map.entry("Hà Nội", "Bắc"),
            Map.entry("Bắc Giang", "Bắc"),
            Map.entry("Bắc Kạn", "Bắc"),
            Map.entry("Bắc Ninh", "Bắc"),
            Map.entry("Cao Bằng", "Bắc"),
            Map.entry("Điện Biên", "Bắc"),
            Map.entry("Hà Giang", "Bắc"),
            Map.entry("Hà Nam", "Bắc"),
            Map.entry("Hải Dương", "Bắc"),
            Map.entry("Hải Phòng", "Bắc"),
            Map.entry("Hòa Bình", "Bắc"),
            Map.entry("Hưng Yên", "Bắc"),
            Map.entry("Lai Châu", "Bắc"),
            Map.entry("Lạng Sơn", "Bắc"),
            Map.entry("Lào Cai", "Bắc"),
            Map.entry("Nam Định", "Bắc"),
            Map.entry("Ninh Bình", "Bắc"),
            Map.entry("Phú Thọ", "Bắc"),
            Map.entry("Quảng Ninh", "Bắc"),
            Map.entry("Sơn La", "Bắc"),
            Map.entry("Thái Bình", "Bắc"),
            Map.entry("Thái Nguyên", "Bắc"),
            Map.entry("Tuyên Quang", "Bắc"),
            Map.entry("Vĩnh Phúc", "Bắc"),
            Map.entry("Yên Bái", "Bắc"),

            // Miền Trung
            Map.entry("Thanh Hóa", "Trung"),
            Map.entry("Nghệ An", "Trung"),
            Map.entry("Hà Tĩnh", "Trung"),
            Map.entry("Quảng Bình", "Trung"),
            Map.entry("Quảng Trị", "Trung"),
            Map.entry("Thừa Thiên Huế", "Trung"),
            Map.entry("Đà Nẵng", "Trung"),
            Map.entry("Quảng Nam", "Trung"),
            Map.entry("Quảng Ngãi", "Trung"),
            Map.entry("Bình Định", "Trung"),
            Map.entry("Phú Yên", "Trung"),
            Map.entry("Khánh Hòa", "Trung"),
            Map.entry("Ninh Thuận", "Trung"),
            Map.entry("Bình Thuận", "Trung"),
            Map.entry("Đắk Lắk", "Trung"),
            Map.entry("Đắk Nông", "Trung"),
            Map.entry("Gia Lai", "Trung"),
            Map.entry("Kon Tum", "Trung"),
            Map.entry("Lâm Đồng", "Trung"),

            // Miền Nam
            Map.entry("Hồ Chí Minh", "Nam"),
            Map.entry("An Giang", "Nam"),
            Map.entry("Bà Rịa Vũng Tàu", "Nam"),
            Map.entry("Bạc Liêu", "Nam"),
            Map.entry("Bến Tre", "Nam"),
            Map.entry("Bình Dương", "Nam"),
            Map.entry("Bình Phước", "Nam"),
            Map.entry("Cà Mau", "Nam"),
            Map.entry("Cần Thơ", "Nam"),
            Map.entry("Đồng Nai", "Nam"),
            Map.entry("Đồng Tháp", "Nam"),
            Map.entry("Hậu Giang", "Nam"),
            Map.entry("Kiên Giang", "Nam"),
            Map.entry("Long An", "Nam"),
            Map.entry("Sóc Trăng", "Nam"),
            Map.entry("Tây Ninh", "Nam"),
            Map.entry("Tiền Giang", "Nam"),
            Map.entry("Trà Vinh", "Nam"),
            Map.entry("Vĩnh Long", "Nam")
    );

    public String getRegion(String province){
        province = province.trim();
        String standardizedProvince = province
                .replaceFirst("^(Thành phố|Tỉnh)\\s*", "")
                .trim();

        System.out.println("----------standardizedProvince: " + standardizedProvince + "province: " + province);
        return REGION_MAP.getOrDefault(standardizedProvince, "Khác");
    }





    public double calculateShippingFee(ShippingRequest request) {


        double volumetricWeight = (request.getLength() * request.getWidth() * request.getHeight()) / 6000.0;
        double chargeableWeight = Math.max(request.getWeight(), volumetricWeight);
        chargeableWeight = Math.ceil(chargeableWeight * 2) / 2.0; // làm tròn 0.5kg

        double basePrice = getBasePrice(request, chargeableWeight);

        if (isSpecialCategory(request.getCategoryName())) {
            basePrice += 150000;
        }

        if (chargeableWeight > 10) {
            basePrice *= 1.2;
        }

        return basePrice;
    }

    private String determineZone(String provincefrom, String provinceto) {
        String provinceFrom = provincefrom
                .replaceFirst("^( Thành phố|Tỉnh)\\s*", "")
                .trim();
        String provinceTo = provinceto
                .replaceFirst("^(Thành phố|Tỉnh)\\s*", "")
                .trim();

        if(provinceFrom.equals("Đà Nẵng") && provinceTo.equals("Hà Nội") ||
                provinceFrom.equals("Hà Nội") && provinceTo.equals("Đà Nẵng") ||
                provinceFrom.equals("Đà Nẵng") && provinceTo.equals("Hồ Chí Minh") ||
                provinceFrom.equals("Hồ Chí Minh") && provinceTo.equals("Đà Nẵng")
        ) {
            return "lien_trung_tam";
        }
        if(provinceFrom.equals("Hồ Chí Minh") && provinceTo.equals("Hà Nội") ||
                provinceFrom.equals("Hà Nội") && provinceTo.equals("Hồ Chí Minh")){
            return "lien_trung_tam_BN";
        }


        if (provinceFrom.contains(provinceTo)) {
           return "noi_tinh";
        }
        System.out.println("provinceFrom: " + provinceFrom + "provinceTO" + provinceTo);
        String regionFrom = REGION_MAP.getOrDefault(provinceFrom, "Khác");
        String regionTo = REGION_MAP.getOrDefault(provinceTo, "Khác");

        if (regionFrom.equals(regionTo)) {
            return "noi_mien";
        }

        if ((regionFrom.equals("Bắc") && regionTo.equals("Trung")) || (regionFrom.equals("Trung") && regionTo.equals("Bắc"))
                || (regionFrom.equals("Nam") && regionTo.equals("Trung")) || (regionFrom.equals("Trung") && regionTo.equals("Nam"))) {
            return "can_mien";
        }
        if (regionFrom.equals("Bắc") && regionTo.equals("Nam") || regionFrom.equals("Nam") && regionTo.equals("Bắc")) {
            return "lien_mien";
        }
        return "Khác";
    }



    private boolean isUrbanDistrict(String province, String district) {
        String standardizedProvince = province
                .replaceFirst("^(Thành phố|Tỉnh)\\s*", "")
                .trim();
        System.out.println("standardizedProvince: " + standardizedProvince + " district: " + district
        + "Get district" + MAP_INTERNAL_DISTRICT.getOrDefault(standardizedProvince,""));

        return district.contains(MAP_INTERNAL_DISTRICT.getOrDefault(standardizedProvince, ""));


    }

    private boolean isSpecialCategory(String categoryName) {
        String name = categoryName.toLowerCase();
        return name.contains("điện tử") || name.contains("điện thoại") || name.contains("laptop") || name.contains("máy ảnh");
    }

    private double getBasePrice(ShippingRequest request, double weight) {
        String zone = determineZone(request.getProvinceFrom(), request.getProvinceTo());
        System.out.println("-----Ở đâu :" + zone);
        System.out.println("Weight :" + weight);
        if(isUrbanDistrict(request.getProvinceTo(), request.getDistrictTo())) {
            System.out.println("Là nội thành");
            if (weight <= 0.5) {
                return switch (zone) {

                    case "noi_tinh" -> 22000;
                    case "noi_mien" -> 28000;
                    case "can_mien" -> 30000;
                    case "lien_mien" -> 31000;
                    default -> 29000;
                };
            } else if (weight <= 1) {
                return switch (zone) {
                    case "noi_tinh" -> 22000;
                    case "noi_mien" -> 30500;
                    case "can_mien" -> 35000;
                    case "lien_mien" -> 36000;
                    default -> 34000;
                };
            } else if (weight <= 1.5) {
                return switch (zone) {
                    case "noi_tinh" -> 22000;
                    case "noi_mien" -> 33000;
                    case "can_mien" -> 40000;
                    case "lien_mien" -> 41000;
                    default -> 39000;
                };
            } else if (weight <= 2) {
                return switch (zone) {
                    case "noi_tinh" -> 22000;
                    case "noi_mien" -> 35500;
                    case "can_mien" -> 45000;
                    case "lien_mien" -> 46000;
                    default -> 44000;
                };
            }else {
                double extraWeight = weight - 2.0;
                int extraSteps = (int) Math.ceil(extraWeight / 0.5);
                double stepCost = switch (zone) {
                    case "noi_tinh", "noi_mien" -> 6000;
                    case "lien_mien" ,"lien_trung_tam_BN" -> 12000;
                    default -> 8000;
                };
                return getBasePrice(request, 2.0) + (extraSteps * stepCost);
            }

        } else {
            System.out.println("Là ngoại thành");
            if (weight <= 0.5) {
                return switch (zone) {
                    case "noi_tinh" -> 25500;
                    case "noi_mien" -> 32500;
                    case "can_mien" -> 34500;
                    case "lien_mien" -> 36000;
                    default -> 33500;
                };
            } else if (weight <= 1) {
                return switch (zone) {
                    case "noi_tinh" -> 25500;
                    case "noi_mien" -> 35500;
                    case "can_mien" -> 40500;
                    case "lien_mien" -> 41500;
                    default -> 39000;
                };
            } else if (weight <= 1.5) {
                return switch (zone) {
                    case "noi_tinh" -> 25500;
                    case "noi_mien" -> 38000;
                    case "can_mien" -> 46000;
                    case "lien_mien" -> 48000;
                    default -> 45000;
                };
            } else if (weight <= 2) {
                return switch (zone) {
                    case "noi_tinh" -> 25500;
                    case "noi_mien" -> 41000;
                    case "can_mien" -> 52000;
                    case "lien_mien" -> 53000;
                    default -> 51000;
                };
            } else {
                double extraWeight = weight - 2.0;
                int extraSteps = (int) Math.ceil(extraWeight / 0.5);
                double stepCost = switch (zone) {
                    case "noi_tinh", "noi_mien" -> 7000;
                    case "lien_mien","lien_trung_tam_BN" -> 14000;
                    default -> 10000;
                };
                return getBasePrice(request, 2.0) + (extraSteps * stepCost);
            }
        }
    }

    public   Map<String,String> timeDelivery(ShippingRequest request){
        String zone = determineZone(
                request.getProvinceFrom(),

                request.getProvinceTo()

        );
        Map<String,String> timeDelivery = new HashMap<>();
        switch (zone) {
            case "noi_mien" -> timeDelivery.put(convertDate(LocalDateTime.now().plusDays(1)), convertDate(LocalDateTime.now().plusDays(2)));
            case "can_mien" -> timeDelivery.put(convertDate(LocalDateTime.now().plusDays(3)), convertDate(LocalDateTime.now().plusDays(4)));
            case "lien_mien" -> timeDelivery.put(convertDate(LocalDateTime.now().plusDays(4)), convertDate(LocalDateTime.now().plusDays(5)));
            default -> timeDelivery.put(convertDate(LocalDateTime.now().plusDays(1)), convertDate(LocalDateTime.now().plusDays(1)));
        }
        return timeDelivery;
    }

    private String convertDate(LocalDateTime dateTime){
        return dateTime.getDayOfMonth() + " Tháng " + dateTime.getMonth();
    }
}

