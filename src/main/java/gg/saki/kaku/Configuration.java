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

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Configuration extends YamlConfiguration {

    private final @NotNull File file;

    private final @NotNull Map<String, Leaf<?>> defaults;

    public Configuration(@NotNull JavaPlugin plugin, @NotNull File file) {
        this.file = file;
        this.defaults = new HashMap<>();

        if (!file.exists()) {
            // create file from plugin resources folder
            plugin.saveResource(file.getName().endsWith(".yml") ? file.getName() : file.getName() + ".yml", false);
        }

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
        this.options().copyDefaults(true);
        this.save();
        return leaf;
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