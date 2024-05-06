package member.service;

import java.util.Map;

/**
 * JoinService가 회원 가입 기능을 구현할 때 필요한 요청 데이터를 담는 클래스
 */
public class JoinRequest {
    private String id;
    private String name;
    private String password;
    private String confirmPassword;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public boolean isPasswordEqualToConfirm() {
        return password != null && password.equals(confirmPassword);
    }

    public void validate(Map<String, Boolean> errors) {
        checkEmpty(errors, id, "id");
        checkEmpty(errors, name, "name");
        checkEmpty(errors, password, "password");
        checkEmpty(errors, confirmPassword, "confirmPassword");
        if(!errors.containsKey("confirmPassword")) {
            if(!isPasswordEqualToConfirm()) {
                errors.put("notMatch", Boolean.TRUE);
            }
        }
    }

    private void checkEmpty(Map<String, Boolean> errors, String value, String filedName) {
        if(value == null || value.isEmpty()) {
            errors.put(filedName, Boolean.TRUE);
        }
    }
}
