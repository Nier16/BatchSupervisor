<html>
    <p> Bonjour, </p>
    <p> Un incident s'est produit pour un ou plusieurs lancement de batch. </p>
    <p> les informations concernants ces incidents sont join ci dessus : </p>
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
            <#list data.reports as groupName, report>
                <tr>
                    <th>${groupName}</th>
                    <th>${report.getResult()}</th>
                </tr>
                <#list report.reportLines as reportLine>
                    <tr>
                        <td>${reportLine.batchName}</td>
                        <td>KO</td>
                        <td>${reportLine.lastSlot.toString()}</td>
                        <td>${reportLine.nextSlot.toString()}</td>
                        <td>${reportLine.information}</td>
                    </tr>
                </#list>
            </#list>
        </tbody>
    </table>
</html>