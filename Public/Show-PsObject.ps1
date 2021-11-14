<#
    .SYNOPSIS
    Reveals all properties and values of an PsObject, including all types of the properties.
#>
function Show-PsObject {

    param (
        [Parameter( Mandatory , ValueFromPipeline )]
        [ValidateNotNullOrEmpty()]
        [PsObject]
        $Object
    )

    process {

        Write-Host "`n"

        $properties = $Object | Get-Member -MemberType NoteProperty
        foreach($property in $properties) {
            $type = $property.Definition.ToString().Split()[0]
            [string]$value = $Object | Select-Object -ExpandProperty $property.Name
            if ($type -in @("string[]")) {
                $value = [System.String]::Join(";", $Object.($property.Name))
            }
            if ($type -eq "Object[]") {
                [String[]]$valueArray = $Object.($property.Name) | Convert-PsObjectToString
                $value = [System.String]::Join(";", $valueArray)
            }

            Write-Host "  $type " -ForegroundColor Gray -NoNewline
            Write-Host $property.Name -ForegroundColor Blue -NoNewline
            if ($value) {
                Write-Host " = $value" -ForegroundColor DarkGray
            } else {
                Write-Host ""
            }
        }

        Write-Host "`n"
    }
}
