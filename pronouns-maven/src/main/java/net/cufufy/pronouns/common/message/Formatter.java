package net.cufufy.pronouns.common.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.cufufy.pronouns.common.platform.Platform;

import java.util.Locale;


public class Formatter {
    private final MiniMessage minimessage;
    private final Component prefix;

    public Formatter(final Platform platform) {
        minimessage = MiniMessage.builder()
                .tags(TagResolver.resolver(
                                Placeholder.parsed("main", "<reset>" + platform.config().main()),
                                Placeholder.parsed("accent", "<reset>" + platform.config().accent()),
                                TagResolver.standard()
                        )
                ).build();

        prefix = minimessage.deserialize("<accent>Pronouns<gray> » ");
    }

    public Component accent(String content) {
        return minimessage.deserialize("<accent>" + content);
    }

    public Component translated(String key, String... args) {
        return translated(key, true, args);
    }

    public Component translated(String key, boolean includePrefix, String... args) {
        final var entry = ProNounsTranslations.registry().translate(key, Locale.ROOT);
        if (entry == null) return Component.translatable(key);
        final var str = "<main>" + entry.format(args);
        final var out = minimessage.deserialize(str);
        return includePrefix ? prefix.append(out) : out;
    }
}
