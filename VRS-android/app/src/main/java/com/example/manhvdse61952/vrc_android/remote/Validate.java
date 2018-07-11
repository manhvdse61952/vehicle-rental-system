package com.example.manhvdse61952.vrc_android.remote;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Validate {
    public Boolean validUsername(String username, EditText input){
        Boolean isValid = true;
        if (username.trim().isEmpty()){
            isValid = false;
            input.setError("Tài khoản không được để trống");
        } else if(username.trim().matches("[a-zA-Z0-9\\.\\_\\@]{3,50}$") == false){
            isValid = false;
            input.setError("Tài khoản từ 3-50 kí tự, chỉ được chứa chữ, số và 3 kí tự đặc biệt _ @ .");
        }
        else{
            isValid = true;
        }
        return isValid;
    }

    public Boolean validPassword(String password, EditText input){
        Boolean isValid = true;
        if (password.trim().isEmpty()){
            isValid = false;
            input.setError("Mật khẩu không được để trống");
        } else if(password.trim().matches("[a-zA-Z0-9\\@]{3,50}$") == false){
            isValid = false;
            input.setError("Mật khẩu từ 3-50 kí tự, chỉ được chứa chữ, số và @");
        } else {
            isValid = true;
        }
        return isValid;
    }

    public Boolean validEmail(String email, EditText input){
        Boolean isValid = true;
        if (email.trim().isEmpty()){
            isValid = false;
            input.setError("Email không được để trống");
        } else if(email.trim().matches("\\w+@\\w+[.]\\w+([.]\\w+)?") == false){
            isValid = false;
            input.setError("Email sai định dạng. Ví dụ đúng: example@gmail.com");
        } else {
            isValid = true;
        }
        return isValid;
    }

    public Boolean validName(String name, EditText input){
        Boolean isValid = true;
        if (name.trim().isEmpty()){
            isValid = false;
            input.setError("Tên không được để trống");
        } else if (name.trim().matches("[a-zA-Z\\s]{3,}$") == false){
            isValid = false;
            input.setError("Tên chỉ gồm chữ cái, khoảng trắng, không có dấu và từ 3 kí tự trở lên");
        } else {
            isValid = true;
        }
        return isValid;
    }

    public Boolean validVehicleName(String name, Context ctx){
        Boolean isValid = true;
        if (name.trim().isEmpty()){
            isValid = false;
            Toast.makeText(ctx, "Vui lòng chọn tên xe", Toast.LENGTH_SHORT).show();
        }
        return isValid;
    }

    public Boolean validPhone(String phone, EditText input){
        Boolean isValid = true;
        if (phone.trim().isEmpty()){
            isValid = false;
            input.setError("Số điện thoại không được để trống");
        } else if(phone.trim().matches("[0-9]{6,15}$") == false){
            isValid = false;
            input.setError("Số điện thoại chỉ chứa từ 6 đến 15 số");
        } else {
            isValid = true;
        }
        return isValid;
    }

    public Boolean validCMND(String cmnd, EditText input){
        Boolean isValid = true;
        if (cmnd.trim().isEmpty()){
            isValid = false;
            input.setError("Số CMND không được để trống");
        } else if(cmnd.trim().matches("[0-9]{5,50}$") == false){
            isValid = false;
            input.setError("Sai định dạng CMND");
        } else {
            isValid = true;
        }
        return isValid;
    }

    public Boolean validImageLink(String link, Context ctx){
        Boolean isValid = true;
        if (link.trim().isEmpty()){
            isValid = false;
            Toast.makeText(ctx, "Yêu cầu chụp ảnh", Toast.LENGTH_SHORT).show();
        }
        return isValid;
    }


    public Boolean validPrice(String price, EditText input){
        Boolean isValid = true;
        if (price.trim().isEmpty()){
            isValid = false;
            input.setError("Tiền không được để trống");
        } else if (price.trim().matches("[0-9]{5,}") == false){
            isValid = false;
            input.setError("Tiền chỉ chứa số và từ 10.000 trở lên");
        } else {
            isValid = true;
        }
        return isValid;
    }

    public Boolean validFrameNumber(String frame, EditText input){
        Boolean isValid = true;
        if (frame.trim().isEmpty()){
            isValid = false;
            input.setError("Số khung không được để trống");
        } else if(frame.trim().matches("[0-9a-zA-Z\\-\\.]{2,}") == false){
            isValid = false;
            input.setError("Số khung chỉ chứa chữ, số, -, .");
        } else {
            isValid = true;
        }
        return isValid;
    }

    public String removeAccentCharacter(String str){
        str = str.toLowerCase();
        str = str.replaceAll("[áàảãạâấầẩẫậăắằẳẵặ]","a");
        str = str.replaceAll("đ","d");
        str = str.replaceAll("[éèẻẽẹêếềểễệ]","e");
        str = str.replaceAll("[íìỉĩị]","i");
        str = str.replaceAll("[óòỏõọôốồổỗộơớờởỡợ]","o");
        str = str.replaceAll("[úùủũụưứừửữự]","u");
        return str;
    }
}
