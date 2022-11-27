function Save-JavaPsObject {

    param (
        [Parameter( Mandatory , ValueFromPipeline )]
        [ValidateNotNullOrEmpty()]
        [PSObject]
        $JavaClass,

        [string]
        $FilePath
    )

    process {

        if (-not $JavaClass.package) {
            Write-Error "[Save-JavaPsObject] The class " $FilePath " has no package"
            return
        }

        # Flatten first level objects
        $objectToSave = New-Object PsObject
        $properties = $JavaClass | Get-Member -MemberType NoteProperty
        foreach($property in $properties) {
            $type = $property.Definition.ToString().Split()[0]
            switch ($type) {
                "string[]" {
                    [string]$joinedString = [System.String]::Join(";", $JavaClass.($property.Name))
                    Add-Member -InputObject $objectToSave -Name $property.Name -Value $joinedString -MemberType NoteProperty
                }
                "Object[]" {
                    [string[]]$valueArray = $JavaClass.($property.Name) | ForEach-Object { Convert-PsObjectToString -Object $_ }
                    [string]$joinedString = [System.String]::Join(";", $valueArray)
                    Add-Member -InputObject $objectToSave -Name $property.Name -Value $joinedString -MemberType NoteProperty
                }
                "bool" {
                    [string]$convertedBoolean = if ($JavaClass.($property.Name)) { "True" } else { "False" }
                    Add-Member -InputObject $objectToSave -Name $property.Name -Value $convertedBoolean -MemberType NoteProperty
                }
                Default {
                    Add-Member -InputObject $objectToSave -Name $property.Name -Value $JavaClass.($property.Name) -MemberType NoteProperty
                }
            }
        }

        # Save the flattened object
        $objectToSave | Export-Csv -Path $FilePath -Delimiter "," -Append -NoTypeInformation
    }
}