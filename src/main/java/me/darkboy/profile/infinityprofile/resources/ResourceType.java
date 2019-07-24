package me.darkboy.profile.infinityprofile.resources;

import me.darkboy.profile.infinityprofile.api.resource.Extension;
import me.darkboy.profile.infinityprofile.api.resource.ResourceReference;

public enum ResourceType {

    CONFIG("Config.yml", Extension.YML),
    LANGUAGE("Language.yml", Extension.YML);

    private final ResourceReference reference;

    /**
     * @param path      the path resources path and file name, relative to the
     *                  plugins data folder.
     * @param extension the resources file extension.
     */
    ResourceType(String path, Extension extension) {
        this.reference = new ResourceReference(path, extension);
    }

    /**
     * @return the resource's reference.
     */
    public ResourceReference getReference() {
        return reference;
    }
}