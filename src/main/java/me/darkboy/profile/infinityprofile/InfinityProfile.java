package me.darkboy.profile.infinityprofile;

import me.darkboy.profile.infinityprofile.api.InfinityPlugin;
import me.darkboy.profile.infinityprofile.commands.ProfileCMD;

public final class InfinityProfile extends InfinityPlugin {

    private static InfinityProfile instance;

    @Override
    public void enable() {
        instance = this;
        this.register(ProfileCMD.class);
    }

    @Override
    public void disable() {

    }

    public static InfinityProfile getInstance() {
        return instance;
    }
}
