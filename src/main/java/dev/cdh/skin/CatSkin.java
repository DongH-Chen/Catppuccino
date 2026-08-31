package dev.cdh.skin;

/**
 * 猫皮肤的描述信息。{@code id} 同时作为资源根路径（例如 {@code "calico_cat"}）。
 *
 * @param id          皮肤标识，对应 classpath 下的资源根目录
 * @param displayName 皮肤显示名，用于界面展示
 */
public record CatSkin(String id, String displayName) {

    /** 皮肤资源根路径，例如 {@code "calico_cat"}。 */
    public String resourceRoot() {
        return id;
    }
}
