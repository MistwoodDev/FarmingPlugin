package me.munchii.igloolib.block.entity;

import me.munchii.igloolib.block.IglooBlock;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IglooBlockEntityTypeBuilder<T extends IglooBlockEntity> {
    private final Factory<? extends T> factory;
    private final List<IglooBlock> blocks;

    private IglooBlockEntityTypeBuilder(Factory<? extends T> factory, List<IglooBlock> blocks) {
        this.factory = factory;
        this.blocks = blocks;
    }

    public static <T extends IglooBlockEntity> IglooBlockEntityTypeBuilder<T> create(Factory<? extends T> factory, IglooBlock... blocks) {
        List<IglooBlock> blocksList = new ArrayList<>(blocks.length);
        Collections.addAll(blocksList, blocks);

        return new IglooBlockEntityTypeBuilder<>(factory, blocksList);
    }

    public IglooBlockEntityTypeBuilder<T> addBlock(IglooBlock block) {
        this.blocks.add(block);
        return this;
    }

    public IglooBlockEntityTypeBuilder<T> addBlocks(IglooBlock... blocks) {
        Collections.addAll(this.blocks, blocks);
        return this;
    }

    public IglooBlockEntityType<T> build() {
        return IglooBlockEntityType.Builder.<T>create(factory::create, blocks.toArray(new IglooBlock[0])).build();
    }

    @FunctionalInterface
    public interface Factory<T extends IglooBlockEntity> {
        T create(Location pos);
    }
}
