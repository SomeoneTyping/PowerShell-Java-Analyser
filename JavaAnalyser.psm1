#----------------------------------------------------------------------------------------
# Author: Konrad Daleske
# Modul: Java Analyser
#----------------------------------------------------------------------------------------

$PublicFunctions = @( Get-ChildItem -Path "$PSScriptRoot/Public/*.ps1" -Recurse -Exclude "*Tests*" -ErrorAction SilentlyContinue )
$PrivateFunctions = @( Get-ChildItem -Path "$PSScriptRoot/Private/*.ps1" -Recurse -Exclude "*Tests*" -ErrorAction SilentlyContinue )

# Binde die Funktionen ein
foreach ($file in @($PublicFunctions + $PrivateFunctions)) {
    try {
        . $file.FullName
    }
    catch {
        $exception = ([System.ArgumentException]"Function not found")
        $errorId = "Load.Function"
        $errorCategory = 'ObjectNotFound'
        $errorTarget = $file
        $errorItem = New-Object -TypeName System.Management.Automation.ErrorRecord $exception, $errorId, $errorCategory, $errorTarget
        $errorItem.ErrorDetails = "Failed to import function $($file.BaseName)"
        throw $errorItem
    }
}

# Some Convenience Aliases
Set-Alias -Name java-init             -Value Initialize-JavaProject
Set-Alias -Name java-byimport         -Value Get-JavaClassesByImport
Set-Alias -Name java-bypackage        -Value Get-JavaClassesByPackage

Set-Alias -Name reveal                -Value Show-PsObject
Set-Alias -Name show                  -Value Show-PsObject

Export-ModuleMember -Function $PublicFunctions.BaseName -Alias *
