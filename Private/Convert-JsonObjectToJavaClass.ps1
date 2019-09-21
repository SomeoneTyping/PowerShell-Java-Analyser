function Convert-JsonObjectToJavaClass {

    param (
        [Parameter( Mandatory , ValueFromPipeline )]
        [ValidateNotNullOrEmpty()]
        [PsObject]
        $jsonObject
    )

    process {

        $javaClass = New-Object PsObject

        if (-not $jsonObject) {
            return $javaClass
        }

        [string]$parsedId = if ($jsonObject.id) { $jsonObject.id.id } else { "" }
        [string]$parsedPath = if ($jsonObject.path) { $jsonObject.path } else { "" }
        [string]$parsedPackage = if ($jsonObject.packageOfClass) { $jsonObject.packageOfClass } else { "" }
        [String[]]$parsedImports = if ($jsonObject.imports) { $jsonObject.imports | Select-Object -ExpandProperty id } else { @() }
        [String[]]$parsedExtendsClasses = if ($jsonObject.extendsClasses) { $jsonObject.extendsClasses | Select-Object -ExpandProperty id } else { @("") }
        [String[]]$parsedImplementsInterfaces = if ($jsonObject.implementsInterfaces) { $jsonObject.implementsInterfaces | Select-Object -ExpandProperty id } else { @("") }
        [String[]]$parsedClassAnnotations = if ($jsonObject.classAnnotations) { $jsonObject.classAnnotations } else { "" }
        [bool]$parsedIsInterface = "True".Equals($jsonObject.isInterface)
        [bool]$parsedIsEnum = "True".Equals($jsonObject.isEnum)
        [bool]$parsedIsAbstract = "True".Equals($jsonObject.isAbstract)

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

        [Object[]]$parsedMembers = if ($jsonObject.members) { $jsonObject.members | Convert-JsonObjectToJavaMember } else { @() }
        Add-Member -InputObject $javaClass -Name "members" -Value $parsedMembers -MemberType NoteProperty

        return $javaClass
    }
}
