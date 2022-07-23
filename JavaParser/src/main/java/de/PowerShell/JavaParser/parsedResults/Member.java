package de.PowerShell.JavaParser.parsedResults;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member implements Serializable {

    private String modifiers;
    private String type;
    private String name;
}
