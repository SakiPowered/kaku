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

import org.bukkit.configuration.MemorySection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Leaf<T> {

    private final @NotNull Configuration configuration;
    private final @NotNull Class<T> type;
    private final @NotNull String path;

    public Leaf(@NotNull Configuration configuration, @NotNull Class<T> type, T value, @NotNull String path) {
        this.configuration = configuration;
        this.type = type;
        this.path = path;

        configuration.addDefault(path, value);
    }

    public @Nullable T get() {
        return this.getObject(path, type);
    }

    public @NotNull Leaf<T> set(@Nullable T value) {
        this.configuration.set(path, value);
        this.configuration.save();
        return this;
    }


    private T getObject(@NotNull String path, @NotNull Class<T> clazz) {
        Object def = this.getDefault(path);
        return getObject(path, clazz, (clazz.isInstance(def)) ? clazz.cast(def) : null);
    }

    private T getObject(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def) {
        Object val = this.configuration.get(path, def);
        return (clazz.isInstance(val)) ? clazz.cast(val) : def;
    }

    private Object getDefault(@NotNull String path) {
        org.bukkit.configuration.Configuration root = this.configuration.getRoot();
        org.bukkit.configuration.Configuration defaults = root == null ? null : root.getDefaults();
        return (defaults == null) ? null : defaults.get(MemorySection.createPath(this.configuration, path));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Leaf<?> that = (Leaf<?>) o;
        return Objects.equals(this.type, that.type) && Objects.equals(this.path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.path);
    }

    @Override
    public String toString() {
        return "Leaf{" +
                ", type=" + this.type +
                ", path='" + this.path + '\'' +
                '}';
    }
}