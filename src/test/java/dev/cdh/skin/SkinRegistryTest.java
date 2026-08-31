package dev.cdh.skin;

import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class SkinRegistryTest {

    @Test
    void fromPropertiesParsesSkinsAndDisplayNames() {
        Properties props = new Properties();
        props.setProperty("skins", "calico_cat, grey_tabby_cat");
        props.setProperty("skin.calico_cat.name", "Calico Cat");

        SkinRegistry registry = SkinRegistry.fromProperties(props);

        assertEquals(2, registry.all().size());
        assertEquals("calico_cat", registry.all().get(0).id());
        assertEquals("Calico Cat", registry.all().get(0).displayName());
        // 未配置显示名时回退为皮肤 id
        assertEquals("grey_tabby_cat", registry.all().get(1).displayName());
    }

    @Test
    void randomSkinAlwaysReturnsRegisteredSkin() {
        Properties props = new Properties();
        props.setProperty("skins", "calico_cat,orange_cat");
        SkinRegistry registry = SkinRegistry.fromProperties(props);

        for (int i = 0; i < 100; i++) {
            assertTrue(registry.all().contains(registry.randomSkin()));
        }
    }

    @Test
    void emptySkinsThrows() {
        assertThrows(IllegalStateException.class, () -> SkinRegistry.fromProperties(new Properties()));
    }

    @Test
    void skinResourceRootEqualsId() {
        CatSkin skin = new CatSkin("white_cat", "White Cat");
        assertEquals("white_cat", skin.resourceRoot());
    }
}
