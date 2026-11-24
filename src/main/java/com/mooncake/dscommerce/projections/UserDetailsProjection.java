package com.mooncake.dscommerce.projections;

public interface UserDetailsProjection {

    Long getId();
    String getUsername();
    String getPassword();
    Long getRoleId();
    String getAuthority();
}
