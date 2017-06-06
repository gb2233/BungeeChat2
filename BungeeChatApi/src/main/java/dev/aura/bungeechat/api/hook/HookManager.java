package dev.aura.bungeechat.api.hook;

import java.util.Collections;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import dev.aura.bungeechat.api.BungeeChatApi;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ServerType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HookManager {
    public static final int DEFAULT_PREFIX_PRIORITY = 100;
    public static final int PERMISSION_PLUGIN_PREFIX_PRIORITY = 200;
    public static final int ACCOUNT_PREFIX_PRIORITY = 300;

    private static ConcurrentMap<String, BungeeChatHook> hooks = new ConcurrentHashMap<>();
    private static final boolean validSide = BungeeChatApi.getInstance().getServerType() == ServerType.BUNGEECORD;

    public static void addHook(String name, BungeeChatHook hook) throws UnsupportedOperationException {
        checkSide();

        hooks.put(name, hook);

        sortHooks();
    }

    public static BungeeChatHook removeHook(String name) throws UnsupportedOperationException {
        checkSide();

        BungeeChatHook out = hooks.remove(name);

        sortHooks();

        return out;
    }

    public String getPrefix(BungeeChatAccount account) {
        checkSide();

        String prefix = "";
        Optional<String> out;

        for (BungeeChatHook hook : hooks.values()) {
            out = hook.getPrefix(account);

            if (out.isPresent()) {
                prefix = out.get();

                break;
            }
        }

        return prefix;
    }
    
    public String getSuffix(BungeeChatAccount account) {
        checkSide();

        String suffix = "";
        Optional<String> out;

        for (BungeeChatHook hook : hooks.values()) {
            out = hook.getSuffix(account);

            if (out.isPresent()) {
                suffix = out.get();

                break;
            }
        }

        return suffix;
    }

    private static void checkSide() throws UnsupportedOperationException {
        if (!validSide)
            throw new UnsupportedOperationException("This operation is only allowed on the BungeeCord!");
    }

    private static void sortHooks() {
        hooks = hooks.entrySet().stream().sorted(Collections.reverseOrder(Entry.comparingByValue()))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, ConcurrentHashMap::new));
    }
}
