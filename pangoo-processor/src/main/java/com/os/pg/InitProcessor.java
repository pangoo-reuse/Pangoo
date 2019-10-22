package com.os.pg;


import com.google.auto.service.AutoService;
import com.os.lib.Pangoo;
import com.os.lib.RootActivity;
import com.os.lib.RootFragment;
import com.os.lib.RootViewModel;
import com.os.lib.SuperLayoutMethod;
import com.os.lib.ViewModel;
import com.os.lib.help.ExcludeView;
import com.os.lib.help.ExcludeViewModel;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.swing.text.View;

@AutoService(Processor.class)
//@SupportedSourceVersion(SourceVersion.RELEASE_7) //指定java版本
public class InitProcessor extends AbstractProcessor {
    private final String greeting = "hello , Pangoo !";
    private Set<String> enums;
    private HashSet<String> viewModels;
    private HashSet<String> excludeSet;
    private TypeName rootActivityName;
    private HashSet<String> excludeViewSet;
    private HashSet<Element> viewModelAbstractMethodEles;
    private HashSet<Element> activityAbstractMethodEles;
    private boolean isDatabinding;
    private String packageId;



    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        enums = new LinkedHashSet<>();
        enums.add(RootViewModel.class.getCanonicalName());
        enums.add(ViewModel.class.getCanonicalName());
        enums.add(ExcludeViewModel.class.getCanonicalName());
        enums.add(ExcludeView.class.getCanonicalName());
        enums.add(Pangoo.class.getCanonicalName());
        enums.add(RootActivity.class.getCanonicalName());
        enums.add(RootFragment.class.getCanonicalName());
        enums.add(SuperLayoutMethod.class.getCanonicalName());
        viewModels = new HashSet<String>();
        excludeSet = new HashSet<String>();
        excludeViewSet = new HashSet<String>();
        viewModelAbstractMethodEles = new HashSet<Element>();
        activityAbstractMethodEles = new HashSet<Element>();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return enums;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    public String upperCaseName(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;

    }

