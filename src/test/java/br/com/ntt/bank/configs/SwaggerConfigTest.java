package br.com.ntt.bank.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class SwaggerConfigTest {

    private SwaggerConfig swaggerConfig;

    @BeforeEach
    void setUp() throws Exception {
        swaggerConfig = new SwaggerConfig();

        setPrivateField(swaggerConfig, "appName", "TestApp");
        setPrivateField(swaggerConfig, "appVersion", "1.0.0");
        setPrivateField(swaggerConfig, "appDescription", "Descrição do Teste");
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void shouldCreateOpenAPIBean() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();

        assertNotNull(openAPI);

        Info info = openAPI.getInfo();
        assertEquals("TestApp", info.getTitle());
        assertEquals("1.0.0", info.getVersion());
        assertEquals("Descrição do Teste", info.getDescription());

        SecurityScheme scheme = openAPI.getComponents().getSecuritySchemes().get("bearerAuth");
        assertNotNull(scheme);
        assertEquals(SecurityScheme.Type.HTTP, scheme.getType());
        assertEquals("bearer", scheme.getScheme());
        assertEquals("JWT", scheme.getBearerFormat());
    }
}
