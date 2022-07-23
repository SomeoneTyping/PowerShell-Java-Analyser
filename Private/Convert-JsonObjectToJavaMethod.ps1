function Convert-JsonObjectToJavaMethod {

    param (
        [Parameter( Mandatory , ValueFromPipeline )]
        [ValidateNotNullOrEmpty()]
        [PsObject]
        $jsonMethodObject
    )

    process {

        $javaMethod = New-Object PsObject

        $validatedModfier = if ($jsonMethodObject.modifiers) { $jsonMethodObject.modifiers } else { "" }
        $validatedType = if ($jsonMethodObject.returnType) { $jsonMethodObject.returnType } else { "" }
        $validatedName = if ($jsonMethodObject.name) { $jsonMethodObject.name } else { "" }

        Add-Member -InputObject $javaMethod -Name "modifiers" -Value $validatedModfier -MemberType NoteProperty
        Add-Member -InputObject $javaMethod -Name "returnType" -Value $validatedType -MemberType NoteProperty
        Add-Member -InputObject $javaMethod -Name "name" -Value $validatedName -MemberType NoteProperty

        return $javaMethod
    }
}
