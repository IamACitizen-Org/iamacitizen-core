package com.iamacitizen.core.model.annotation;

import com.iamacitizen.core.exception.SerigyException;
import com.iamacitizen.core.model.DomainObject;
import com.iamacitizen.core.util.ConfigFileLoader;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

/**
 *
 * @author felipe
 */
public class AnnotationDescriptorFinder {

    private static String getPackageName() throws SerigyException {
        Properties properties = ConfigFileLoader.load();
        return properties.getProperty("ModelPackage");
    }

    public static AnnotationDescriptor findDescriptor(String name) throws SerigyException {
        AnnotationDescriptor annotationDescriptor = null;

        String packageName = getPackageName();
		//TODO Remover API Reflection
        List<ClassLoader> classLoadersList = new LinkedList<>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));

        Set<Class<? extends DomainObject>> classes = reflections.getSubTypesOf(DomainObject.class);

        for (Class clazz : classes) {
            annotationDescriptor = new AnnotationDescriptor(clazz);

            if (annotationDescriptor.hasAnnotationName(name)) {
                return annotationDescriptor;
            }
        }

        return null;
    }
}
