package de.PowerShell.JavaParser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import de.PowerShell.JavaParser.IdConverting.IdCreator;
import de.PowerShell.JavaParser.parsedResults.Id;
import de.PowerShell.JavaParser.parsedResults.JavaSerializableClass;
import de.PowerShell.JavaParser.parsedResults.Member;
import de.PowerShell.JavaParser.parsedResults.Method;
import de.PowerShell.JavaParser.parsedResults.MethodParameter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JavaParsingWorker {

    public List<JavaSerializableClass> parse(File fileToAnalyse) throws FileNotFoundException {

        CompilationUnit compilationUnit = StaticJavaParser.parse(fileToAnalyse);

        // Stuff for all classes in the file
        Optional<PackageDeclaration> packageDeclaration = compilationUnit.findFirst(PackageDeclaration.class);
        List<ImportDeclaration> importDeclarations = compilationUnit.findAll(ImportDeclaration.class);

        List<JavaSerializableClass> resultList = new LinkedList<>();

        List<ClassOrInterfaceDeclaration> allClasses = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);

        List<String> classIdentifiers = allClasses.stream()
                .filter(cl -> cl.getFullyQualifiedName().isPresent())
                .map(optional -> optional.getFullyQualifiedName().get())
                .collect(Collectors.toList());

        Optional<EnumDeclaration> enumDeclaration = compilationUnit.findFirst(EnumDeclaration.class);
        if (enumDeclaration.isPresent()) {
            JavaSerializableClass resultClass = new JavaSerializableClass();
            grazeImportDeclarations(resultClass, importDeclarations);
            packageDeclaration.ifPresent(packageDec -> resultClass.setPackageOfClass(packageDec.getName().asString()));
            IdCreator idCreator = IdCreator.getCreatorFor(resultClass.getImports(), resultClass.getPackageOfClass(), classIdentifiers);
            graceEnumDeclaration(resultClass, idCreator, enumDeclaration.get());
            resultList.add(resultClass);
        }

        for (ClassOrInterfaceDeclaration currentClass : allClasses) {

            JavaSerializableClass resultClass = new JavaSerializableClass();

            grazeImportDeclarations(resultClass, importDeclarations);
            packageDeclaration.ifPresent(packageDec -> resultClass.setPackageOfClass(packageDec.getName().asString()));
            IdCreator idCreator = IdCreator.getCreatorFor(resultClass.getImports(), resultClass.getPackageOfClass(), classIdentifiers);

            resultClass.setInterface(currentClass.isInterface());
            resultClass.setAbstract(currentClass.isAbstract());
            resultClass.setName(currentClass.getName().getIdentifier());

            List<Method> resultMethods = grazeMethods(currentClass, idCreator);
            resultClass.setMethods(resultMethods);

            Optional<String> fullQualifiedName = currentClass.getFullyQualifiedName();
            if (fullQualifiedName.isPresent()) {
                resultClass.setId(Id.valueOf(currentClass.getFullyQualifiedName().get()));
            }

            NodeList<ClassOrInterfaceType> extendsTypes = currentClass.getExtendedTypes();
            resultClass.setExtendsClasses(extendsTypes.stream()
                    .map(ext -> ext.isPrimitiveType() ? Id.valueOf(ext.getName().asString()) : idCreator.createAbsoluteId(ext.getName().asString()))
                    .collect(Collectors.toList()));

            NodeList<ClassOrInterfaceType> implementsTypes = currentClass.getImplementedTypes();
            resultClass.setImplementsInterfaces(implementsTypes.stream()
                    .map(impl -> idCreator.createAbsoluteId(impl.getNameAsString()))
                    .collect(Collectors.toList()));

            resultClass.setClassAnnotations(currentClass.getAnnotations().stream()
                    .map(a -> a.getName().asString())
                    .collect(Collectors.toList()));

            resultClass.setMembers(currentClass.getMembers().stream()
                    .filter(BodyDeclaration::isFieldDeclaration)
                    .map(mem -> convertFieldDeclarationToMember((FieldDeclaration)mem, idCreator))
                    .collect(Collectors.toList()));

            resultClass.setTestClass(currentClass.getMembers().stream()
                    .filter(BodyDeclaration::isMethodDeclaration)
                    .anyMatch(mem -> analyseAnnotationsIfTheyContainTestAnnotation(mem.getAnnotations())));

            resultList.add(resultClass);
        }

        return resultList;
    }

    private List<Method> grazeMethods(ClassOrInterfaceDeclaration currentClass, IdCreator idCreator) {

        List<Method> resultMethods = new LinkedList<>();

        List<MethodDeclaration> methods = currentClass.findAll(MethodDeclaration.class, Node.TreeTraversal.DIRECT_CHILDREN);
        for (MethodDeclaration currentMethod : methods) {
            Method resultMethod = new Method();
            resultMethod.setModifiers(currentMethod.getModifiers().stream().map(a -> a.getKeyword().asString()).collect(Collectors.joining(" ")));
            String parsedReturnType = currentMethod.getType().getElementType().isPrimitiveType()
                    ? currentMethod.getType().getElementType().asString()
                    : idCreator.createAbsoluteId(currentMethod.getType().getElementType().asString()).getIdAsString();
            resultMethod.setReturnType(parsedReturnType);
            resultMethod.setName(currentMethod.getName().asString());
            NodeList<Parameter> parameters = currentMethod.getParameters();
            for (Parameter currentParameter : parameters) {
                MethodParameter resultParameter = new MethodParameter();
                resultParameter.setName(currentParameter.getNameAsString());
                String parsedParameterType = currentParameter.getType().getElementType().isPrimitiveType()
                        ? currentParameter.getType().getElementType().asString()
                        : idCreator.createAbsoluteId(currentParameter.getType().getElementType().asString()).getIdAsString();
                resultParameter.setType(parsedParameterType);
                resultMethod.addMethodParameter(resultParameter);
            }
            resultMethods.add(resultMethod);
        }

        return resultMethods;
    }

    private void grazeImportDeclarations(JavaSerializableClass result, List<ImportDeclaration> importDeclarations) {

        List<Id> importsAsIdList = new LinkedList<>();
        for (ImportDeclaration oneImport : importDeclarations) {
            importsAsIdList.add(Id.valueOf(oneImport.getName().toString()));
        }
        result.setImports(importsAsIdList);
    }

    private void graceEnumDeclaration(JavaSerializableClass result, IdCreator idCreator, EnumDeclaration enumDeclaration) {

        result.setEnum(enumDeclaration.isEnumDeclaration());

        result.setName(enumDeclaration.getName().getIdentifier());
        Optional<String> fullQualifiedName = enumDeclaration.getFullyQualifiedName();
        if (fullQualifiedName.isPresent()) {
            result.setId(Id.valueOf(enumDeclaration.getFullyQualifiedName().get()));
        }

        result.setClassAnnotations(enumDeclaration.getAnnotations().stream()
                .map(a -> a.getName().asString())
                .collect(Collectors.toList()));

        result.setEnumEntries(enumDeclaration.getEntries().stream()
                .filter(EnumConstantDeclaration::isEnumConstantDeclaration)
                .map(e -> e.getName().asString())
                .collect(Collectors.toList()));

        result.setMembers(enumDeclaration.getMembers().stream()
                .filter(BodyDeclaration::isFieldDeclaration)
                .map(mem -> convertFieldDeclarationToMember((FieldDeclaration)mem, idCreator))
                .collect(Collectors.toList()));
    }

    private Member convertFieldDeclarationToMember(FieldDeclaration field, IdCreator idFactory) {

        Member generatedMember = new Member();

        generatedMember.setModifiers(field.getModifiers().stream().map(a -> a.getKeyword().asString()).collect(Collectors.joining(" ")));
        String parsedType = field.getElementType().isPrimitiveType()
                ? field.getElementType().asString()
                : idFactory.createAbsoluteId(field.getElementType().asString()).getIdAsString();
        generatedMember.setType(parsedType);

        Optional<String> optionalName = field.getVariables().stream().map(a -> a.getName().asString()).findFirst();
        optionalName.ifPresent(generatedMember::setName);

        return generatedMember;
    }

    private boolean analyseAnnotationsIfTheyContainTestAnnotation(NodeList<AnnotationExpr> annotations) {

        return annotations.stream().anyMatch(a -> a.getName().asString().equals("Test"));
    }

}
