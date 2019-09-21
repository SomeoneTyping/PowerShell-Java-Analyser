package de.PowerShell.JavaParser.parsedResults;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    private String modifiers;
    private String type;
    private String name;
}
