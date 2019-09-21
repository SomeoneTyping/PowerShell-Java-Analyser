package de.PowerShell.JavaParser.IdConverting;

import java.util.List;
import java.util.Optional;

import de.PowerShell.JavaParser.parsedResults.Id;

public class IdCreator {

    private List<Id> importDeclarations;
    private String packageDeclaration;
    private List<String> classIdentifiers;

    private IdCreator(List<Id> importDeclarations, String packageDeclaration, List<String> classIdentifiers) {
        this.importDeclarations = importDeclarations;
        this.packageDeclaration = packageDeclaration;
        this.classIdentifiers = classIdentifiers;
    }

    public Id createAbsoluteId(String id) {

        if (id == null) {
            return null;
        }

        if (classIdentifiers != null && !classIdentifiers.isEmpty()) {
            Optional<String> relevantClassIdentificator = classIdentifiers.stream()
                    .filter(a -> a.endsWith(id))
                    .findFirst();
            if (relevantClassIdentificator.isPresent()) {
                return Id.valueOf(relevantClassIdentificator.get());
            }
        }

        if (importDeclarations != null && !importDeclarations.isEmpty()) {
            Optional<Id> relevantImport = importDeclarations.stream()
                    .filter(a -> a.getIdAsString().endsWith(id))
                    .findFirst();
            if (relevantImport.isPresent()) {
                return Id.valueOf(relevantImport.get().getIdAsString());
            }
        }

        if (packageDeclaration != null) {
            return Id.valueOf(packageDeclaration + "." + id);
        }

        return Id.valueOf(id);
    }

    public static IdCreator getCreatorFor(List<Id> imports, String packageDeclaration, List<String> allClassIdentifiers) {
        return new IdCreator(imports, packageDeclaration, allClassIdentifiers);
    }

}
