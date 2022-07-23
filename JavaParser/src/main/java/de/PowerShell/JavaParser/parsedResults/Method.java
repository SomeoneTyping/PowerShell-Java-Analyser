package de.PowerShell.JavaParser.parsedResults;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Method implements Serializable {

    private String modifiers;
    private String returnType;
    private String name;
    private List<MethodParameter> parameters = new LinkedList<>();

    public void addMethodParameter(MethodParameter newParameter) {
        parameters.add(newParameter);
    }
}
