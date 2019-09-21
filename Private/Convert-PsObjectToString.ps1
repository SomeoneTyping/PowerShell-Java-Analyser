function Convert-PsObjectToString {

    param (
        [Parameter( Mandatory , ValueFromPipeline )]
        [ValidateNotNullOrEmpty()]
        [PsObject]
        $Object
    )

    process {

        [string]$resultString = ""

        $properties = $Object | Get-Member -MemberType NoteProperty
        foreach($property in $properties) {
            $value = $Object | Select-Object -ExpandProperty $property.Name
            if ([string]::IsNullOrEmpty($resultString)) {
                $resultString = $value
            } else {
                $resultString += [string]::Format("/{0}", $value);
            }
        }

        return $resultString
    }
}
