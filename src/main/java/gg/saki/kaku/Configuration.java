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
import gg.saki.kaku.annotations.CommentPosition;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public abstract class Configuration extends YamlConfiguration {


    private final JavaPlugin plugin;
    private final @NotNull File file;

    private final @NotNull Map<String, Leaf<?>> defaults;

    public Configuration(@NotNull JavaPlugin plugin, @NotNull File file) {
        ConfigurationSerialization.registerClass(Text.class);

        this.plugin = plugin;
        this.file = file;
        this.defaults = new HashMap<>();

        if (!file.exists()) {
            // create file from plugin resources folder
            plugin.saveResource(file.getName().endsWith(".yml") ? file.getName() : file.getName() + ".yml", false);
        }

        this.options().copyDefaults(true);
        this.load();
    }

    public Configuration(@NotNull JavaPlugin plugin, @NotNull String fileName) {
        this(plugin, new File(plugin.getDataFolder(), fileName));
    }


    public void load() {
        try {
            this.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> @NotNull Leaf<T> useDefault(@NotNull Class<T> type, @Nullable T value, @NotNull String path) {
        if (this.defaults.containsKey(path)) {
            return (Leaf<T>) this.defaults.get(path);
        }

        Leaf<T> leaf = new Leaf<>(this, type, value, path);
        this.defaults.put(path, leaf);
        this.save();
        return leaf;
    }

    @SuppressWarnings("unchecked")
    public <T extends Configuration> T loadComments() {

        for(Field field : this.getClass().getDeclaredFields()){
            if(field.getType() != Leaf.class) continue;

            Comment comment = field.getAnnotation(Comment.class);
            if(comment == null) continue;

            try{
                field.setAccessible(true);
                Leaf<?> leaf = (Leaf<?>) field.get(this);
                List<String> comments = Arrays.asList(comment.value());

                if (comment.position() == CommentPosition.INLINE) {
                    this.setInlineComments(leaf.getPath(), comments);
                } else {
                    this.setComments(leaf.getPath(), comments);
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.save();
        return (T) this;
    }

    public @NotNull Map<String, Leaf<?>> getDefaultLeaves() {
        return this.defaults;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Configuration that = (Configuration) o;
        return Objects.equals(this.file, that.file) && Objects.equals(this.defaults, that.defaults);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.file, this.defaults);
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "file=" + this.file +
                ", defaults=" + this.defaults +
                '}';
    }
}