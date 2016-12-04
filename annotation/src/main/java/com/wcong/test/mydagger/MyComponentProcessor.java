package com.wcong.test.mydagger;

import com.google.auto.common.BasicAnnotationProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * my component processor
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2016/12/4
 */
public class MyComponentProcessor extends BasicAnnotationProcessor {
	@Override
	protected Iterable<? extends ProcessingStep> initSteps() {
		List<ProcessingStep> processingStepList = new ArrayList<>();
		processingStepList.add(new MyProvidesStep(processingEnv.getFiler(), processingEnv.getMessager()));
		return processingStepList;
	}
}
