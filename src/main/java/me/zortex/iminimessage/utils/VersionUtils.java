package me.zortex.iminimessage.utils;

import org.bukkit.Bukkit;

public final class VersionUtils {

    public static final int DEFAULT_MIN_MAJOR = 1;
    public static final int DEFAULT_MIN_MINOR = 18;
    public static final int DEFAULT_MIN_PATCH = 2;

    private VersionUtils() {}

    public static boolean isPaper() {
        try {
            Class.forName("io.papermc.paper.event.player.AsyncChatEvent");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @SuppressWarnings("unused")
    public static boolean isSupportedVersion() {
        return isSupportedVersion(DEFAULT_MIN_MAJOR, DEFAULT_MIN_MINOR, DEFAULT_MIN_PATCH);
    }

    /**
     * Checks if the current server version satisfies the specified minimum version.
     *
     * @param minMajor Major version
     * @param minMinor Minor version
     * @param minPatch Patch version
     */
    public static boolean isSupportedVersion(int minMajor, int minMinor, int minPatch) {
        String version = Bukkit.getBukkitVersion().split("-")[0];
        String[] parts = version.split("\\.");

        try {
            int major = Integer.parseInt(parts[0]);
            int minor = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
            int patch = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;

            if (major > minMajor) return true;
            if (major < minMajor) return false;

            if (minor > minMinor) return true;
            if (minor < minMinor) return false;

            return patch >= minPatch;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}