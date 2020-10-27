package net.nowtryz.mcutils.templating;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Patterns {
    INFO("info.yml"),
    LEADERBOARD("leaderboard.yml"),
    ADMIN("admin.yml"),
    LIST("list.yml");

    public static final String FOLDER_NAME = "guis";

    private final String fileName;
}
