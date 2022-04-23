function Get-GeneratedValue {

    param (
        [Parameter( Mandatory , ValueFromPipeline )]
        [ValidateNotNullOrEmpty()]
        [PsObject]
        $MemberObject
    )

    process {

        $index = $MemberObject.type.LastIndexOf(".")
        $rawType = $MemberObject.type.Substring($index+1)
        $name = $MemberObject.name

        $result = ""
        switch ($rawType) {
            "String" {
                $result = [string]::Format('"{0}"', $name)
            }
            "Long" {
                $random = Get-Random -Minimum 0 -Maximum 1000
                $result = [string]::Format('{0}L', $random)
            }
            "Instant" {
                $result = "Instant.now()"
            }
            "boolean" {
                $result = "true"
            }
            "LocalDateTime" {
                $date = Get-Date
                $year = $date.Year
                $month = $date.Month
                $day = $date.Day
                $hour = $date.Hour
                $minute = $date.Minute
                $result = "LocalDateTime.of($year, $month, $day, $hour, $minute, 0)"
            }
            "LocalDate" {
                $date = Get-Date
                $year = $date.Year
                $month = $date.Month
                $day = $date.Day
                $result = "LocalDate.of($year, $month, $day)"
            }
            Default {
                $result = $rawType
            }
        }

        return $result
    }
}
