/*
 * MIT License
 *
 * Copyright (c) 2024 SakiPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package gg.saki.kaku;

import gg.saki.kaku.utils.StringUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Text implements ConfigurationSerializable {

    // ** CONSTANTS **
    private static final String MESSAGE_KEY = "message";
    private static final String POSITION_KEY = "position";

    private final String message;
    private final TextPosition position;

    public Text(String message, TextPosition position){
        this.message = message;
        this.position = position;
    }

    public Text(String message){
        this(message, TextPosition.CHAT);
    }

    public void send(CommandSender sender, Object... replace){
        String message = String.format(StringUtil.translate(this.message), replace);

        if(!(sender instanceof Player player)){
            sender.sendMessage(message);
            return;
        }

        switch(this.position){
            case CHAT -> player.sendMessage(message);
            case TITLE -> player.sendTitle(message,null,10,70,20);
            case ACTIONBAR -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        }
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        return Map.of(
                MESSAGE_KEY, this.message,
                POSITION_KEY, this.position.name()
        );
    }

    @NotNull
    public static Text deserialize(@NotNull Map<String, Object> args) {
        return new Text((String) args.get(MESSAGE_KEY), TextPosition.valueOf((String) args.get(POSITION_KEY)));
    }
}
