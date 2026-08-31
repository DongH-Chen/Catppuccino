package dev.cdh.skin;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.random.RandomGenerator;

/**
 * 皮肤注册表：从外部配置（{@code resources/skins.properties}）发现可用皮肤，
 * 避免皮肤清单硬编码在代码中。新增皮肤只需放入资源目录并在配置中注册一行，
 * 无需修改任何代码。
 *
 * <p>若要改为其他皮肤来源（如目录扫描、ServiceLoader 插件），仅需替换
 * {@link #load()} 的实现，调用方不受影响。
 */
public final class SkinRegistry {
    /** 皮肤清单配置文件路径（classpath 根下）。 */
    public static final String CONFIG_PATH = "skins.properties";

    /** 皮肤列表键：逗号分隔的皮肤 id。 */
    private static final String SKINS_KEY = "skins";
    /** 显示名键前缀：{@code skin.<id>.name}。 */
    private static final String NAME_KEY_PREFIX = "skin.";

    private final List<CatSkin> skins;
    private final RandomGenerator random = RandomGenerator.getDefault();

    private SkinRegistry(List<CatSkin> skins) {
        this.skins = List.copyOf(skins);
    }

    /** 从 classpath 的 {@code skins.properties} 加载皮肤注册表。 */
    public static SkinRegistry load() {
        Properties props = new Properties();
        try (InputStream stream = SkinRegistry.class.getClassLoader().getResourceAsStream(CONFIG_PATH)) {
            props.load(Objects.requireNonNull(stream, "Missing resource: " + CONFIG_PATH));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load: " + CONFIG_PATH, e);
        }
        return fromProperties(props);
    }

    /**
     * 从属性数据构建注册表（包内可见，便于测试及扩展其他数据源）。
     * Configuration format：
     * <pre>
     * skins=calico_cat,grey_tabby_cat
     * skin.calico_cat.name=Calico Cat
     * </pre>
     */
    public static SkinRegistry fromProperties(Properties props) {
        String list = props.getProperty(SKINS_KEY, "").trim();
        if (list.isEmpty()) {
            throw new IllegalStateException("No skins declared in " + CONFIG_PATH);
        }
        List<CatSkin> skins = new ArrayList<>();
        for (String id : list.split(",")) {
            id = id.trim();
            if (id.isEmpty()) {
                continue;
            }
            String displayName = props.getProperty(NAME_KEY_PREFIX + id + ".name", id);
            skins.add(new CatSkin(id, displayName));
        }
        if (skins.isEmpty()) {
            throw new IllegalStateException("No skins declared in " + CONFIG_PATH);
        }
        return new SkinRegistry(skins);
    }

    public List<CatSkin> all() {
        return skins;
    }

    public CatSkin randomSkin() {
        return skins.get(random.nextInt(skins.size()));
    }
}
