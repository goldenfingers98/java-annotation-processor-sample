package tn.ksoftwares.annotation.processors.service;

import com.google.auto.service.AutoService;
import tn.ksoftwares.annotation.processors.annotation.Builder;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AutoService(Processor.class)
@SupportedAnnotationTypes("tn.ksoftwares.annotation.processors.annotation.Builder")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class BuilderProcessor extends AbstractProcessor {

    private static String capitalizeFirstLetter(String str) {
        return String.format("%s%s", str.substring(0, 1).toUpperCase(), str.substring(1));
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Builder.class)) {
            if (element.getKind().isClass()) {
                final String className = String.format("%sBuilder", element.getSimpleName());
                try {
                    JavaFileObject builderFiler = processingEnv.getFiler().createSourceFile(className);
                    BufferedWriter bufferedWriter = initBufferedWriter(builderFiler, element);
                    writeBuilder(bufferedWriter, className, element);
                    bufferedWriter.close();

                } catch (IOException e) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.toString());
                }
            }
        }
        return false;
    }

    @SuppressWarnings("java:S2095")
    private static BufferedWriter initBufferedWriter(JavaFileObject builderFiler, final Element element) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(builderFiler.openWriter());
        PackageElement packageElement = (PackageElement) element.getEnclosingElement();
        // write package line
        bufferedWriter.append(String.format("package %s;", packageElement.getQualifiedName().toString()));
        bufferedWriter.newLine();
        return bufferedWriter;
    }

    private void writeBuilder(BufferedWriter bufferedWriter, final String className, Element element) throws IOException {
        // import the class of the builder
        bufferedWriter.newLine();
        bufferedWriter.append(String.format("import %s;", element.asType()));
        bufferedWriter.newLine();
        // write the class declaration
        bufferedWriter.newLine();
        bufferedWriter.append(String.format("public final class %s {", className));
        bufferedWriter.newLine();
        // get the private final fields
        List<Element> elements = element.getEnclosedElements().stream()
                .filter(it -> it.getKind().isField()
                        && !it.getModifiers().contains(Modifier.STATIC)
                        && it.getModifiers().contains(Modifier.FINAL))
                .collect(Collectors.toList());
        // write fields declarations
        elements.forEach(it -> {
            try {
                bufferedWriter.append(String.format("\tprivate %s %s;", it.asType(), it.getSimpleName()));
                bufferedWriter.newLine();
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.toString());
            }
        });
        // write builder setters
        for (Element e : elements) {
            bufferedWriter.append(String.format("\tpublic %s set%s(%s %s) {",
                    className, capitalizeFirstLetter(e.getSimpleName().toString()), e.asType(), e.getSimpleName()));
            bufferedWriter.newLine();
            bufferedWriter.append(String.format("\t\tthis.%s = %s;", e.getSimpleName(), e.getSimpleName()));
            bufferedWriter.newLine();
            bufferedWriter.append("\t\treturn this;");
            bufferedWriter.newLine();
            bufferedWriter.append("\t}");
            bufferedWriter.newLine();
        }
        // write build method
        bufferedWriter.append(String.format("\t public %s build() {", element.getSimpleName()));
        bufferedWriter.newLine();
        bufferedWriter.append(String.format("\t\treturn new %s(", element.getSimpleName()));
        for (int i = 0; i<elements.size(); i++) {
            bufferedWriter.append(String.format("%s", elements.get(i).getSimpleName()));
            if (i < elements.size() - 1) {
                bufferedWriter.append(", ");
            }
        }
        bufferedWriter.append(");");
        bufferedWriter.newLine();
        bufferedWriter.append("\t}");
        // end of generation
        bufferedWriter.newLine();
        bufferedWriter.append("}");
    }

}
