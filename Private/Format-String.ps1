function Format-String {

    param (
        [Parameter( Mandatory )]
        [ValidateNotNullOrEmpty()]
        [Alias("Input")]
        [string]
        $InputString,

        [switch]
        $CamelCase,

        [switch]
        $TitleCase,

        [switch]
        $Getter,

        [switch]
        $Setter,

        [switch]
        $CapitalCase
    )

    process {

        $normalizedInput = $InputString -csplit "(?=[A-Z])" | ForEach-Object { $_.ToLower() }
        if ($InputString.Contains("_")) {
            $normalizedInput = $InputString -csplit "_" | ForEach-Object { $_.ToLower() }
        }

        $titleCaseArray = $normalizedInput | Where-Object { $_ } | Foreach-Object { $_.ToCharArray()[0].ToString().ToUpper() + $_.Remove(0, 1) }

        if ($Camelcase.IsPresent) {
            $camelCaseString = $titleCaseArray -join ""
            return $camelCaseString.ToCharArray()[0].Tostring().ToLower() + $camelCaseString.Remove(0, 1)
        }

        if ($TitleCase.IsPresent) {
            return $titleCaseArray -join ""
        }

        if ($Getter.IsPresent) {
            return ([string]::Format("get{0}", ($titleCaseArray -join "")))
        }

        if ($Setter.IsPresent) {
            return ([string]::Format("set{0}", ($titleCaseArray -join "")))
        }

        if ($CapitalCase.IsPresent) {
            return ($normalizedInput | ForEach-Object { $_.ToUpper() }) -join "_"
        }
    }
}