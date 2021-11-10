package com.github.hrozhek.stepikoscourse.week2.util;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceGetter {

    private ResourceGetter() {
    }

    public static File getResourceAsFile(String name) throws URISyntaxException {
        return getResourceAsPath(name).toFile();
    }

    public static Path getResourceAsPath(String name) throws URISyntaxException {
        return Paths.get(ResourceGetter.class.getClassLoader().getResource(name).toURI());
    }
}
