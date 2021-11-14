function Convert-JsonObjectToJavaClass {

    param (
        [Parameter( Mandatory , ValueFromPipeline )]
        [ValidateNotNullOrEmpty()]
        [PsObject]
        $JsonObject
    )

    process {

        $javaClass = New-Object PsObject

        if (-not $JsonObject) {
            return $javaClass
        }

        [string]$parsedId = if ($JsonObject.id) { $JsonObject.id.id } else { "" }
        [string]$parsedPath = if ($JsonObject.path) { $JsonObject.path } else { "" }
        [string]$parsedPackage = if ($JsonObject.packageOfClass) { $JsonObject.packageOfClass } else { "" }
        [String[]]$parsedImports = if ($JsonObject.imports) { $JsonObject.imports | Select-Object -ExpandProperty id } else { @() }
        [String[]]$parsedExtendsClasses = if ($JsonObject.extendsClasses) { $JsonObject.extendsClasses | Select-Object -ExpandProperty id } else { @("") }
        [String[]]$parsedImplementsInterfaces = if ($JsonObject.implementsInterfaces) { $JsonObject.implementsInterfaces | Select-Object -ExpandProperty id } else { @("") }
        [String[]]$parsedClassAnnotations = if ($JsonObject.classAnnotations) { $JsonObject.classAnnotations } else { "" }
        [bool]$parsedIsInterface = "True".Equals($JsonObject.isInterface)
        [bool]$parsedIsEnum = "True".Equals($JsonObject.isEnum)
        [bool]$parsedIsAbstract = "True".Equals($JsonObject.isAbstract)
        [string]$hash = Get-FileHash -Path $Path -Algorithm MD5 | Select-Object -ExpandProperty Hash

        Add-Member -InputObject $javaClass -Name "id" -Value $parsedId -MemberType NoteProperty
        Add-Member -InputObject $javaClass -Name "path" -Value $parsedPath -MemberType NoteProperty
        Add-Member -InputObject $javaClass -Name "package" -Value $parsedPackage -MemberType NoteProperty
        Add-Member -InputObject $javaClass -Name "imports" -Value $parsedImports -MemberType NoteProperty
        Add-Member -InputObject $javaClass -Name "extends" -Value $parsedExtendsClasses -MemberType NoteProperty
        Add-Member -InputObject $javaClass -Name "implements" -Value $parsedImplementsInterfaces -MemberType NoteProperty
        Add-Member -InputObject $javaClass -Name "annotations" -Value $parsedClassAnnotations -MemberType NoteProperty
        Add-Member -InputObject $javaClass -Name "isInterface" -Value $parsedIsInterface -MemberType NoteProperty
        Add-Member -InputObject $javaClass -Name "isEnum" -Value $parsedIsEnum -MemberType NoteProperty
        Add-Member -InputObject $javaClass -Name "isAbstract" -Value $parsedIsAbstract -MemberType NoteProperty
        Add-Member -InputObject $javaClass -Name "hash" -Value $hash -MemberType NoteProperty

        [Object[]]$parsedMembers = if ($JsonObject.members) { $JsonObject.members | Convert-JsonObjectToJavaMember } else { @() }
        Add-Member -InputObject $javaClass -Name "members" -Value $parsedMembers -MemberType NoteProperty

        return $javaClass
    }
}
