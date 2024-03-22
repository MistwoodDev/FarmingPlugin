package me.munchii.igloolib.util;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class IglooPersistentDataType {
    public static final PersistentDataType<byte[], String[]> STRING_ARRAY = new StringArrayTagType(Charset.defaultCharset());

    public static class StringArrayTagType implements PersistentDataType<byte[], String[]> {

        private final Charset charset;

        public StringArrayTagType(final Charset charset) {
            this.charset = charset;
        }

        @Override
        public @NotNull Class<byte[]> getPrimitiveType() {
            return byte[].class;
        }

        @Override
        public @NotNull Class<String[]> getComplexType() {
            return String[].class;
        }

        @NotNull
        @Override
        public byte[] toPrimitive(final String[] strings, @NotNull final PersistentDataAdapterContext context) {
            final byte[][] allStringBytes = new byte[strings.length][];
            int total = 0;
            for (int i = 0; i < allStringBytes.length; i++) {
                final byte[] bytes = strings[i].getBytes(charset);
                allStringBytes[i] = bytes;
                total += bytes.length;
            }

            final ByteBuffer buffer = ByteBuffer.allocate(total + allStringBytes.length * 4); //stores integers
            for (final byte[] bytes : allStringBytes) {
                buffer.putInt(bytes.length);
                buffer.put(bytes);
            }

            return buffer.array();
        }

        @NotNull
        @Override
        public String[] fromPrimitive(@NotNull final byte[] bytes, @NotNull final PersistentDataAdapterContext itemTagAdapterContext) {
            final ByteBuffer buffer = ByteBuffer.wrap(bytes);
            final List<String> list = new ArrayList<>();

            while (buffer.remaining() > 0) {
                if (buffer.remaining() < 4) break;
                final int stringLength = buffer.getInt();
                if (buffer.remaining() < stringLength) break;

                final byte[] stringBytes = new byte[stringLength];
                buffer.get(stringBytes);

                list.add(new String(stringBytes, charset));
            }

            return list.toArray(new String[0]);
        }
    }
}