    TypeName rootViewModelName = null;
    String viewModelPackageName = null;
    String sourceDirPath = null;
    String layoutId = null;
    String activityPackageName = null;
     MethodSpec viewModelConstructor;
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        boolean process = false;
        for (TypeElement te : annotations) {
            boolean contains = enums.contains(te.getQualifiedName().toString());
            if (contains) {
                process = contains;
                break;
            }
        }
        if (process) {
            parsing(roundEnv);
        }
        return true;
    }

    private void parsing(RoundEnvironment roundEnv) {
        for (Element e : roundEnv.getRootElements()) {
            // read global path include layout full path ,source dir full path,  view model package path,
            Pangoo pathConfig = e.getAnnotation(Pangoo.class);
            if (pathConfig != null) {
                packageId = pathConfig.pkg();
                viewModelPackageName = pathConfig.vm();
                activityPackageName = pathConfig.activity();
                sourceDirPath = pathConfig.source();
                layoutId = pathConfig.layout();
                isDatabinding = pathConfig.databinding();
                if(viewModelPackageName.contains("/")){
                    StringBuilder sb = new StringBuilder();
                    String[] split = viewModelPackageName.split("/");
                    if (split.length> 0){
                        for (String sp:split ) {
                            sb.append(sp).append(".");
                        }
                        viewModelPackageName = sb.subSequence(0,sb.length() -1).toString();
                    }
                }
                if(activityPackageName.contains("/")){
                    StringBuilder sb = new StringBuilder();
                    String[] split = activityPackageName.split("/");
                    if (split.length> 0){
                        for (String sp:split ) {
                            sb.append(sp).append(".");
                        }
                        activityPackageName = sb.subSequence(0,sb.length() -1).toString();
                    }
                }


            }
            //get root view model name with type
            RootViewModel rootViewModel = e.getAnnotation(RootViewModel.class);
            if (rootViewModel != null) {
                rootViewModelName = TypeName.get(e.asType());
                List<? extends Element> enclosedElements = e.getEnclosedElements();
                if (enclosedElements != null && enclosedElements.size() > 0) {

                    for (Element eee : enclosedElements) {
                        try {
                            String name = eee.getKind().name();
                            if ("CONSTRUCTOR".equalsIgnoreCase(name)){
                                MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder();
                                constructorBuilder.addModifiers( eee.getModifiers());
                                if (eee instanceof  ExecutableElement){
                                    ExecutableElement executableElement = (ExecutableElement) eee;
                                    List<? extends VariableElement> parameters = executableElement.getParameters();
                                    if (parameters != null && parameters.size()>0){
                                        StringBuilder sb  = new StringBuilder("super(");
                                        StringBuilder sbv  = new StringBuilder("");
                                        for (VariableElement ve : parameters) {

                                            ParameterSpec parameterSpec = ParameterSpec.get(ve);
                                            constructorBuilder.addParameter(parameterSpec);
                                            sb.append("$N").append(",");
                                            sbv.append(ve.getSimpleName().toString()).append(",");

                                        }
                                        StringBuilder rsb = new StringBuilder();
                                        if (sb.toString().contains(",")){
                                            rsb.append(sb.substring(0,sb.length()-1));
                                        }else {
                                            rsb = sb;
                                        }
                                        rsb.append(")");
                                        if (sbv.length() > 0){
                                            String[] split = sbv.subSequence(0, sbv.length() - 1).toString().split(",");
                                            constructorBuilder.addStatement(rsb.toString(),split);
                                        }else {
                                            constructorBuilder.addStatement(rsb.toString());
                                        }


                                    }

                                }

                                viewModelConstructor = constructorBuilder.build();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                        Set<Modifier> modifierSet = eee.getModifiers();
                        for (Modifier m : modifierSet) {
                            if (m.toString().equalsIgnoreCase("abstract"))
                                viewModelAbstractMethodEles.add(eee);
                        }
                    }

                }
            }
            //get root view model name with type
            RootActivity rootActivity = e.getAnnotation(RootActivity.class);
            if (rootActivity != null) {
                rootActivityName = TypeName.get(e.asType());
                List<? extends Element> enclosedElements = e.getEnclosedElements();
                if (enclosedElements != null && enclosedElements.size() > 0) {
                    for (Element ee : enclosedElements) {
                        Set<Modifier> modifierSet = ee.getModifiers();
                        for (Modifier m : modifierSet) {
                            if (m.toString().equalsIgnoreCase("abstract"))
                                activityAbstractMethodEles.add(ee);
                        }
                    }
                }

            }

            List<? extends Element> enclosedElements = e.getEnclosedElements();
            if (enclosedElements != null && enclosedElements.size() > 0) {
                for (Element ee : enclosedElements) {
                    ViewModel viewModel = ee.getAnnotation(ViewModel.class);
                    if (viewModel != null) {
                        String viewModelName = upperCaseName(ee.getSimpleName().toString());
                        viewModels.add(viewModelName);

                    }
                }
            }
            //read protect generate files
            ExcludeViewModel exclude = e.getAnnotation(ExcludeViewModel.class);
            if (exclude != null)
                excludeSet.add(e.getSimpleName().toString());

            //read protect generate files
            ExcludeView excludeView = e.getAnnotation(ExcludeView.class);
            if (excludeView != null)
                excludeViewSet.add(e.getSimpleName().toString());

        }

        if (viewModels != null && viewModels.size() > 0) {
            for (String name : viewModels) {
                if (!excludeSet.contains(name)) {
                    System.out.println("create viewModel : " + name);
                    createViewModel(name);
                }

            }


        }
    }

    private void createViewModel(String name) {


        TypeSpec.Builder builder = TypeSpec.classBuilder(name)
                .superclass(rootViewModelName)
                .addAnnotation(ExcludeViewModel.class)
                .addJavadoc(CodeBlock.builder().addStatement("this is autoExclude class ,please dont delete inject 'ExcludeViewModel'").build())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        builder.addMethods(getAbstractMethods(viewModelAbstractMethodEles));

        /*
          public AbcdeViewModel(TextView tv) {
            super(tv);
          }
         */
        if (viewModelConstructor != null){
            builder.addMethod(viewModelConstructor);
        }

        TypeSpec rootClassBuilder = builder.build();

        try {
            JavaFile javaFile = JavaFile.builder(packageId.concat(".").concat(viewModelPackageName), rootClassBuilder)
                    .addFileComment("this codes are generated automatically. Do not modifyActivity!")
                    .build();
            File file = new File(sourceDirPath);
            javaFile.writeTo(file);
            String activityName;
            if (name.contains("ViewModel")) {
                activityName = name.replaceAll("ViewModel", "");
            } else {
                activityName = name + "ViewModel";
            }
            writeActivity(activityName, name, layoutId, activityPackageName);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private Iterable<MethodSpec> getAbstractMethods(HashSet<Element> elements) {
        LinkedHashSet<MethodSpec> methodSpecs = new LinkedHashSet<>();
        if (elements != null && elements.size() > 0) {
            for (Element e : elements) {
                buildNormalMethod(methodSpecs, e);
            }
        }
        return methodSpecs;
    }

    private void buildNormalMethod(LinkedHashSet<MethodSpec> methodSpecs, Element e) {
        if (e instanceof ExecutableElement) {
            ExecutableElement abstractMethodEle = (ExecutableElement) e;
            TypeMirror returnType = abstractMethodEle.getReturnType();
            Set<Modifier> modifiers = abstractMethodEle.getModifiers();
            HashSet<Modifier> modifierHashSet = new HashSet<>();
            for (Modifier m : modifiers) {
                if (!m.toString().equalsIgnoreCase("abstract")) {
                    modifierHashSet.add(m);
                }

            }

            TypeName typeName = TypeName.get(returnType);
            MethodSpec.Builder builder = MethodSpec.methodBuilder(abstractMethodEle.getSimpleName().toString())
                    .addModifiers(modifierHashSet)
                    .addAnnotation(Override.class)
                    .returns(typeName);

            List<? extends VariableElement> parameters = abstractMethodEle.getParameters();
            for (VariableElement ve : parameters) {
                ParameterSpec parameterSpec = ParameterSpec.get(ve);
                builder.addParameter(parameterSpec);
            }
            builder.addStatement("$T.out.println($S)", System.class, greeting);
            if (!"void".equalsIgnoreCase(typeName.toString())) {
                builder.addStatement("return null");
            }
            MethodSpec methodSpec = builder.build();
            methodSpecs.add(methodSpec);

        }
    }

    private void writeActivity(String activityName, String viewModelName, String layoutPath, String activityPackageName) {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(activityName)
                //ParameterizedTypeName.get(Comparator.class, String.class)
                .superclass(rootActivityName)
                .addAnnotation(ExcludeView.class)
                .addJavadoc(CodeBlock.builder().addStatement("this is autoExclude class ,please dont delete inject 'ExcludeView'").build())
                .addModifiers(Modifier.PUBLIC);


        LinkedHashSet<MethodSpec> superLayoutMethods = getSuperLayoutMethod(activityAbstractMethodEles, activityName, viewModelName, classBuilder);
        classBuilder.addMethods(superLayoutMethods);

        try {
            String activityPackageFull = packageId.concat(".").concat(activityPackageName);
            JavaFile javaFile = JavaFile.builder(activityPackageFull, classBuilder.build())
                    .addFileComment(" This codes are generated automatically. Do not modifyActivity!")
                    .build();
            File file = new File(sourceDirPath);
            javaFile.writeTo(file);


            SaxHandler saxHandler = new SaxHandler();

            File layout = new File(layoutPath, Util.toHumpXmlName(activityName) + ".xml");
            if (!layout.exists()) {
                layout.createNewFile();
                saxHandler.createLayout(layout, packageId.concat(".").concat(viewModelPackageName).concat(".").concat(viewModelName));

            }


            File manifest = new File(new File(sourceDirPath).getParent(), "AndroidManifest.xml");
            saxHandler.modifyActivity(manifest, activityPackageFull.concat(".").concat(activityName));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private LinkedHashSet<MethodSpec> getSuperLayoutMethod(HashSet<Element> elements, String activityName, String viewModelName, TypeSpec.Builder classBuilder) {
        LinkedHashSet<MethodSpec> methodSpecs = new LinkedHashSet<>();
        for (Element e : elements) {
            SuperLayoutMethod layoutMethod = e.getAnnotation(SuperLayoutMethod.class);
            if (layoutMethod != null) {
                if (e instanceof ExecutableElement) {
                    ExecutableElement layoutMethodEle = (ExecutableElement) e;
                    TypeMirror returnType = layoutMethodEle.getReturnType();
                    Set<Modifier> modifiers = layoutMethodEle.getModifiers();
                    HashSet<Modifier> modifierHashSet = new HashSet<>();
                    for (Modifier m : modifiers) {
                        if (!m.toString().equalsIgnoreCase("abstract")) {
                            modifierHashSet.add(m);
                        }
                    }

                    MethodSpec.Builder builder = MethodSpec.methodBuilder(layoutMethodEle.getSimpleName().toString())
                            .addModifiers(modifierHashSet)
                            .addAnnotation(Override.class)
                            .returns(TypeName.get(returnType));

                    List<? extends VariableElement> parameters = layoutMethodEle.getParameters();
                    for (VariableElement ve : parameters) {
                        ParameterSpec parameterSpec = ParameterSpec.get(ve);
                        builder.addParameter(parameterSpec);
                    }

                    TypeName typeName = TypeName.get(returnType);
                    if (isDatabinding) {
                        // com.os.pangoo.R;
                        String bindingName = Util.toField(activityName).concat("Binding");


                        ClassName viewModelBinding = ClassName.get(packageId.concat(".").concat("databinding"), activityName.concat("Binding"));
                        classBuilder.addField(viewModelBinding, bindingName, Modifier.PUBLIC);




                        ClassName R = ClassName.get(packageId, "R");
                        ClassName dataBindingUtil = ClassName.get("androidx.databinding", "DataBindingUtil");
                        //acBinding = DataBindingUtil.setContentView(this, R.layout.ac);
                        builder.addStatement("$N = $T.setContentView(this, $T.layout.$N)", Util.toField(bindingName), dataBindingUtil, R, Util.toHumpXmlName(activityName));

//                    AcViewModel acViewModel = new AcViewModel();
                        //import com.os.pangoo.vm.AbcViewModel;
                        //ClassName bundle = ClassName.get("android.os", "Bundle");
                        //addStatement("$T bundle = new $T()",bundle)
                        String viewModelRealName = Util.toField(viewModelName);
                        ClassName viewModel = ClassName.get(packageId.concat(".").concat(viewModelPackageName), viewModelName);
                        //private ActivityLoginViewModel activityLoginViewModel;
                        classBuilder.addField(viewModel, viewModelRealName, Modifier.PRIVATE);

                        builder.addStatement("$N = new $T()", viewModelRealName, viewModel);
//                    abcViewModel AbcViewModel = new abcViewModel;
//                    acBinding.setViewModel(acViewModel);
                        builder.addStatement("$N.setViewModel($N)", bindingName, viewModelRealName);

                        //import android.view.View;
                        if (!"void".equalsIgnoreCase(typeName.toString())) {
                            //activityLoginBinding.getRoot()
                            builder.addStatement("return $N.getRoot()",bindingName);
                        }


                    }
                    methodSpecs.add(builder.build());
                }


            } else {
                buildNormalMethod(methodSpecs, e);
            }
        }
        return methodSpecs;
    }
}


