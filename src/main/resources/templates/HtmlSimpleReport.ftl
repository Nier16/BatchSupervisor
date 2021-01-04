<html>
<p> Bonjour, </p>
<p> Voici le rapport quotidien de l'execution des batch pour la periode ${data.period.toString()} : </p>
<table border="solid 1px">
    <thead>
    <tr>
        <th>Nom Batch</th>
        <th>Status</th>
        <th colspan="2">Planification</th>
        <th>Information</th>
    </tr>
    <tr>
        <th></th>
        <th></th>
        <th>Derniere</th>
        <th>Prochaine</th>
        <th></th>
    </tr>
    </thead>
    <tbody>
        <#list data.reports as groupName, reportGroup>
            <tr>
                <th>${groupName}</th>
                <th>{}</th>
            </tr>
                <#list reportGroup.reportLines as reportLine>
                    <tr>
                        <td>${reportLine.batchName}</td>
                        <td>${reportLine.ok}</td>
                        <td>${reportLine.lastSlot.toString()}</td>
                        <td>${reportLine.nextSlot.toString()}</td>
                        <td>${reportLine.information}</td>
                    </tr>
                </#list>
        </#list>
    </tbody>
</table>
</html>