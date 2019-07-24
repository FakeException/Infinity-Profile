package me.darkboy.profile.infinityprofile.resources;

import me.darkboy.profile.infinityprofile.api.InfinityPlugin;
import me.darkboy.profile.infinityprofile.api.register.Registrable;
import me.darkboy.profile.infinityprofile.api.resource.Resource;
import me.darkboy.profile.infinityprofile.api.resource.ResourceProvider;
import me.darkboy.profile.infinityprofile.api.resource.ResourceReference;

import java.util.stream.Stream;

public class Resources implements Registrable {

    private static final Resources instance = new Resources();

    public static Resources get() {
        return instance;
    }

    private InfinityPlugin plugin;

    @Override
    public void register() {
        Stream.of(ResourceType.values()).forEach(type -> plugin.getResourceProvider().loadResource(type.getReference()));
    }

    /**
     * Gets the resource from the provided resource type.
     * <p>
     * The {@link ResourceProvider} will load and cache the resource if
     * it has not already been done.
     *
     * @param reference the resource's reference.
     * @return the resource.
     */
    public Resource getResource(ResourceReference reference) {
        return plugin.getResourceProvider().getResource(reference);
    }

    public Resource getResource(ResourceType type) {
        return getResource(type.getReference());
    }

}