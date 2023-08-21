package vn.lotusviet.hotelmgmt.core.annotation.security;

import org.springframework.security.access.annotation.Secured;
import vn.lotusviet.hotelmgmt.security.AuthorityConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Secured({AuthorityConstants.ADMIN, AuthorityConstants.RECEPTIONIST})
public @interface PortalSecured {
}