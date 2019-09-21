function New-ConfigFileEntry {

    param (
        [Parameter( Mandatory )]
        [ValidateNotNullOrEmpty()]
        [string]
        $key,

        [Parameter( Mandatory )]
        [ValidateNotNullOrEmpty()]
        [string]
        $value
    )

    process {

        $psObject = New-Object PsObject

        Add-Member -InputObject $psObject -Name "key" -Value $key -MemberType NoteProperty
        Add-Member -InputObject $psObject -Name "value" -Value $value -MemberType NoteProperty

        return $psObject
    }
}