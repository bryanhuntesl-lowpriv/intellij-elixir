package org.elixir_lang.beam.chunk;

import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.elixir_lang.beam.chunk.Chunk.unsignedByte;
import static org.elixir_lang.beam.chunk.Chunk.unsignedInt;

public class Atoms {
    /*
     * Fields
     */

    @NotNull
    private final List<String> atomList;

    private Atoms(@NotNull List<String> atomList) {
        this.atomList = Collections.unmodifiableList(atomList);
    }

    /*
     * Static Methods
     */

    @Nullable
    public static Atoms from(@NotNull Chunk chunk, @NotNull Chunk.TypeID typeID, @NotNull Charset charset) {
        Atoms atoms = null;

        if (chunk.typeID.equals(typeID.toString()) && chunk.data.length >= 4) {
            int offset = 0;
            Pair<Long, Integer> atomCountByteCount = unsignedInt(chunk.data, offset);
            long atomCount = atomCountByteCount.first;
            offset += atomCountByteCount.second;

            List<String> atomList = new ArrayList<>();

            for (long i = 0; i < atomCount; i++) {
                Pair<Integer, Integer> atomLengthByteCount = unsignedByte(chunk.data[offset]);
                int atomLength = atomLengthByteCount.first;
                offset += atomLengthByteCount.second;

                String entry = new String(chunk.data, offset, atomLength, charset);
                offset += atomLength;
                atomList.add(entry);
            }

            atoms = new Atoms(atomList);
        }

        return atoms;
    }

    /*
     * Instance Methods
     */

    /**
     * @param index 1-based index.  1 is reserved for {#link moduleName}
     * @return atom if it exists
     */
    @Nullable
    public String get(int index) {
        String atom = null;

        if (index >= 1) {
            // index is 1-based, so subtract 1 to get 0-based
            atom = atomList.get(index - 1);
        }

        return atom;
    }

    @Nullable
    public String moduleName() {
        String moduleName = null;

        if (atomList.size() > 0) {
            moduleName = atomList.get(0);
        }

        return moduleName;
    }
}
