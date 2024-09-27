package it.airdesk.airdesk_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.airdesk.airdesk_app.model.auth.Credentials;
import it.airdesk.airdesk_app.service.auth.CredentialsService;

@ControllerAdvice
public class GlobalController { // Facade controller to handle index mapping globally

    @Autowired
    private CredentialsService credentialsService;

    @GetMapping("/")
    public String getIndex() {
        return "index.html";
    }

    // Returns the current logged-in user's details (either OIDC user or standard UserDetails)
    @ModelAttribute("userDetails")
    public Object getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();

            // Check if the principal is an OIDC user (OAuth2)
            if (principal instanceof DefaultOidcUser) {
                DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
                return oidcUser;  // You can also return specific attributes like oidcUser.getEmail()
            }
            // Check if the principal is a standard UserDetails user
            else if (principal instanceof UserDetails) {
                return (UserDetails) principal;
            }
        }
        return null;  // Return null if no user is authenticated
    }

//    @ModelAttribute("oidcUserInfo")
//    public OidcUserInfo getOidcUserInfo() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (!(authentication instanceof AnonymousAuthenticationToken)) {
//            Object principal = authentication.getPrincipal();
//            if (principal instanceof DefaultOidcUser) {
//                DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
//                OidcUserInfo oidcUserInfo = OidcUserInfo.builder() //returns new Builder()
//                    .givenName(oidcUser.getGivenName()) 
//                    .familyName(oidcUser.getFamilyName())
//                    .email(oidcUser.getEmail())
//                    .build();
//                return oidcUserInfo;
//            }
//        }
//        return null;
//    }

    // Returns the credentials of the logged-in user
    @ModelAttribute("credentials")
    public Credentials getCredentials() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();

            // Handle OIDC User
            if (principal instanceof DefaultOidcUser) {
                DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
                return credentialsService.findByUsername("USERNAMEof" + oidcUser.getEmail()).orElse(null);
            }
            // Handle standard UserDetails user
            else if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                return credentialsService.findByUsername(userDetails.getUsername()).orElse(null);
            }
        }
        return null;  // Return null if no credentials are available
    }
}
