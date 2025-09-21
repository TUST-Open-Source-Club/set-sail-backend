package com.tustosc.setsail.Utils;

import com.tustosc.setsail.Entiy.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        jwtUtils = new JwtUtils();
        Field f=jwtUtils.getClass().getDeclaredField("token");
        f.setAccessible(true);
        f.set(jwtUtils, "testjwt");
    }

    @Test
    void jwtBuilderAndVerify() throws Exception {
        List<String> privileges=List.of(Role.admin().toString(),Role.user().toString());
        String jwt = jwtUtils.jwtBuilder(UUID.randomUUID().toString().replace("-", ""), privileges);
        assertNotNull(jwt);
        String[] parts = jwt.split("\\.");
        String header = parts[0];
        String payload = parts[1];
        String sign = parts[2];
        assertTrue(jwt.startsWith(header));
        assertNotNull(payload);
        assertNotNull(sign);
        assertTrue(jwtUtils.verifyJwt(jwt));
    }

    @Test
    void build() throws Exception {
        List<String> privileges=List.of(Role.admin().toString(),Role.user().toString());
        String jwt = jwtUtils.builder()
                .id(UUID.randomUUID().toString())
                .role(Role.admin())
                .build();
        assertNotNull(jwt);
        String[] parts = jwt.split("\\.");
        String header = parts[0];
        String payload = parts[1];
        String sign = parts[2];
        assertTrue(jwt.startsWith(header));
        assertNotNull(payload);
        assertNotNull(sign);
        assertTrue(jwtUtils.verifyJwt(jwt));
    }

    @Test
    void getUuidFromJwt() throws Exception {
        List<String> privileges=List.of(Role.user().toString());
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String jwt = jwtUtils.jwtBuilder(uuid, privileges);
        String uuidFromJwt = jwtUtils.getUuidFromJwt(jwt);
        assertEquals(uuid, uuidFromJwt);
    }

    @Test
    void getRoleFromJwt() throws Exception {
        List<String> privileges=List.of(Role.user().toString());
        String uuid = UUID.randomUUID().toString();
        String jwt = jwtUtils.jwtBuilder(uuid, privileges);
        List<Role> roles = jwtUtils.getRoleFromJwt(jwt);
        List<String> rolesFromJwt = roles.stream().map(Role::toString).toList();
        assertEquals(privileges, rolesFromJwt);
    }

}