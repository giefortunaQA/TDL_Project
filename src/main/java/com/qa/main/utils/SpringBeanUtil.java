package com.qa.main.utils;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanWrapperImpl;

public class SpringBeanUtil {

	public static void mergeNotNull(Object source, Object target) {
		copyProperties(source, target, getNullPropName(source));
	}

	private static String[] getNullPropName(Object source) {
		final BeanWrapperImpl wrappedSource=new BeanWrapperImpl(source);
		Set<String> propName=new HashSet<>();
		for (PropertyDescriptor descriptor:wrappedSource.getPropertyDescriptors()) {
			if (wrappedSource.getPropertyValue(descriptor.getName())==null) 
				propName.add(descriptor.getName());
		}
		return propName.toArray(new String[propName.size()]);
	}
}
