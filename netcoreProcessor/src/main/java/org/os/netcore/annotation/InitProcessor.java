package org.os.netcore.annotation;

import com.google.auto.service.AutoService;
import com.os.netcoreannatation.RestApi;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class InitProcessor extends AbstractProcessor {
    private final String greeting = "this domain is : ";
    private Set<String> enums;
    private Filer filer;
    private TypeElement restApiClass;
    private TypeName apiName;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        enums = new LinkedHashSet<String>();
        enums.add(RestApi.class.getCanonicalName());
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
            RestApi restApi = e.getAnnotation(RestApi.class);
            if (restApi != null) {
                apiName = TypeName.get(e.asType());
                if (e instanceof TypeElement) {
                    restApiClass = (TypeElement) e;
                }
            }

        }
        createViewModel();

    }

    /**
     * public abstract class RootBaseTask<D> extends Task {
     *   protected ApiService api;
     *
     *   public RootBaseTask(String domain, NameValuePair<? extends Object>... params) {
     *     super(params);
     *     api(domain);
     *   }
     *
     *   private final ApiService api(@Nonnull final String domain) {
     *     Retrofit retrofit = RetrofitFactory.getInstants().createJsonRetrofit(domain);
     *     if(api == null) {
     *       api = retrofit.create(ApiService.class);
     *     }
     *     System.out.println("this domain is : " + domain);
     *     return api;
     *   }
     * }
     */
    private void createViewModel() {
        String pkg = "org.os.netcore.task";
        ClassName task = ClassName.get(pkg, "Task");
        TypeSpec.Builder builder = TypeSpec.classBuilder("RootBaseTask")
                .superclass(task)
//                .addTypeVariable(TypeVariableName.get("D"))
                .addJavadoc(CodeBlock.builder().addStatement("this is auto class ,please dont modify.use it before init api method").build())
                .addField(apiName, "api", Modifier.PROTECTED)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
        builder.addMethod(createApiMethod());
        builder.addMethod(createConstructor());
        TypeSpec spec = builder.build();

        try {
            JavaFile javaFile = JavaFile.builder(pkg, spec)
                    .addFileComment("this codes are generated automatically. do not modify !")
                    .build();
            javaFile.writeTo(filer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *      *   public RootBaseTask(String domain, NameValuePair<? extends Object>... params) {
     *      *     super(params);
     *      *     api(domain);
     *      *   }
     * @return
     */
    private MethodSpec createConstructor() {
        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder();
        ClassName paramsClassName = ClassName.get("org.os.netcore.task","NameValuePair");
        constructorBuilder.addParameter(String.class,"domain");
        constructorBuilder.addParameter(ArrayTypeName.of(ParameterizedTypeName.get(paramsClassName, WildcardTypeName.subtypeOf(Object.class))) ,"params");
        constructorBuilder.addCode(CodeBlock.of("super(params);\n"));
        constructorBuilder.addCode(CodeBlock.of("api($N);\n","domain"));
        constructorBuilder.addModifiers(Modifier.PROTECTED);
        constructorBuilder.varargs(true);
        return constructorBuilder.build();
    }


    private MethodSpec createApiMethod() {
        ClassName retrofitFactory = ClassName.get("org.os.netcore.net", "RetrofitFactory");
        ClassName retrofit = ClassName.get("retrofit2", "Retrofit");
        ClassName nonnull = ClassName.get("javax.annotation", "Nonnull");
        MethodSpec.Builder builder = MethodSpec.methodBuilder("api")
                .addModifiers(Modifier.PRIVATE,Modifier.FINAL)
                .addParameter(ParameterSpec.builder(String.class, "domain", Modifier.FINAL).addAnnotation(nonnull).build())
                .returns(apiName);

        builder.addCode(CodeBlock.builder()
                .add("$T retrofit = $T.getInstants().createJsonRetrofit(domain);\n", retrofit, retrofitFactory)
                .beginControlFlow("if(api == null)")
                .add(CodeBlock.of("api = retrofit.create($T.class);\n", ClassName.get(restApiClass)))
                .endControlFlow()
                .add("$T.out.println($S + $N);\n", System.class, greeting,"domain")
                .add("return api;\n")
                .build());


        MethodSpec methodSpec = builder.build();
        return methodSpec;
    }

}


