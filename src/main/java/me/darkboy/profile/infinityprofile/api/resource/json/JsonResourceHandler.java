/*
 * Copyright 2018 Bradley Steele
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.darkboy.profile.infinityprofile.api.resource.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import me.darkboy.profile.infinityprofile.api.resource.Extension;
import me.darkboy.profile.infinityprofile.api.resource.ResourceHandler;
import me.darkboy.profile.infinityprofile.api.resource.ResourceProvider;
import me.darkboy.profile.infinityprofile.api.resource.ResourceReference;
import me.darkboy.profile.infinityprofile.api.util.logging.StaticLog;

import java.io.*;
import java.util.List;

/**
 * @author Bradley Steele
 */
public class JsonResourceHandler implements ResourceHandler<ResourceJson> {

    private static final JsonParser parser = new JsonParser();
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    @Override
    public ResourceJson load(ResourceProvider provider, ResourceReference reference) {
        ResourceJson resource = new ResourceJson(new File(provider.getDataFolder() + reference.getSeparatorPathStart(), reference.getChild()), reference, this);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(resource.getFile())))) {
            resource.setConfiguration(parser.parse(in).getAsJsonObject());
        } catch (Exception e) {
            // Ignored
        }

        return resource;
    }

    @Override
    public void save(ResourceJson resource) {
        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(resource.getFile()));
            out.write(gson.toJson(resource.getConfiguration()));
            out.close();
        } catch (IOException e) {
            StaticLog.error("An IOException occurred when trying to save [&c" + resource.getReference() + "&r]:");
            StaticLog.exception(e);
        }
    }

    @Override
    public List<? extends CharSequence> getExtensions() {
        return Extension.JSON.getExtensions();
    }
}