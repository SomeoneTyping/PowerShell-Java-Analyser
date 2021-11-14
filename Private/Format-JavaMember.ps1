<#
    .SYNOPSIS
    This function generates code from the members of a java class
#>
function Format-JavaMember {

    param (
        [Parameter( Mandatory )]
        [ValidateNotNullOrEmpty()]
        [Alias("file")]
        [string]
        $Path,

        [Parameter( Mandatory )]
        [ValidateNotNullOrEmpty()]
        [Alias("f")]
        [string]
        $Format,

        [Alias("ex")]
        [string]
        $ExcludeModifier
    )

    process {

        if ((-not (Test-Path $Path)) -or (-not $Path.EndsWith(".java"))) {
            throw "[Format-JavaMember] The given path '$Path' does not point to a *.java file."
        }

        $javaPsObject = New-JavaPsObject -Path $Path
        $members = $javaPsObject | Select-Object -ExpandProperty members
        if ($ExcludeModifier) {
            $members = $members | Where-Object { -not ($_.modifiers.Contains($ExcludeModifier)) }
        }

        $resultList = @()
        foreach($member in $members) {
            $getter = Format-String -Input $member.name -Getter
            $setter = Format-String -Input $member.name -Setter
            $camelcase = Format-String -Input $member.name -CamelCase
            $titleCase = Format-String -Input $member.name -TitleCase
            $uppercase = Format-String -Input $member.name -CapitalCase
            $resultList += $format.Replace("[member]", $member.name).Replace("[getter]", $getter).Replace("[setter]", $setter).Replace("[camelcase]", $camelcase).Replace("[titlecase]", $titleCase).Replace("[uppercase]", $uppercase)
        }

        return $resultList
    }
}
