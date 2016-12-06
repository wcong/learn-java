package org.wcong.test.mydagger;

import com.google.auto.common.BasicAnnotationProcessor;
import com.google.common.collect.SetMultimap;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * process step ,do it last.
 * export one class,make all provide class
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2016/12/4
 */
public class MyComponentStep implements BasicAnnotationProcessor.ProcessingStep {
	@Override
	public Set<? extends Class<? extends Annotation>> annotations() {
		Set<Class<? extends Annotation>> classSet = new HashSet<>();
		classSet.add(MyComponent.class);
		return classSet;
	}

	@Override
	public Set<Element> process(SetMultimap<Class<? extends Annotation>, Element> elementsByAnnotation) {
		return null;
	}
}
