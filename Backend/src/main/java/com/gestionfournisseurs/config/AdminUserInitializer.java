package com.gestionfournisseurs.config;

import com.gestionfournisseurs.entity.AppRole;
import com.gestionfournisseurs.entity.AppUser;
import com.gestionfournisseurs.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminUserInitializer implements ApplicationRunner {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.security.admin-username:admin}")
    private String adminUsername;

    @Value("${app.security.admin-password:admin123}")
    private String adminPassword;

    public AdminUserInitializer(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (appUserRepository.findByUsername(adminUsername).isPresent()) {
            return;
        }
        AppUser admin = new AppUser();
        admin.setUsername(adminUsername);
        admin.setPasswordHash(passwordEncoder.encode(adminPassword));
        admin.setRole(AppRole.ADMIN);
        appUserRepository.save(admin);
    }
}
