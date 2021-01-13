<html>
    <head>
        <style type="text/css">
            <#include "ftl.css">
        </style>
    </head>
    <p> Bonjour, </p>
    <p> Voici le rapport quotidien de l'éxecution des batch pour la periode ${data.period.toString()} : </p>
    <table>
        <thead>
        <tr class="headerOne">
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
                <tr class="groupeName">
                    <th>${groupName}</th>
                    <th class="result">${report.getResult()}</th>
                    <th></th>
                    <th></th>
                    <th></th>
                </tr>
                <#list report.reportLines as reportLine>
                    <tr class="reportLine">
                        <td>${reportLine.batchName}</td>
                        <td class="result">${reportLine.getResult()}</td>
                        <td>${reportLine.lastExec}</td>
                        <td>${reportLine.nextSlot.toString()}</td>
                        <td>
                            <#list reportLine.informations as info>
                                <p>${info}</p>
                            </#list>
                        </td>
                    </tr>
                </#list>
            </#list>
        </tbody>
    </table>
</html>