package com.example.manhvdse61952.vrc_android.controller.resources;

public class ImmutableValue {
    //Role of app
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_OWNER = "ROLE_OWNER";

    //Vehicle type
    public static final String XE_MAY = "XE_MAY";
    public static final String XE_CA_NHAN = "XE_CA_NHAN";
    public static final String XE_DU_LICH = "XE_DU_LICH";

    //Contract status
    public static final String CONTRACT_INACTIVE = "INACTIVE";
    public static final String CONTRACT_ACTIVE = "ACTIVE";
    public static final String CONTRACT_PENDING = "PENDING";
    public static final String CONTRACT_PRE_FINISHED = "PRE_FINISHED";
    public static final String CONTRACT_FINISHED = "FINISHED";
    public static final String CONTRACT_ISSUE = "ISSUE";
    public static final String CONTRACT_REFUNDED = "REFUNDED";

    ///// SHARED PREFERENCES CODE and value of it /////
    //Sign up information for customer
    public static final String SIGNUP_SHARED_PREFERENCES_CODE = "VRS_SIGNUP";
    public static final String SIGNUP_username = "signup_username";
    public static final String SIGNUP_password = "signup_password";
    public static final String SIGNUP_email = "signup_email";
    public static final String SIGNUP_fullName = "signup_fullName";
    public static final String SIGNUP_phone = "signup_phone";
    public static final String SIGNUP_img_CMND = "signup_imgCmndPath";
    public static final String SIGNUP_cmnd = "signup_cmnd";
    public static final String SIGNUP_role = "signup_role";

    //Sign up information for vehicle
    public static final String VEHICLE_vehicleType = "vehicle_type";
    public static final String VEHICLE_vehicleMaker = "vehicle_maker";
    public static final String VEHICLE_vehicleModel = "vehicle_model";
    public static final String VEHICLE_img_vehicle_1 = "vehicle_img_path1";
    public static final String VEHICLE_img_vehicle_2 = "vehicle_img_path2";
    public static final String VEHICLE_img_frameNumber = "vehicle_img_frameNumber_path";
    public static final String VEHICLE_year = "vehicle_year";
    public static final String VEHICLE_frameNumber = "vehicle_frameNumber";
    public static final String VEHICLE_plateNumber = "vehicle_plateNumber";
    public static final String VEHICLE_city = "vehicle_city";
    public static final String VEHICLE_district = "vehicle_district";
    public static final String VEHICLE_rentFeePerHours = "vehicle_rentFeePerHours";
    public static final String VEHICLE_rentFeePerDay = "vehicle_rentFeePerDay";
    public static final String VEHICLE_depositFee = "vehicle_depositFee";
    public static final String VEHICLE_isGasoline = "vehicle_isGasoline";
    public static final String VEHICLE_isManual = "vehicle_isManual";
    public static final String VEHICLE_requireHouseHold = "vehicle_requireHouseHold";
    public static final String VEHICLE_requireIdCard = "vehicle_requireIdCard";
    public static final String VEHICLE_informationID = "vehicle_informationID";
    public static final String VEHICLE_districtID = "vehicle_districtID";
    public static final String VEHICLE_address = "vehicle_address";

    //Home information value
    public static final String HOME_SHARED_PREFERENCES_CODE = "VRS_HOME";
    public static final String HOME_username = "home_username";
    public static final String HOME_userID = "home_userID";
    public static final String HOME_accessToken = "home_accessToken";
    public static final String HOME_role = "home_role";
    public static final String HOME_fullName = "home_fullName";
    public static final String HOME_tabIndex = "home_tabIndex";

    //In-app information value
    public static final String MAIN_SHARED_PREFERENCES_CODE = "VRS_MAIN";
    public static final String MAIN_contractID = "main_contractID";
    public static final String MAIN_contractStatus = "main_contractStatus";
    public static final String MAIN_ownerID = "main_ownerID";
    public static final String MAIN_customerID = "main_customerID";
    public static final String MAIN_vehicleID = "main_vehicleID";
    public static final String MAIN_vehicleSeat = "main_vehicleSeat";
    public static final String MAIN_vehicleType = "main_vehicleType";
    public static final String MAIN_vehicleName = "main_vehicleName";
    public static final String MAIN_vehicleImgFront = "main_vehicleImgFront";
    public static final String MAIN_vehicleImgBack = "main_vehicleImgBack";
    public static final String MAIN_startHour = "main_startHour";
    public static final String MAIN_endHour = "main_endHour";
    public static final String MAIN_startMinute = "main_startMinute";
    public static final String MAIN_endMinute = "main_endMinute";
    public static final String MAIN_startDate = "main_startDate";
    public static final String MAIN_endDate = "main_endDate";
    public static final String MAIN_totalDay = "main_totalDay";
    public static final String MAIN_totalHour = "main_totalHour";
    public static final String MAIN_totalMinute = "main_totalMinute";
    public static final String MAIN_totalMoney = "main_totalMoney";
    public static final String MAIN_rentFeeMoney = "main_rentFeeMoney";
    public static final String MAIN_rentFeePerDayID = "main_rentFeePerDayID";
    public static final String MAIN_rentFeePerHourID = "main_rentFeePerHourID";
    public static final String MAIN_receiveType = "main_receiveType";
    public static final String MAIN_startDayLong = "main_startDayLong";
    public static final String MAIN_endDayLong = "main_endDayLong";

    public static final String changeFeeMessageBody = "Chủ xe đã thay đổi phí. Vui lòng kiểm tra";
}
