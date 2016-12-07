package org.wcong.test.mydagger;

import com.google.auto.common.BasicAnnotationProcessor;
import com.squareup.javapoet.ParameterizedTypeName;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * my component processor
 *
 * @author wcong<wc19920415@gmail.com>
 * @since 2016/12/4
 */
public class MyComponentProcessor extends BasicAnnotationProcessor {

    private Map<ParameterizedTypeName, List<ParameterizedTypeName>> dependencyMap = new HashMap<>();

    @Override
    protected Iterable<? extends ProcessingStep> initSteps() {
        Filer filer = processingEnv.getFiler();
        Messager messager = processingEnv.getMessager();
        List<ProcessingStep> processingStepList = new ArrayList<>();
        processingStepList.add(new MyProvidesStep(filer, messager, dependencyMap));
        processingStepList.add(new MyComponentStep(filer, messager, dependencyMap));
        return processingStepList;
    }
}
