/*
 * MIT License
 *
 * Copyright (c) 2023 SakiPowered
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

import gg.saki.kaku.annotations.Comment;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageConfig extends Configuration{

    public MessageConfig(JavaPlugin plugin, String fileName) {
        super(plugin, fileName);
    }

    @Comment("This is your name")
    public final Leaf<String> NAME = useDefault(String.class, "DylanDeNewb", "name");
    public final Leaf<Location> LOCATION = useDefault(Location.class, new Location(Bukkit.getWorld("world"), 0,0,0), "location");

    public final Leaf<Text> CHAT = useDefault(Text.class, new Text("&#fc037bHELLO"), "text.chat");
    public final Leaf<Text> ACTIONBAR = useDefault(Text.class, new Text("&#5757ffGOODBYE", TextPosition.ACTIONBAR), "text.actionbar");
    public final Leaf<Text> TITLE = useDefault(Text.class, new Text("&#bfff57AFTERNOON", TextPosition.TITLE), "text.title");
}
