package web.models.role;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN, USER;

    @Override
    public String getAuthority() {
        return name();
    }
}

//    public Role getRole(String role){
//        if (role.equalsIgnoreCase("ADMIN")){
//            return ADMIN;
//        }
//        else return USER;
//    }
//}
