import net.minecraft.nbt.NBTTagCompound;

public class Test {
    public static void test() {
        NBTTagCompound tag = new NBTTagCompound();
        // in test we can use the "formal" (or whatever you call it) version of NMS (the ones with real names instead of obfuscated names
        // (it doesn't work in "release" (when you run on MC if you use the de-obfuscated names)
        // TODO: we should probably create modules for each NMS version and an API interface for NMS so it's easy to upgrade
    }
}
