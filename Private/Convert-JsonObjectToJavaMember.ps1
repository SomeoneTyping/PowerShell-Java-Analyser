function Convert-JsonObjectToJavaMember {

    param (
        [Parameter( Mandatory , ValueFromPipeline )]
        [ValidateNotNullOrEmpty()]
        [PsObject]
        $jsonMemberObject
    )

    process {

        $javaMember = New-Object PsObject

        $validatedModfier = if ($jsonMemberObject.modifiers) { $jsonMemberObject.modifiers } else { "" }
        $validatedType = if ($jsonMemberObject.type) { $jsonMemberObject.type } else { "" }
        $validatedName = if ($jsonMemberObject.name) { $jsonMemberObject.name } else { "" }

        Add-Member -InputObject $javaMember -Name "modifiers" -Value $validatedModfier -MemberType NoteProperty
        Add-Member -InputObject $javaMember -Name "type" -Value $validatedType -MemberType NoteProperty
        Add-Member -InputObject $javaMember -Name "name" -Value $validatedName -MemberType NoteProperty

        return $javaMember
    }
}
